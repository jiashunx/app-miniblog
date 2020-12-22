package io.github.jiashunx.miniblog.file;

import io.github.jiashunx.masker.rest.framework.serialize.MRestSerializer;
import io.github.jiashunx.miniblog.database.Database;
import io.github.jiashunx.miniblog.model.ConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiashunx
 */
public class FileHolder {

    private static final Logger logger = LoggerFactory.getLogger(FileHolder.class);

    private final Database database;
    private final ConfigVo configVo;

    public FileHolder(String rootPath) {
        this.database = new Database(rootPath);
        this.configVo = loadConfigVo();
        if (!this.configVo.check()) {
            throw new RuntimeException("config check failed...");
        }
    }

    public synchronized void storeConfigVo() {
        database.write("/config.mb", MRestSerializer.objectToJson(this.configVo, true));
        this.configVo.cacheUpdate();
    }

    private ConfigVo loadConfigVo() {
        String filePath = "/config.mb";
        ConfigVo configVo = null;
        try {
            String json = database.readString(filePath);
            if (logger.isInfoEnabled()) {
                logger.info("load config:\n{}", json);
            }
            configVo = MRestSerializer.jsonToObj(json, ConfigVo.class).initialize();
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("parse config vo failed, filepath: {}", filePath, throwable);
            }
        }
        if (configVo == null) {
            configVo = ConfigVo.buildDefault();
        }
        return configVo;
    }

    @Override
    public String toString() {
        return "FileHolder{" +
                "rootPath='" + database.getRootPath() + '\'' +
                '}';
    }

    public ConfigVo getConfigVo() {
        return configVo;
    }
}
