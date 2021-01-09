package io.github.jiashunx.app.miniblog.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

import java.util.Date;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_FILE")
public class FileEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "FILE_ID")
    private String fileId;

    @SQLite3Column(columnName = "FILE_NAME")
    private String fileName;

    @SQLite3Column(columnName = "FILE_BYTE_SIZE")
    private int fileByteSize;

    @SQLite3Column(columnName = "FILE_CONTENT")
    private byte[] fileBytes;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @SQLite3Column(columnName = "CREATE_TIME")
    private Date createTime;

    public FileEntity() {}

    public FileEntity(FileEntity fileEntity) {
        this.fileId = fileEntity.getFileId();
        this.fileName = fileEntity.getFileName();
        this.fileBytes = fileEntity.getFileBytes();
        this.fileByteSize = fileEntity.getFileByteSize();
        this.createTime = fileEntity.getCreateTime();
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileByteSize() {
        return fileByteSize;
    }

    public void setFileByteSize(int fileByteSize) {
        this.fileByteSize = fileByteSize;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
