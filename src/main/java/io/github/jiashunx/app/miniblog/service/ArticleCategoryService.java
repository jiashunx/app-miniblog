package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleCategoryEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

/**
 * @author jiashunx
 */
public class ArticleCategoryService extends SQLite3Service<ArticleCategoryEntity, String> implements IService {

    public ArticleCategoryService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {}

    @Override
    protected Class<ArticleCategoryEntity> getEntityClass() {
        return ArticleCategoryEntity.class;
    }
}
