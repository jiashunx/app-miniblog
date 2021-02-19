package io.github.jiashunx.app.miniblog.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.IndexModel;
import io.github.jiashunx.app.miniblog.model.IndexRow;
import io.github.jiashunx.app.miniblog.model.PageableIndex;
import io.github.jiashunx.app.miniblog.model.entity.ArticleEntity;
import io.github.jiashunx.app.miniblog.service.*;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.filter.Filter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilterChain;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author jiashunx
 */
@Filter(urlPatterns = "/*", order = Integer.MAX_VALUE)
public class IndexServlet implements MRestFilter {

    private static final String INDEX_HTML = IOUtils.loadContentFromClasspath("template/index/index.html");

    private final SQLite3JdbcTemplate sqLite3JdbcTemplate;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ArticleCategoryService articleCategoryService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    public IndexServlet(ServiceBus serviceBus) {
        this.sqLite3JdbcTemplate = serviceBus.getDatabaseService().getJdbcTemplate();
        this.articleService = Objects.requireNonNull(serviceBus.getArticleService());
        this.categoryService = Objects.requireNonNull(serviceBus.getCategoryService());
        this.articleCategoryService = Objects.requireNonNull(serviceBus.getArticleCategoryService());
        this.tagService = Objects.requireNonNull(serviceBus.getTagService());
        this.articleTagService = Objects.requireNonNull(serviceBus.getArticleTagService());
    }

    @Override
    public void doFilter(MRestRequest request, MRestResponse response, MRestFilterChain filterChain) {
        String requestUrl = request.getUrl();
        switch (requestUrl) {
            case "/":
                index(1, request, response);
                break;
            case "/timeline":
                timeline(request, response);
                break;
            case "/categories":
                categories(request, response);
                break;
            case "/tags":
                tags(request, response);
                break;
            default:
                //    /2021/02/23/article-short-id
                if (requestUrl.matches("^/\\d{4}/\\d{1,2}/\\d{1,2}/\\S+$")) {

                } else if (requestUrl.matches("^/tag/\\S+$")) {
                    //    /tags/tag-name
                } else if (requestUrl.matches("^/categories/\\S+$")) {
                    //    /categories/category-name
                } else if (requestUrl.matches("^/page/\\d+$")) {
                    //    /page/page-index
                    int pageIndex = Integer.parseInt(requestUrl.substring(requestUrl.lastIndexOf("/") + 1));
                    index(pageIndex, request, response);
                } else {
                    filterChain.doFilter(request, response);
                }
                break;
        }
    }

    public void index(int pageIndex, MRestRequest request, MRestResponse response) {
        List<ArticleEntity> entityList = articleService.listAll();
        int totalSize = entityList.size();
        int pageRowCount = 20;
        int pageSize = (totalSize / pageRowCount) + ((totalSize % pageRowCount) > 0 ? 1 : 0);
        if (pageIndex < 1 || pageIndex > pageSize) {
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
            indexRow.setUrl(BlogUtils.format(entity.getCreateTime(), "/yyyy/mm/dd") + "/" + entity.getArticleIdLocator());
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
        response.write(BlogUtils.render(INDEX_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    /**
     * 时间轴页面
     */
    public void timeline(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有分类页面
     */
    public void categories(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有tag页面
     */
    public void tags(MRestRequest request, MRestResponse response) {

    }

}
