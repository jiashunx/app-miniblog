package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleTagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

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

    @Override
    protected Class<ArticleTagEntity> getEntityClass() {
        return ArticleTagEntity.class;
    }
}
