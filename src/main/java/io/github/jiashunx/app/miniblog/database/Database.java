package io.github.jiashunx.app.miniblog.database;

import io.github.jiashunx.masker.rest.framework.serialize.MRestSerializer;
import io.github.jiashunx.masker.rest.framework.util.FileUtils;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author jiashunx
 */
public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private final String rootPath;
    private final File rootDirectory;
    private final FileLock fileLock;

    public Database(String rootPath) {
        this.rootPath = formatPath(rootPath);
        logger.info("Database init, rootPath: {}", this.rootPath);
        FileUtils.newDirectory(this.rootPath);
        this.rootDirectory = new File(this.rootPath);
        this.fileLock = new FileLock(this.rootPath);
        logger.info("Database instance init success.");
    }

    public boolean newDirectory(String filePath) {
        try {
            fileLock.newDirectory(filePath);
            return true;
        } catch (Throwable throwable) {
            logger.error("create directory failed: {}", filePath, throwable);
        }
        return false;
    }

    public boolean newFile(String filePath) {
        try {
            fileLock.newFile(filePath);
            return true;
        } catch (Throwable throwable) {
            logger.error("create file failed: {}", filePath, throwable);
        }
        return false;
    }

    public boolean delete(String filePath) {
        try {
            fileLock.delete(filePath);
            return true;
        } catch (Throwable throwable) {
            logger.error("delete file failed: {}", filePath, throwable);
        }
        return false;
    }

    public boolean write(String filePath, String content) {
        return write(filePath, content.getBytes(StandardCharsets.UTF_8));
    }

    public boolean write(String filePath, Object object) {
        return write(filePath, MRestSerializer.jsonSerialize(object));
    }

    public boolean write(String filePath, byte[] bytes) {
        return write(filePath, new ByteArrayInputStream(bytes));
    }

    public boolean write(String filePath, InputStream inputStream) {
        try {
            fileLock.write(filePath, (File target) -> {
                FileUtils.newFile(target.getAbsolutePath());
                try {
                    IOUtils.write(inputStream, target);
                } catch (Throwable throwable) {
                    if (logger.isErrorEnabled()) {
                        logger.error("persistent file: {} failed", filePath, throwable);
                    }
                }
            });
            return true;
        } catch (Throwable throwable) {
            logger.error("write content to file failed: {}", filePath, throwable);
        }
        return false;
    }

    public byte[] read(String filePath) {
        try {
            return fileLock.read(filePath, file -> {
                return IOUtils.loadBytesFromDisk(file.getAbsolutePath());
            });
        } catch (Throwable throwable) {
            logger.error("read file content failed: {}", filePath, throwable);
        }
        return new byte[0];
    }

    public <T> T readObject(String filePath, Class<T> klass) {
        try {
            return MRestSerializer.jsonDeserialize(klass, read(filePath));
        } catch (Throwable throwable) {
            logger.error("read file content to object failed: {}", filePath, throwable);
        }
        return null;
    }

    public <T> List<T> readList(String filePath, Class<T> klass) {
        try {
            return MRestSerializer.jsonToList(readString(filePath), klass);
        } catch (Throwable throwable) {
            logger.error("read file content to object list failed: {}", filePath, throwable);
        }
        return null;
    }

    public String readString(String filePath) {
        try {
            return new String(read(filePath));
        } catch (Throwable throwable) {
            logger.error("read file content to string failed: {}", filePath, throwable);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Database{" +
                "rootPath='" + rootPath + '\'' +
                '}';
    }

    public String getRootPath() {
        return rootPath;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    private static String formatPath(String filePath) {
        String path = String.valueOf(filePath).replace("\\", "/");
        while (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

}
