package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.app.miniblog.model.entity.ArticleCategoryEntity;
import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.service.ArticleCategoryService;
import io.github.jiashunx.app.miniblog.service.CategoryService;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.MServlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.PostMapping;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;

import java.util.*;

/**
 * @author jiashunx
 */
public class CategoryManageServlet extends AbstractRestServlet {

    private static final String CATEGORY_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/category-index.html");

    private final CategoryService categoryService;
    private final ArticleCategoryService articleCategoryService;

    public CategoryManageServlet(CategoryService categoryService, ArticleCategoryService articleCategoryService) {
        this.categoryService = Objects.requireNonNull(categoryService);
        this.articleCategoryService = Objects.requireNonNull(articleCategoryService);
    }

    @PostMapping(url = "/console/category/create")
    public void create(MRestRequest request, MRestResponse response) {
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        entity.setCategoryId(UUID.randomUUID().toString());
        entity.setCategoryName(String.valueOf(entity.getCategoryName()));
        entity.setCreateTime(new Date());
        categoryService.insert(entity);
    }

    @PostMapping(url = "/console/category/update")
    public void update(MRestRequest request, MRestResponse response) {
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        CategoryEntity storedEntity = categoryService.find(entity.getCategoryId());
        // may throw NullPointerException
        storedEntity.setCategoryName(entity.getCategoryName());
        categoryService.update(storedEntity);
    }

    @PostMapping(url = "/console/category/delete")
    public void delete(MRestRequest request, MRestResponse response) {
        CategoryEntity entity = request.parseBodyToObj(CategoryEntity.class);
        List<ArticleCategoryEntity> articleCategoryEntityList = articleCategoryService.listCategoryArticles(entity.getCategoryId());
        if (!articleCategoryEntityList.isEmpty()) {
            throw new MiniBlogException(String.format("there is %d articles linked to category: %s", articleCategoryEntityList.size(), entity.getCategoryName()));
        }
        categoryService.deleteById(entity.getCategoryId());
    }

    @GetMapping(url = "/console/category/index.html")
    public void index(MRestRequest request, MRestResponse response) {
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
