package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.TagEntity;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

import java.util.Objects;

/**
 * @author jiashunx
 */
public class TagService extends SQLite3Service<TagEntity, String> implements IService {

    private final DatabaseService databaseService;
    private final SQLite3JdbcTemplate jdbcTemplate;

    TagService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
        this.databaseService = Objects.requireNonNull(serviceBus).getDatabaseService();
        this.jdbcTemplate = this.databaseService.getJdbcTemplate();
    }

    @Override
    public void init() {

    }

    @Override
    protected Class<TagEntity> getEntityClass() {
        return TagEntity.class;
    }
}
