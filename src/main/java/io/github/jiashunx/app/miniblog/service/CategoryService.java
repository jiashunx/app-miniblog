package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.CategoryEntity;
import io.github.jiashunx.app.miniblog.model.entity.TagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;
import io.github.jiashunx.tools.sqlite3.util.SQLite3Utils;

import java.util.List;

/**
 * @author jiashunx
 */
public class CategoryService extends SQLite3Service<CategoryEntity, String> implements IService {

    CategoryService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {

    }

    public CategoryEntity findByCategoryName(String categoryName) {
        List<CategoryEntity> entityList = listAll();
        for (CategoryEntity entity: entityList) {
            if (categoryName.equals(entity.getCategoryName())) {
                return entity;
            }
        }
        return null;
    }

    @Override
    protected String getListAllSQL() {
        return SQLite3Utils.getClassTableModel(getEntityClass()).getSelectAllSQL(stringBuilder -> {
            stringBuilder.append(" ORDER BY CREATE_TIME DESC");
        });
    }

    @Override
    protected Class<CategoryEntity> getEntityClass() {
        return CategoryEntity.class;
    }

}
