package io.github.jiashunx.app.miniblog.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

import java.util.Date;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_TAG")
public class TagEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "TAG_ID")
    private String tagId;

    @SQLite3Column(columnName = "TAG_NAME")
    private String tagName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @SQLite3Column(columnName = "CREATE_TIME")
    private Date createTime;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
