package io.github.jiashunx.app.miniblog.servlet;

import io.github.jiashunx.app.miniblog.service.CategoryService;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/category/*")
public class CategoryManageServlet implements MRestServlet {

    private static final Logger logger = LoggerFactory.getLogger(CategoryManageServlet.class);

    private static final String CATEGORY_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/category-index.html");

    private final CategoryService categoryService;

    public CategoryManageServlet(CategoryService categoryService) {
        this.categoryService = Objects.requireNonNull(categoryService);
    }

    @Override
    public void service(MRestRequest request, MRestResponse response) {
        String requestUrl = request.getUrl();
        switch (requestUrl) {
            case "/console/category/index.html":
                index(request, response);
                break;
            default:
                break;
        }
    }

    private void index(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        response.write(CATEGORY_MANAGE_HTML.getBytes(StandardCharsets.UTF_8)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
