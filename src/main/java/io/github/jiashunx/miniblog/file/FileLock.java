package io.github.jiashunx.miniblog.file;

import java.io.File;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

/**
 * @author jiashunx
 */
public class FileLock {

    private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    public static void read(String filePath, Consumer<File> consumer) {
        read(new File(filePath), consumer);
    }

    public static void read(File file, Consumer<File> consumer) {
        read(new File[] { file }, files -> {
            for (File f: files) {
                consumer.accept(f);
            }
        });
    }

    public static void read(String[] filePaths, Consumer<File[]> consumer) {
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            files[i] = new File(filePaths[i]);
        }
        read(files, consumer);
    }

    public static void read(File[] files, Consumer<File[]> consumer) {
        READ_WRITE_LOCK.readLock().lock();
        try {
            consumer.accept(files);
        } finally {
            READ_WRITE_LOCK.readLock().unlock();
        }
    }

    public static void write(String filePath, Consumer<File> consumer) {
        write(new File(filePath), consumer);
    }

    public static void write(File file, Consumer<File> consumer) {
        write(new File[] { file }, files -> {
            for (File f: files) {
                consumer.accept(f);
            }
        });
    }

    public static void write(String[] filePaths, Consumer<File[]> consumer) {
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            files[i] = new File(filePaths[i]);
        }
        write(files, consumer);
    }

    public static void write(File[] files, Consumer<File[]> consumer) {
        READ_WRITE_LOCK.writeLock().lock();
        try {
            consumer.accept(files);
        } finally {
            READ_WRITE_LOCK.writeLock().unlock();
        }
    }

}
