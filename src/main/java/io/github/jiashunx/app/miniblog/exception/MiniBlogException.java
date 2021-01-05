package io.github.jiashunx.app.miniblog.exception;

/**
 * @author jiashunx
 */
public class MiniBlogException extends RuntimeException {

    public MiniBlogException() {
        super();
    }
    public MiniBlogException(String message) {
        super(message);
    }
    public MiniBlogException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public MiniBlogException(Throwable throwable) {
        super(throwable);
    }

}
