package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.ArticleEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;
import io.github.jiashunx.tools.sqlite3.util.SQLite3Utils;

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
    protected String getListAllSQL() {
        return SQLite3Utils.getClassTableModel(getEntityClass()).getSelectAllSQL(stringBuilder -> {
            stringBuilder.append(" ORDER BY CREATE_TIME DESC");
        });
    }

    @Override
    public void init() {

    }
}
