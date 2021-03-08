package io.github.jiashunx.app.miniblog.index;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.IndexModel;
import io.github.jiashunx.app.miniblog.model.IndexRow;
import io.github.jiashunx.app.miniblog.model.PageableIndex;
import io.github.jiashunx.app.miniblog.model.entity.*;
import io.github.jiashunx.app.miniblog.service.*;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

/**
 * @author jiashunx
 */
public class IndexServlet extends AbstractRestServlet {

    private static final String INDEX_HTML = IOUtils.loadContentFromClasspath("template/index/index.html");
    private static final String ARTICLE_HTML = IOUtils.loadContentFromClasspath("template/index/article.html");
    private static final String TAG_HTML = IOUtils.loadContentFromClasspath("template/index/tags.html");
    private static final String CATEGORY_HTML = IOUtils.loadContentFromClasspath("template/index/categories.html");

    private final SQLite3JdbcTemplate sqLite3JdbcTemplate;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ArticleCategoryService articleCategoryService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    public IndexServlet(ServiceBus serviceBus) {
        super();
        this.sqLite3JdbcTemplate = serviceBus.getDatabaseService().getJdbcTemplate();
        this.articleService = Objects.requireNonNull(serviceBus.getArticleService());
        this.categoryService = Objects.requireNonNull(serviceBus.getCategoryService());
        this.articleCategoryService = Objects.requireNonNull(serviceBus.getArticleCategoryService());
        this.tagService = Objects.requireNonNull(serviceBus.getTagService());
        this.articleTagService = Objects.requireNonNull(serviceBus.getArticleTagService());
    }

    @GetMapping(url = "/")
    public void root(MRestRequest request, MRestResponse response) {
        index0(1, request, response);
    }

    @GetMapping(url = "/page/{pageIndex}")
    public void index(MRestRequest request, MRestResponse response) {
        index0(Integer.parseInt(request.getPathVariable("pageIndex")), request, response);
    }

