package io.github.jiashunx.miniblog.database;

import io.github.jiashunx.miniblog.model.Vo;
import io.github.jiashunx.miniblog.model.ConfigVo;

/**
 * @author jiashunx
 */
public class DatabaseClient {

    private final Database database;
    private final ConfigVo configVo;

    public DatabaseClient(String rootPath) {
        this.database = new Database(rootPath);
        this.configVo = Vo.loadObj(this.database, "/config.mb", ConfigVo.class);
    }

    @Override
    public String toString() {
        return "DatabaseClient{" +
                "rootPath='" + database.getRootPath() + '\'' +
                '}';
    }

    public ConfigVo getConfigVo() {
        return configVo;
    }

    public Database getDatabase() {
        return database;
    }
}
