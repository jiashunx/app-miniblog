package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.TagEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;
import io.github.jiashunx.tools.sqlite3.util.SQLite3Utils;

import java.util.List;

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

    public TagEntity findByTagName(String tagName) {
        List<TagEntity> entityList = listAll();
        for (TagEntity entity: entityList) {
            if (tagName.equals(entity.getTagName())) {
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
    protected Class<TagEntity> getEntityClass() {
        return TagEntity.class;
    }
}