    @GetMapping(url = "/categories/{categoryName}")
    public void renderCategoryArticles(MRestRequest request, MRestResponse response) {
        String categoryName = request.getPathVariable("categoryName");
        CategoryEntity categoryEntity = categoryService.findByCategoryName(categoryName);
        if (categoryEntity == null) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        String categoryId = categoryEntity.getCategoryId();
        List<ArticleCategoryEntity> articleCategoryEntityList = articleCategoryService.getCategoryArticleMap().get(categoryId);
        List<ArticleEntity> articleEntityList = new ArrayList<>();
        if (articleCategoryEntityList != null) {
            articleCategoryEntityList.forEach(articleCategoryEntity -> {
                ArticleEntity articleEntity = articleService.find(articleCategoryEntity.getArticleId());
                if (articleEntity == null) {
                    throw new NullPointerException();
                }
                articleEntityList.add(articleEntity);
            });
        }
        List<IndexRow> indexRowList = new ArrayList<>();
        for (ArticleEntity entity: articleEntityList) {
            IndexRow indexRow = new IndexRow();
            indexRow.setUrl("/" + BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd).replace("-", "/") + "/" + entity.getArticleIdLocator());
            indexRow.setTitle(entity.getArticleName());
            indexRow.setCreateTime(BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd));
            indexRowList.add(indexRow);
        }
        Kv kv = new Kv();
        kv.put("indexRowList", indexRowList);
        kv.put("urlPrefix", "..");
        response.write(BlogUtils.render(INDEX_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    @GetMapping(url = "/tags/{tagName}")
    public void renderTagArticles(MRestRequest request, MRestResponse response) {
        String tagName = request.getPathVariable("tagName");
        TagEntity tagEntity = tagService.findByTagName(tagName);
        if (tagEntity == null) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        String tagId = tagEntity.getTagId();
        List<ArticleTagEntity> articleTagEntityList = articleTagService.getTagArticleMap().get(tagId);
        List<ArticleEntity> articleEntityList = new ArrayList<>();
        if (articleTagEntityList != null) {
            articleTagEntityList.forEach(articleTagEntity -> {
                ArticleEntity articleEntity = articleService.find(articleTagEntity.getArticleId());
                if (articleEntity == null) {
                    throw new NullPointerException();
                }
                articleEntityList.add(articleEntity);
            });
        }
        List<IndexRow> indexRowList = new ArrayList<>();
        for (ArticleEntity entity: articleEntityList) {
            IndexRow indexRow = new IndexRow();
            indexRow.setUrl("/" + BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd).replace("-", "/") + "/" + entity.getArticleIdLocator());
            indexRow.setTitle(entity.getArticleName());
            indexRow.setCreateTime(BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd));
            indexRowList.add(indexRow);
        }
        Kv kv = new Kv();
        kv.put("indexRowList", indexRowList);
        kv.put("urlPrefix", "..");
        response.write(BlogUtils.render(INDEX_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    @GetMapping(url = "/{yyyy}/{MM}/{dd}/{locatorId}")
    public void locateArticle(MRestRequest request, MRestResponse response) {
        String createTimeStr = String.format("%s-%s-%s", request.getPathVariable("yyyy")
            , request.getPathVariable("MM"), request.getPathVariable("dd"));
        String locatorId = request.getPathVariable("locatorId");
        ArticleEntity entity = articleService.findByLocatorIdAndDate(createTimeStr, locatorId);
        if (entity == null) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        List<ArticleTagEntity> articleTagEntityList = articleTagService.getArticleTagMap().get(entity.getArticleId());
        List<Map<String, Object>> tagList = new ArrayList<>();
        if (articleTagEntityList != null) {
            articleTagEntityList.forEach(articleTagEntity -> {
                String tagId = articleTagEntity.getTagId();
                String tagName = tagService.find(tagId).getTagName();
                Map<String, Object> map = new HashMap<>();
                map.put("tagId", tagId);
                map.put("tagName", tagName);
                tagList.add(map);
            });
        }
        Kv kv = new Kv();
        kv.put("title", entity.getArticleName());
        kv.put("createTime", createTimeStr);
        kv.put("url", request.getOriginUrl());
        kv.put("content", new String(entity.getArticleContent()));
        kv.put("keywords", entity.getArticleKeywords());
        kv.put("description", entity.getArticleDescription());
        kv.put("tagList", tagList);
        response.write(BlogUtils.render(ARTICLE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    private void index0(int pageIndex, MRestRequest request, MRestResponse response) {
        List<ArticleEntity> entityList = articleService.listAll();
        int totalSize = entityList.size();
        int pageRowCount = 20;
        int pageSize = (totalSize / pageRowCount) + ((totalSize % pageRowCount) > 0 ? 1 : 0);
        if (pageIndex < 1 || pageSize > 0 && pageIndex > pageSize) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        IndexModel indexModel = new IndexModel();
        List<IndexRow> indexRowList = new ArrayList<>();
        int startIndex = (pageIndex - 1) * pageRowCount + 1;
        int endIndex = pageIndex * pageRowCount;
        if (endIndex > totalSize) {
            endIndex = totalSize;
        }
        for (int index = startIndex - 1; index < endIndex; index++) {
            ArticleEntity entity = entityList.get(index);
            IndexRow indexRow = new IndexRow();
            indexRow.setUrl("/" + BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd).replace("-", "/") + "/" + entity.getArticleIdLocator());
            indexRow.setTitle(entity.getArticleName());
            indexRow.setCreateTime(BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMdd));
            indexRowList.add(indexRow);
        }
        indexModel.setIndexRowList(indexRowList);
        if (pageIndex > 1) {
            PageableIndex pageableIndex = new PageableIndex();
            pageableIndex.setIndexNo(pageIndex - 1);
            pageableIndex.setIndexUrl("/page/" + (pageIndex - 1));
            indexModel.setPrevPageableIndex(pageableIndex);
        }
        if (pageIndex < pageSize) {
            PageableIndex pageableIndex = new PageableIndex();
            pageableIndex.setIndexNo(pageIndex + 1);
            pageableIndex.setIndexUrl("/page/" + (pageIndex + 1));
            indexModel.setNextPageableIndex(pageableIndex);
        }
        List<PageableIndex> pageableIndexList = new ArrayList<>();
        PageableIndex currentPageableIndex = new PageableIndex();
        currentPageableIndex.setCurrent(true);
        currentPageableIndex.setIndexNo(pageIndex);
        if (pageIndex - 1 > 3) {
            PageableIndex firstPageableIndex = new PageableIndex();
            firstPageableIndex.setIndexNo(1);
            firstPageableIndex.setIndexUrl("/page/1");
            PageableIndex ignorePageableIndex = new PageableIndex();
            ignorePageableIndex.setIgnore(true);
            pageableIndexList.add(firstPageableIndex);
            pageableIndexList.add(ignorePageableIndex);
            for (int index = pageIndex - 2; index < pageIndex; index++) {
                PageableIndex pageableIndex = new PageableIndex();
                pageableIndex.setIndexNo(index);
                pageableIndex.setIndexUrl("/page/" + index);
                pageableIndexList.add(pageableIndex);
            }
        } else {
            for (int index = 1; index < pageIndex; index++) {
                PageableIndex pageableIndex = new PageableIndex();
                pageableIndex.setIndexNo(index);
                pageableIndex.setIndexUrl("/page/" + index);
                pageableIndexList.add(pageableIndex);
            }
        }
        pageableIndexList.add(currentPageableIndex);
        if (pageSize - pageIndex > 3) {
            for (int index = pageIndex + 1; index < pageIndex + 3; index++) {
                PageableIndex pageableIndex = new PageableIndex();
                pageableIndex.setIndexNo(index);
                pageableIndex.setIndexUrl("/page/" + index);
                pageableIndexList.add(pageableIndex);
            }
            PageableIndex ignorePageableIndex = new PageableIndex();
            ignorePageableIndex.setIgnore(true);
            pageableIndexList.add(ignorePageableIndex);
        } else {
            for (int index = pageIndex + 1; index <= pageSize; index++) {
                PageableIndex pageableIndex = new PageableIndex();
                pageableIndex.setIndexNo(index);
                pageableIndex.setIndexUrl("/page/" + index);
                pageableIndexList.add(pageableIndex);
            }
        }
        indexModel.setPageableIndexList(pageableIndexList);
        Kv kv = new Kv();
        kv.put("indexRowList", indexModel.getIndexRowList());
        kv.put("prevPageableIndex", indexModel.getPrevPageableIndex());
        kv.put("pageableIndexList", indexModel.getPageableIndexList());
        kv.put("nextPageableIndex", indexModel.getNextPageableIndex());
        String urlPrefix = "";
        String requestUrl = request.getUrl();
        if (requestUrl.equals("/")) {
            urlPrefix = "";
        } else if (requestUrl.matches("^/\\d{4}/\\d{1,2}/\\d{1,2}/\\S+$")) {
            urlPrefix = "../../..";
        }
        kv.put("urlPrefix", urlPrefix);
        response.write(BlogUtils.render(INDEX_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    /**
     * 所有分类页面
     */
    @GetMapping(url = "/categories")
    public void categories(MRestRequest request, MRestResponse response) {
        List<CategoryEntity> entityList = categoryService.listAll();
        List<Map<String, Object>> categoryList = new ArrayList<>();
        for (CategoryEntity entity: entityList) {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryName", entity.getCategoryName());
            categoryList.add(map);
        }
        Kv kv = new Kv();
        kv.put("categoryList", categoryList);
        response.write(BlogUtils.render(CATEGORY_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    /**
     * 所有tag页面
     */
    @GetMapping(url = "/tags")
    public void tags(MRestRequest request, MRestResponse response) {
        List<TagEntity> entityList = tagService.listAll();
        List<Map<String, Object>> tagList = new ArrayList<>();
        for (TagEntity entity: entityList) {
            Map<String, Object> map = new HashMap<>();
            map.put("tagName", entity.getTagName());
            tagList.add(map);
        }
        Kv kv = new Kv();
        kv.put("tagList", tagList);
        response.write(BlogUtils.render(TAG_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
