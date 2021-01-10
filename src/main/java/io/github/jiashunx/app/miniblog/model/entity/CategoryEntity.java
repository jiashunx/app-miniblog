package io.github.jiashunx.app.miniblog.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

import java.util.Date;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_CATEGORY")
public class CategoryEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "CATEGORY_ID")
    private String categoryId;

    @SQLite3Column(columnName = "CATEGORY_NAME")
    private String categoryName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @SQLite3Column(columnName = "CREATE_TIME")
    private Date createTime;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
