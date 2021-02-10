package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleTagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

import java.util.List;

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
        return null;
    }

    public List<ArticleTagEntity> listTagArticles(String tagId) {
        return null;
    }

    @Override
    protected Class<ArticleTagEntity> getEntityClass() {
        return ArticleTagEntity.class;
    }
}
