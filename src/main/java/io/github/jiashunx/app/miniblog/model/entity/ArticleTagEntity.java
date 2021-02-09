package io.github.jiashunx.app.miniblog.model.entity;

import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_ARTICLE_TAG")
public class ArticleTagEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "ARTICLE_TAG_ID")
    private String articleTagId;

    @SQLite3Column(columnName = "ARTICLE_ID")
    private String articleId;

    @SQLite3Column(columnName = "TAG_ID")
    private String tagId;

    public String getArticleTagId() {
        return articleTagId;
    }

    public void setArticleTagId(String articleTagId) {
        this.articleTagId = articleTagId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
