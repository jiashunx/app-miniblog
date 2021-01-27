package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

/**
 * @author jiashunx
 */
public class ArticleService extends SQLite3Service<ArticleEntity, String> implements IService {

    public ArticleService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    protected Class<ArticleEntity> getEntityClass() {
        return ArticleEntity.class;
    }

    @Override
    public void init() {

    }
}
