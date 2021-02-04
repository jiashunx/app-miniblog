package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.ArticleEntity;
import io.github.jiashunx.app.miniblog.service.ArticleService;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
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

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/article/*")
public class ArticleManageServlet extends AbstractRestServlet {

    private static final String ARTICLE_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/article-index.html");
    private static final String ARTICLE_EDIT_HTML = IOUtils.loadContentFromClasspath("template/console/article-edit.html");

    private final ArticleService articleService;

    public ArticleManageServlet(ServiceBus serviceBus) {
        this.articleService = Objects.requireNonNull(serviceBus.getArticleService());
    }

    @PostMapping(url = "/console/article/edit")
    public void actionEdit(MRestRequest request, MRestResponse response) {
        Map<String, Object> params = (Map<String, Object>) request.parseBodyToObj(Map.class);
        String articleId = params.get("articleId").toString();
        String articleName = params.get("articleName").toString();
        String articleContent = params.get("articleContent").toString();
        String articleIdLocator = params.get("articleIdLocator").toString();
        String articleDescription = params.get("articleDescription").toString();
        // 新增
        if (StringUtils.isBlank(articleId)) {
            ArticleEntity entity = new ArticleEntity();
            entity.setArticleId(UUID.randomUUID().toString());
            entity.setArticleName(articleName);
            entity.setArticleContent(articleContent.getBytes(StandardCharsets.UTF_8));
            entity.setArticleIdLocator(articleIdLocator);
            entity.setArticleDescription(articleDescription);
            entity.setCreateTime(new Date());
            entity.setLastModifiedTime(entity.getCreateTime());
            articleService.insert(entity);
        } else {
            // 更新
            ArticleEntity entity = articleService.find(articleId);
            entity.setArticleName(articleName);
            entity.setArticleDescription(articleDescription);
            entity.setArticleContent(articleContent.getBytes(StandardCharsets.UTF_8));
            entity.setArticleIdLocator(articleIdLocator);
            entity.setArticleDescription(articleDescription);
            entity.setLastModifiedTime(new Date());
            articleService.update(entity);
        }
    }

    @PostMapping(url = "/console/article/delete")
    public void actionDelete(MRestRequest request, MRestResponse response) {

    }

    @GetMapping(url = "/console/article/create.html")
    public void createArticle(MRestRequest request, MRestResponse response) {
        Kv kv = new Kv();
        kv.put("status", "新建文章");
        kv.put("articleId", "");
        kv.put("articleName", "");
        kv.put("articleContent", "");
        kv.put("articleIdLocator", "");
        kv.put("articleDescription", "");
        response.write(BlogUtils.render(ARTICLE_EDIT_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    @GetMapping(url = "/console/article/edit.html")
    public void editArticle(MRestRequest request, MRestResponse response) {
        // TODO 获取待编辑的数据
        Kv kv = new Kv();
        kv.put("status", "编辑文章");
        kv.put("articleId", "");
        kv.put("articleName", "");
        kv.put("articleContent", "");
        kv.put("articleIdLocator", "");
        kv.put("articleDescription", "");
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
