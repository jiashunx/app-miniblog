package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.TagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

/**
 * @author jiashunx
 */
public class TagService extends SQLite3Service<TagEntity, String> implements IService {

    TagService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {

    }

    @Override
    protected Class<TagEntity> getEntityClass() {
        return TagEntity.class;
    }
}
