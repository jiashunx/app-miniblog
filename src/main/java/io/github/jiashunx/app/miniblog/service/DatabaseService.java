package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;
import io.github.jiashunx.tools.sqlite3.table.SQLPackage;
import io.github.jiashunx.tools.sqlite3.util.SQLite3SQLHelper;

/**
 * @author jiashunx
 */
public class DatabaseService implements IService {

    private final SQLite3JdbcTemplate jdbcTemplate;

    private final SQLPackage sqlPackage;

    DatabaseService() {
        String dbFilePath = System.getProperty("user.home") + "/.miniblog/blog.db";
        jdbcTemplate = new SQLite3JdbcTemplate(dbFilePath);
        sqlPackage = SQLite3SQLHelper.loadSQLPackageFromClasspath("miniblog/sql.xml");
        jdbcTemplate.initSQLPackage(sqlPackage);
    }

    @Override
    public void init() {
        jdbcTemplate.initSQLPackage(sqlPackage);
    }

    public SQLite3JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public SQLPackage getSqlPackage() {
        return sqlPackage;
    }

    public String getDQL(String sqlId) {
        return getSqlPackage().getDQL(sqlId).getContent();
    }

    public String getDML(String sqlId) {
        return getSqlPackage().getDML(sqlId).getContent();
    }

}
