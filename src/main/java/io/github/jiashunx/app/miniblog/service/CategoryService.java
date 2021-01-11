package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.util.Constants;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.*;

/**
 * @author jiashunx
 */
public class CategoryService extends DefaultService<CategoryEntity> implements IService {

    private final DatabaseService databaseService;
    private final SQLite3JdbcTemplate jdbcTemplate;

    CategoryService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
        this.databaseService = Objects.requireNonNull(serviceBus).getDatabaseService();
        this.jdbcTemplate = this.databaseService.getJdbcTemplate();
    }

    @Override
    public void init() {

    }

    @Override
    protected Class<CategoryEntity> getEntityClass() {
        return CategoryEntity.class;
    }

}
