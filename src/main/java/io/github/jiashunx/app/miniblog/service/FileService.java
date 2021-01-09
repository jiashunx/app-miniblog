package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.FileEntity;
import io.github.jiashunx.app.miniblog.util.Constants;
import io.github.jiashunx.masker.rest.framework.util.StringUtils;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.*;

/**
 * @author jiashunx
 */
public class FileService implements IService {

    private final DatabaseService databaseService;
    private final SQLite3JdbcTemplate jdbcTemplate;

    FileService(ServiceBus serviceBus) {
        this.databaseService = Objects.requireNonNull(serviceBus).getDatabaseService();
        this.jdbcTemplate = this.databaseService.getJdbcTemplate();
    }

    @Override
    public void init() {
        // do nothing.
    }

    private final FileEntity emptyFileEntity = new FileEntity();
    private final Map<String, FileEntity> fileVoMap = new HashMap<>();

    public List<FileEntity> listAll() {
        return jdbcTemplate.queryForList(databaseService.getDQL(Constants.DQL_QUERY_FILE_ALL), FileEntity.class);
    }

    public FileEntity update(FileEntity fileEntity) {
        synchronized (fileVoMap) {
            jdbcTemplate.update(fileEntity);
            fileVoMap.put(fileEntity.getFileId(), fileEntity);
        }
        return fileEntity;
    }

    public FileEntity insert(FileEntity fileEntity) {
        return insert(Collections.singletonList(fileEntity)).get(0);
    }

    public List<FileEntity> insert(List<FileEntity> fileEntityList) {
        if (fileEntityList == null) {
            throw new NullPointerException();
        }
        Map<String, FileEntity> voMap = new HashMap<>();
        fileEntityList.forEach(fileEntity -> {
            if (StringUtils.isEmpty(fileEntity.getFileName())) {
                throw new NullPointerException("file name is empty");
            }
            if (StringUtils.isBlank(fileEntity.getFileId())) {
                fileEntity.setFileId(UUID.randomUUID().toString());
            }
            voMap.put(fileEntity.getFileId(), fileEntity);
        });
        synchronized (fileVoMap) {
            jdbcTemplate.insert(fileEntityList);
            fileVoMap.putAll(voMap);
        }
        return fileEntityList;
    }

    public FileEntity deleteOne(String fileId) {
        FileEntity fileEntity = findOne(fileId);
        if (fileEntity != null) {
            jdbcTemplate.executeUpdate(databaseService.getDML(Constants.DML_DELETE_FILE_BY_ID), statement -> {
                statement.setString(1, fileId);
            });
        }
        return fileEntity;
    }

    public void delete(List<String> fileIdList) {
        jdbcTemplate.batchUpdate(
                databaseService.getDML(Constants.DML_DELETE_FILE_BY_ID)
                , fileIdList.size()
                , (index, statement) -> {
                    statement.setString(1, fileIdList.get(index));
        });
    }

    public FileEntity findOne(String fileId) {
        FileEntity fileEntity = fileVoMap.get(fileId);
        if (fileEntity == null) {
            synchronized (fileVoMap) {
                fileEntity = fileVoMap.get(fileId);
                if (fileEntity == null) {
                    fileEntity = jdbcTemplate.queryForObj(databaseService.getDQL(Constants.DQL_QUERY_FILE_BY_ID), statement -> {
                        statement.setString(1, fileId);
                    }, FileEntity.class);
                    if (fileEntity == null) {
                        fileEntity = emptyFileEntity;
                    }
                    fileVoMap.put(fileId, fileEntity);
                }
            }
        }
        if (fileEntity == emptyFileEntity) {
            fileEntity = null;
        }
        return fileEntity;
    }

}
