package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleCategoryEntity;
import io.github.jiashunx.app.miniblog.model.entity.ArticleTagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiashunx
 */
public class ArticleCategoryService extends SQLite3Service<ArticleCategoryEntity, String> implements IService {

    public ArticleCategoryService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {}

    public List<ArticleCategoryEntity> listCategoryArticles(String categoryId) {
        List<ArticleCategoryEntity> entities = getCategoryArticleMap().get(categoryId);
        if (entities == null) {
            entities = new ArrayList<>(0);
        }
        return entities;
    }

    public Map<String, List<ArticleCategoryEntity>> getCategoryArticleMap() {
        Map<String, List<ArticleCategoryEntity>> categoryArticleMap = new HashMap<>();
        listAll().forEach(articleCategoryEntity -> {
            String categoryId = articleCategoryEntity.getCategoryId();
            categoryArticleMap.computeIfAbsent(categoryId, key -> new ArrayList<>()).add(articleCategoryEntity);
        });
        return categoryArticleMap;
    }

    @Override
    protected Class<ArticleCategoryEntity> getEntityClass() {
        return ArticleCategoryEntity.class;
    }
}
