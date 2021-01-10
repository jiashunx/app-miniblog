package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author jiashunx
 */
public class CategoryService implements IService {

    private final DatabaseService databaseService;
    private final SQLite3JdbcTemplate jdbcTemplate;

    CategoryService(ServiceBus serviceBus) {
        this.databaseService = Objects.requireNonNull(serviceBus).getDatabaseService();
        this.jdbcTemplate = this.databaseService.getJdbcTemplate();
    }

    @Override
    public void init() {

    }

    public List<CategoryEntity> listAll() {
        return null;
    }

    public CategoryEntity update(CategoryEntity entity) {
        return null;
    }

    public CategoryEntity insert(CategoryEntity entity) {
        return null;
    }

    public CategoryEntity deleteOne(String categoryId) {
        return null;
    }

    public CategoryEntity findOne(String categoryId) {
        return null;
    }

}
