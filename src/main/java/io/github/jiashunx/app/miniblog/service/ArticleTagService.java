package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleTagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiashunx
 */
public class ArticleTagService extends SQLite3Service<ArticleTagEntity, String> implements IService {

    public ArticleTagService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {

    }

    public List<ArticleTagEntity> listArticleTags(String articleId) {
        List<ArticleTagEntity> entities = getArticleTagMap().get(articleId);
        if (entities == null) {
            entities = new ArrayList<>(0);
        }
        return entities;
    }

    public List<ArticleTagEntity> listTagArticles(String tagId) {
        List<ArticleTagEntity> entities = getTagArticleMap().get(tagId);
        if (entities == null) {
            entities = new ArrayList<>(0);
        }
        return entities;
    }

    public Map<String, List<ArticleTagEntity>> getArticleTagMap() {
        Map<String, List<ArticleTagEntity>> articleTagMap = new HashMap<>();
        listAll().forEach(articleTagEntity -> {
            String articleId = articleTagEntity.getArticleId();
            articleTagMap.computeIfAbsent(articleId, key -> new ArrayList<>()).add(articleTagEntity);
        });
        return articleTagMap;
    }

    public Map<String, List<ArticleTagEntity>> getTagArticleMap() {
        Map<String, List<ArticleTagEntity>> tagArticleMap = new HashMap<>();
        listAll().forEach(tagArticleEntity -> {
            String tagId = tagArticleEntity.getTagId();
            tagArticleMap.computeIfAbsent(tagId, key -> new ArrayList<>()).add(tagArticleEntity);
        });
        return tagArticleMap;
    }

    @Override
    protected Class<ArticleTagEntity> getEntityClass() {
        return ArticleTagEntity.class;
    }
}
