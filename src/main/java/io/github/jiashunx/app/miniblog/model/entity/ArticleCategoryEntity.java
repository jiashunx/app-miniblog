package io.github.jiashunx.app.miniblog.model.entity;

import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_ARTICLE_CATEGORY")
public class ArticleCategoryEntity {

    @SQLite3Id
    @SQLite3Column(columnName = "ARTICLE_ID")
    private String articleId;

    @SQLite3Column(columnName = "CATEGORY_ID")
    private String categoryId;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
