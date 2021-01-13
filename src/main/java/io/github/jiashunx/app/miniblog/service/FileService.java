package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.FileEntity;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

/**
 * @author jiashunx
 */
public class FileService extends SQLite3Service<FileEntity, String> implements IService {

    FileService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
    }

    @Override
    public void init() {
        // do nothing.
    }

    @Override
    protected Class<FileEntity> getEntityClass() {
        return FileEntity.class;
    }

}
