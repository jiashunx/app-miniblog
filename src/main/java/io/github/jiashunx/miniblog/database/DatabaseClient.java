package io.github.jiashunx.miniblog.database;

import io.github.jiashunx.miniblog.model.Cache;
import io.github.jiashunx.miniblog.model.ConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiashunx
 */
public class DatabaseClient {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseClient.class);

    private final Database database;
    private final ConfigVo configVo;

    public DatabaseClient(String rootPath) {
        this.database = new Database(rootPath);
        this.configVo = Cache.loadObj(this.database, "/config.mb", ConfigVo.class);
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
