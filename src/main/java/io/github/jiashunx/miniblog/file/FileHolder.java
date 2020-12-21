package io.github.jiashunx.miniblog.file;

import io.github.jiashunx.masker.rest.framework.serialize.MRestSerializer;
import io.github.jiashunx.masker.rest.framework.util.FileUtils;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.miniblog.model.ConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author jiashunx
 */
public class FileHolder {

    private static final Logger logger = LoggerFactory.getLogger(FileHolder.class);

    private final String rootPath;
    private final File rootDirectory;
    private final String configPath;
    private final ConfigVo configVo;

    public FileHolder(String rootPath) {
        String _rootPath = Objects.requireNonNull(rootPath);
        while (_rootPath.endsWith("/") && _rootPath.length() > 1) {
            _rootPath = rootPath.substring(0, _rootPath.length() - 1);
        }
        this.rootPath = _rootPath;
        this.rootDirectory = new File(this.rootPath);
        FileLock.write(this.rootPath, f -> {
            FileUtils.newDirectory(f.getAbsolutePath());
        });
        this.configPath = this.rootPath + "/config.mb";
        this.configVo = parseConfigVo();
        if (!this.configVo.check()) {
            throw new RuntimeException("config check failed...");
        }
    }

    public synchronized void storeConfigVo() {
        try {
            FileLock.write(this.configPath, file -> {
                // 配置持久化后更新内存缓存
                write(this.configVo, file);
                this.configVo.cacheUpdate();
            });
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("store config failed, configpath: {}", configPath, throwable);
            }
        }
    }

    private static void write(byte[] bytes, File file) {
        String filePath = file.getAbsolutePath();
        FileUtils.newFile(filePath);
        try (OutputStream outputStream = new FileOutputStream(file);
             InputStream inputStream = new ByteArrayInputStream(bytes);) {
            IOUtils.copy(inputStream, outputStream);
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("write content to file failed, filepath: {}", filePath, throwable);
            }
        }
    }
    private static void write(String string, File file) {
        write(string.getBytes(StandardCharsets.UTF_8), file);
    }
    private static void write(Object object, File file) {
        write(MRestSerializer.objectToJsonBytes(object), file);
    }

    private ConfigVo parseConfigVo() {
        FileLock.write(configPath, f -> {
            FileUtils.newFile(configPath);
        });
        ConfigVo configVo = null;
        try {
            String json = IOUtils.loadFileContentFromDisk(configPath, StandardCharsets.UTF_8);
            configVo = MRestSerializer.jsonDeserialize(ConfigVo.class, json.getBytes(StandardCharsets.UTF_8));
            configVo = configVo.initialize();
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("parse config vo failed, filepath: {}, error message: {}", configPath, throwable.getMessage());
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
                "rootPath='" + rootPath + '\'' +
                '}';
    }

    public String getRootPath() {
        return rootPath;
    }
    public File getRootDirectory() {
        return rootDirectory;
    }
    public String getConfigPath() {
        return configPath;
    }
    public ConfigVo getConfigVo() {
        return configVo;
    }
}
