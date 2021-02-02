package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.service.CategoryService;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.PostMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.RequestMapping;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/category/*")
@RequestMapping(url = "/console/category")
public class CategoryManageServlet extends AbstractRestServlet {

    private static final String CATEGORY_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/category-index.html");

    private final CategoryService categoryService;

    public CategoryManageServlet(CategoryService categoryService) {
        this.categoryService = Objects.requireNonNull(categoryService);
    }

    @PostMapping(url = "/create")
    private void create(MRestRequest request, MRestResponse response) {
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        entity.setCategoryId(UUID.randomUUID().toString());
        entity.setCategoryName(String.valueOf(entity.getCategoryName()));
        entity.setCreateTime(new Date());
        categoryService.insert(entity);
    }

    @PostMapping(url = "/update")
    private void update(MRestRequest request, MRestResponse response) {
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        CategoryEntity storedEntity = categoryService.find(entity.getCategoryId());
        // may throw NullPointerException
        storedEntity.setCategoryName(entity.getCategoryName());
        categoryService.update(storedEntity);
    }

    @PostMapping(url = "/delete")
    private void delete(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        categoryService.deleteById(entity.getCategoryId());
    }

    @GetMapping(url = "/index.html")
    private void index(MRestRequest request, MRestResponse response) {
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
