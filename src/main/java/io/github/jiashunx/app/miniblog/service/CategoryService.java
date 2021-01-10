package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.util.Constants;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.*;

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

    private final CategoryEntity emptyEntity = new CategoryEntity();
    private final Map<String, CategoryEntity> entityMap = new HashMap<>();

    public List<CategoryEntity> listAll() {
        synchronized (entityMap) {
            List<CategoryEntity> entities = new ArrayList<>(entityMap.values());
            if (entities.isEmpty()) {
                entities = jdbcTemplate.queryForList(databaseService.getDQL(Constants.DQL_QUERY_CATEGORY_ALL), CategoryEntity.class);
            }
            for (CategoryEntity entity: entities) {
                entityMap.put(entity.getCategoryId(), entity);
            }
            return new ArrayList<>(entityMap.values());
        }
    }

    public CategoryEntity update(CategoryEntity entity) {
        synchronized (entityMap) {
            jdbcTemplate.update(entity);
            entityMap.put(entity.getCategoryId(), entity);
        }
        return entity;
    }

    public CategoryEntity insert(CategoryEntity entity) {
        synchronized (entityMap) {
            jdbcTemplate.insert(entity);
            entityMap.put(entity.getCategoryId(), entity);
        }
        return entity;
    }

    public CategoryEntity deleteOne(String categoryId) {
        return null;
    }

    public CategoryEntity findOne(String categoryId) {
        return null;
    }

}
