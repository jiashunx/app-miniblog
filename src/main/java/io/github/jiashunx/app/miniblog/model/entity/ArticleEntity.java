package io.github.jiashunx.app.miniblog.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

import java.util.Date;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_ARTICLE")
public class ArticleEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "ARTICLE_ID")
    private String articleId;

    @SQLite3Column(columnName = "ARTICLE_NAME")
    private String articleName;

    @SQLite3Column(columnName = "ARTICLE_DESCRIPTION")
    private String articleDescription;

    @SQLite3Column(columnName = "ARTICLE_CONTENT")
    private byte[] articleContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @SQLite3Column(columnName = "CREATE_TIME")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @SQLite3Column(columnName = "LAST_MODIFIED_TIME")
    private Date lastModifiedTime;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public byte[] getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(byte[] articleContent) {
        this.articleContent = articleContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
