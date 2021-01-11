package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.FileEntity;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.github.jiashunx.tools.sqlite3.service.SQLite3Service;

import java.util.*;

/**
 * @author jiashunx
 */
public class FileService extends SQLite3Service<FileEntity, String> implements IService {

    private final DatabaseService databaseService;
    private final SQLite3JdbcTemplate jdbcTemplate;

    FileService(ServiceBus serviceBus) {
        super(serviceBus.getDatabaseService().getJdbcTemplate());
        this.databaseService = Objects.requireNonNull(serviceBus).getDatabaseService();
        this.jdbcTemplate = this.databaseService.getJdbcTemplate();
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
