package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.*;
import io.github.jiashunx.app.miniblog.service.*;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.PostMapping;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.github.jiashunx.masker.rest.framework.util.StringUtils;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/article/*")
public class ArticleManageServlet extends AbstractRestServlet {

    private static final String ARTICLE_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/article-index.html");
    private static final String ARTICLE_EDIT_HTML = IOUtils.loadContentFromClasspath("template/console/article-edit.html");

    private final SQLite3JdbcTemplate sqLite3JdbcTemplate;
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ArticleCategoryService articleCategoryService;
    private final TagService tagService;
    private final ArticleTagService articleTagService;

    public ArticleManageServlet(ServiceBus serviceBus) {
        this.sqLite3JdbcTemplate = serviceBus.getDatabaseService().getJdbcTemplate();
        this.articleService = Objects.requireNonNull(serviceBus.getArticleService());
        this.categoryService = Objects.requireNonNull(serviceBus.getCategoryService());
        this.articleCategoryService = Objects.requireNonNull(serviceBus.getArticleCategoryService());
        this.tagService = Objects.requireNonNull(serviceBus.getTagService());
        this.articleTagService = Objects.requireNonNull(serviceBus.getArticleTagService());
    }

    @PostMapping(url = "/console/article/edit")
    public void actionEdit(MRestRequest request, MRestResponse response) {
        Map<String, Object> params = (Map<String, Object>) request.parseBodyToObj(Map.class);
        String articleId = params.get("articleId").toString();
        String categoryId = params.get("categoryId").toString();
        String articleName = params.get("articleName").toString();
        String articleContent = params.get("articleContent").toString();
        String articleIdLocator = params.get("articleIdLocator").toString();
        String articleDescription = params.get("articleDescription").toString();
        // 新增
        if (StringUtils.isBlank(articleId)) {
            ArticleEntity articleEntity = new ArticleEntity();
            articleEntity.setArticleId(UUID.randomUUID().toString());
            articleEntity.setArticleName(articleName);
            articleEntity.setArticleContent(articleContent.getBytes(StandardCharsets.UTF_8));
            articleEntity.setArticleIdLocator(articleIdLocator);
            articleEntity.setArticleDescription(articleDescription);
            articleEntity.setCreateTime(new Date());
            articleEntity.setLastModifiedTime(articleEntity.getCreateTime());
            ArticleCategoryEntity articleCategoryEntity = new ArticleCategoryEntity();
            articleCategoryEntity.setArticleId(articleEntity.getArticleId());
            articleCategoryEntity.setCategoryId(categoryId);
            sqLite3JdbcTemplate.doTransaction(() -> {
                articleService.insert(articleEntity);
                articleCategoryService.insert(articleCategoryEntity);
            });
        } else {
            // 更新
            ArticleEntity articleEntity = articleService.find(articleId);
            articleEntity.setArticleName(articleName);
            articleEntity.setArticleDescription(articleDescription);
            articleEntity.setArticleContent(articleContent.getBytes(StandardCharsets.UTF_8));
            articleEntity.setArticleIdLocator(articleIdLocator);
            articleEntity.setArticleDescription(articleDescription);
            articleEntity.setLastModifiedTime(new Date());
            ArticleCategoryEntity articleCategoryEntity = articleCategoryService.find(articleId);
            if (articleCategoryEntity == null) {
                articleCategoryEntity = new ArticleCategoryEntity();
                articleCategoryEntity.setArticleId(articleId);
            }
            articleCategoryEntity.setCategoryId(categoryId);
            AtomicReference<ArticleCategoryEntity> articleCategoryEntityRef = new AtomicReference<>(articleCategoryEntity);
            sqLite3JdbcTemplate.doTransaction(() -> {
                articleService.update(articleEntity);
                articleCategoryService.update(articleCategoryEntityRef.get());
            });
        }
    }

    @PostMapping(url = "/console/article/delete")
    public void actionDelete(MRestRequest request, MRestResponse response) {
        Map<String, Object> params = (Map<String, Object>) request.parseBodyToObj(Map.class);
        String articleId = params.get("articleId").toString();
        sqLite3JdbcTemplate.doTransaction(() -> {
            articleService.deleteById(articleId);
            articleCategoryService.deleteById(articleId);
        });
    }

    @GetMapping(url = "/console/article/edit.html")
    public void editArticle(MRestRequest request, MRestResponse response) {
        String articleId = request.getParameter("articleId");
        Kv kv = new Kv();
        if (StringUtils.isBlank(articleId)) {
            kv.put("status", "新建文章");
            kv.put("articleId", "");
            kv.put("articleName", "");
            kv.put("articleContent", "");
            kv.put("articleIdLocator", "");
            kv.put("articleDescription", "");
            kv.put("categoryId", "");
        } else {
            ArticleEntity entity = articleService.find(articleId);
            kv.put("status", "编辑文章");
            kv.put("articleId", articleId);
            kv.put("articleName", entity.getArticleName());
            kv.put("articleContent", new String(entity.getArticleContent(), StandardCharsets.UTF_8));
            kv.put("articleIdLocator", entity.getArticleIdLocator());
            kv.put("articleDescription", entity.getArticleDescription());
            ArticleCategoryEntity articleCategoryEntity = articleCategoryService.find(articleId);
            String categoryId = "";
            if (articleCategoryEntity != null) {
                categoryId = articleCategoryEntity.getCategoryId();
            }
            kv.put("categoryId", categoryId);
        }
        List<CategoryEntity> categoryEntityList = categoryService.listAll();
        List<Map<String, Object>> categoryVoList = new ArrayList<>(categoryEntityList.size());
        Map<String, Object> defaultCategory = new HashMap<>();
        defaultCategory.put("categoryId", "");
        defaultCategory.put("categoryName", "==请选择==");
        defaultCategory.put("disabled", "disabled");
        categoryVoList.add(defaultCategory);
        categoryEntityList.forEach(categoryEntity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("categoryId", categoryEntity.getCategoryId());
            map.put("categoryName", categoryEntity.getCategoryName());
            map.put("disabled", "disabled-no");
            categoryVoList.add(map);
        });
        kv.put("categoryVoList", categoryVoList);
        List<TagEntity> tagEntityList = tagService.listAll();
        List<Map<String, Object>> tagVoList = new ArrayList<>(tagEntityList.size());
        Map<String, Object> defaultTag = new HashMap<>();
        defaultTag.put("tagId", "");
        defaultTag.put("tagName", "==请选择==");
        defaultTag.put("disabled", "disabled");
        tagVoList.add(defaultTag);
        tagEntityList.forEach(tagEntity -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tagId", tagEntity.getTagId());
            map.put("tagName", tagEntity.getTagName());
            map.put("disabled", "disabled-no");
            tagVoList.add(map);
        });
        kv.put("tagVoList", tagVoList);
        response.write(BlogUtils.render(ARTICLE_EDIT_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    @GetMapping(url = "/console/article/index.html")
    public void index(MRestRequest request, MRestResponse response) {
        List<ArticleEntity> entityList = articleService.listAll();
        List<Map<String, Object>> mapList = new ArrayList<>(entityList.size());
        for (ArticleEntity entity : entityList) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("articleId", entity.getArticleId());
            objectMap.put("articleIdLocator", entity.getArticleIdLocator());
            objectMap.put("articleName", entity.getArticleName());
            objectMap.put("createTime", BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMddHHmmssSSS));
            objectMap.put("lastModifiedTime", BlogUtils.format(entity.getLastModifiedTime(), BlogUtils.yyyyMMddHHmmssSSS));
            mapList.add(objectMap);
        }
        Kv kv = new Kv();
        kv.put("articleVoList", mapList);
        response.write(BlogUtils.render(ARTICLE_MANAGE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
