package io.github.jiashunx.app.miniblog.model;

import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_FILE")
public class FileVo {

    @SQLite3Id
    @SQLite3Column(columnName = "FILE_ID")
    private String fileId;

    @SQLite3Column(columnName = "FILE_NAME")
    private String fileName;

    @SQLite3Column(columnName = "FILE_BYTE_SIZE")
    private int fileByteSize;

    @SQLite3Column(columnName = "FILE_CONTENT")
    private byte[] fileBytes;

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
}
