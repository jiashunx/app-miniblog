package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.ArticleEntity;
import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.service.ArticleService;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/article/*")
public class ArticleManageServlet implements MRestServlet {

    private static final String ARTICLE_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/article-index.html");

    private final ArticleService articleService;

    public ArticleManageServlet(ServiceBus serviceBus) {
        this.articleService = Objects.requireNonNull(serviceBus.getArticleService());
    }

    @Override
    public void service(MRestRequest request, MRestResponse response) {
        String requestUrl = request.getUrl();
        switch (requestUrl) {
            case "/console/article/index.html":
                index(request, response);
                break;
            default:
                response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
                break;
        }
    }

    private void index(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
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
        response.write(BlogUtils.render(ARTICLE_MANAGE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
