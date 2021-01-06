package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.FileVo;
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

    private final FileVo emptyFileVo = new FileVo();
    private final Map<String, FileVo> fileVoMap = new HashMap<>();

    public List<FileVo> listAll() {
        return jdbcTemplate.queryForList(databaseService.getDQL(Constants.DQL_QUERY_FILE_ALL), FileVo.class);
    }

    public FileVo update(FileVo fileVo) {
        synchronized (fileVoMap) {
            jdbcTemplate.update(fileVo);
            fileVoMap.put(fileVo.getFileId(), fileVo);
        }
        return fileVo;
    }

    public FileVo insert(FileVo fileVo) {
        return insert(Collections.singletonList(fileVo)).get(0);
    }

    public List<FileVo> insert(List<FileVo> fileVoList) {
        if (fileVoList == null) {
            throw new NullPointerException();
        }
        Map<String, FileVo> voMap = new HashMap<>();
        fileVoList.forEach(fileVo -> {
            if (StringUtils.isEmpty(fileVo.getFileName())) {
                throw new NullPointerException("file name is empty");
            }
            if (StringUtils.isBlank(fileVo.getFileId())) {
                fileVo.setFileId(UUID.randomUUID().toString());
            }
            voMap.put(fileVo.getFileId(), fileVo);
        });
        synchronized (fileVoMap) {
            jdbcTemplate.insert(fileVoList);
            fileVoMap.putAll(voMap);
        }
        return fileVoList;
    }

    public FileVo deleteOne(String fileId) {
        FileVo fileVo = findOne(fileId);
        if (fileVo != null) {
            jdbcTemplate.executeUpdate(databaseService.getDML(Constants.DML_DELETE_FILE_BY_ID), statement -> {
                statement.setString(1, fileId);
            });
        }
        return fileVo;
    }

    public void delete(List<String> fileIdList) {
        jdbcTemplate.batchUpdate(
                databaseService.getDML(Constants.DML_DELETE_FILE_BY_ID)
                , fileIdList.size()
                , (index, statement) -> {
                    statement.setString(1, fileIdList.get(index));
        });
    }

    public FileVo findOne(String fileId) {
        FileVo fileVo = fileVoMap.get(fileId);
        if (fileVo == null) {
            synchronized (fileVoMap) {
                fileVo = fileVoMap.get(fileId);
                if (fileVo == null) {
                    fileVo = jdbcTemplate.queryForObj(databaseService.getDQL(Constants.DQL_QUERY_FILE_BY_ID), statement -> {
                        statement.setString(1, fileId);
                    }, FileVo.class);
                    if (fileVo == null) {
                        fileVo = emptyFileVo;
                    }
                    fileVoMap.put(fileId, fileVo);
                }
            }
        }
        if (fileVo == emptyFileVo) {
            fileVo = null;
        }
        return fileVo;
    }

}
