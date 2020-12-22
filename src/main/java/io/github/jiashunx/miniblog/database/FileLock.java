package io.github.jiashunx.miniblog.database;

import io.github.jiashunx.masker.rest.framework.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 文件锁(使用读写锁实现).
 * @author jiashunx
 */
class FileLock {

    private static final Logger logger = LoggerFactory.getLogger(FileLock.class);

    private final String rootPath;

    public FileLock(String rootPath) {
        this.rootPath = formatPath(rootPath);
        logger.info("FileLock init, rootPath: {}", this.rootPath);
        initFileLock(new File(this.rootPath));
        logger.info("FileLock instance init success.");
    }

    public void newDirectory(String filePath) {
        write(filePath, (File f) -> {
            FileUtils.newDirectory(f.getAbsolutePath());
        });
    }

    public void newFile(String filePath) {
        write(filePath, (File f) -> {
            FileUtils.newFile(f.getAbsolutePath());
        });
    }

    public void delete(String filePath) {
        delete(filePath, f -> {
            FileUtils.deleteFile(new File(f.getAbsolutePath()));
        });
    }

    public void write(String filePath, Consumer<File> consumer) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = getReadWriteLock(path, true);
        lock.writeLock().lock();
        try {
            consumer.accept(new File(path));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public <R> R write(String filePath, Function<File, R> function) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = getReadWriteLock(path, true);
        lock.writeLock().lock();
        try {
            return function.apply(new File(path));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void read(String filePath, Consumer<File> consumer) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = getReadWriteLock(path);
        if (lock == null) {
            throw new IllegalArgumentException(String.format("file lock not found, filepath: %s", filePath));
        }
        lock.readLock().lock();
        try {
            consumer.accept(new File(path));
        } finally {
            lock.readLock().unlock();
        }
    }

    public <R> R read(String filePath, Function<File, R> function) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = getReadWriteLock(path);
        if (lock == null) {
            throw new IllegalArgumentException(String.format("don't found file lock, filepath: %s", filePath));
        }
        lock.readLock().lock();
        try {
            return function.apply(new File(path));
        } finally {
            lock.readLock().unlock();
        }
    }

    public FileLock delete(String filePath, Consumer<File> consumer) {
        delete(filePath, file -> {
            consumer.accept(file);
            return new Object();
        });
        return this;
    }

    public <R> R delete(String filePath, Function<File, R> function) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = getReadWriteLock(path, true);
        if (lock == null) {
            throw new IllegalArgumentException(String.format("don't found file lock, filepath: %s", filePath));
        }
        R retObj = null;
        lock.writeLock().lock();
        try {
            File file = new File(path);
            retObj = function.apply(file);
            removeReadWriteLock(path);
        } finally {
            lock.writeLock().unlock();
        }
        return retObj;
    }

    private void initFileLock(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("file not exists !");
        }
        String filePath = formatPath(file.getAbsolutePath());
        if (file.isFile()) {
            getReadWriteLock(filePath, true);
        } else if (file.isDirectory()) {
            getReadWriteLock(filePath, true);
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    initFileLock(f);
                }
            }
        }
    }


    /************************************************************************************/


    private final Map<String, ReentrantReadWriteLock> LOCK_MAP = new HashMap<>();
    private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

    public ReentrantReadWriteLock getReadWriteLock(String filePath) {
        return getReadWriteLock(filePath, false);
    }

    public ReentrantReadWriteLock getReadWriteLock(String filePath, boolean autoCreate) {
        String path = getFilePath(filePath);
        ReentrantReadWriteLock lock = null;
        LOCK.readLock().lock();
        try {
            lock = LOCK_MAP.get(path);
        } finally {
            LOCK.readLock().unlock();
        }
        if (lock != null) {
            return lock;
        }
        if (autoCreate) {
            LOCK.writeLock().lock();
            try {
                lock = LOCK_MAP.get(path);
                if (lock != null) {
                    return lock;
                }
                final ReentrantReadWriteLock $lock= new ReentrantReadWriteLock();
                LOCK_MAP.put(path, $lock);
                return $lock;
            } finally {
                LOCK.writeLock().unlock();
            }
        }
        return null;
    }

    public void removeReadWriteLock(String filePath) {
        String path = getFilePath(filePath);
        LOCK.writeLock().lock();
        try {
            LOCK_MAP.remove(path);
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    private String getFilePath(String filePath) {
        String path = formatPath(filePath);
        if (!path.startsWith(rootPath)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            path = rootPath + path;
        }
        return path;
    }

    private static String formatPath(String filePath) {
        String path = String.valueOf(filePath).replace("\\", "/");
        while (path.endsWith("/") && path.length() > 1) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

}
