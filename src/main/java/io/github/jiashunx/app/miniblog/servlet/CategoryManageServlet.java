package io.github.jiashunx.app.miniblog.servlet;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.model.entity.FileEntity;
import io.github.jiashunx.app.miniblog.service.CategoryService;
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
            case "/console/category/create":
                create(request, response);
                break;
            case "/console/category/update":
                update(request, response);
                break;
            case "/console/category/delete":
                delete(request, response);
                break;
            default:
                break;
        }
    }

    private void create(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        entity.setCategoryId(UUID.randomUUID().toString());
        entity.setCategoryName(String.valueOf(entity.getCategoryName()));
        entity.setCreateTime(new Date());
        categoryService.insert(entity);
        response.write(HttpResponseStatus.OK);
    }

    private void update(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        CategoryEntity storedEntity = categoryService.find(entity.getCategoryId());
        // may throw NullPointerException
        storedEntity.setCategoryName(entity.getCategoryName());
        categoryService.update(storedEntity);
        response.write(HttpResponseStatus.OK);
    }

    private void delete(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        categoryService.deleteById(entity.getCategoryId());
        response.write(HttpResponseStatus.OK);
    }

    private void index(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        List<CategoryEntity> entityList = categoryService.listAll();
        List<Map<String, Object>> mapList = new ArrayList<>(entityList.size());
        for (CategoryEntity entity : entityList) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("categoryId", entity.getCategoryId());
            objectMap.put("categoryName", entity.getCategoryName());
            objectMap.put("createTime", BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMddHHmmssSSS));
            mapList.add(objectMap);
        }
        Kv kv = new Kv();
        kv.put("categoryVoList", mapList);
        response.write(BlogUtils.render(CATEGORY_MANAGE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
