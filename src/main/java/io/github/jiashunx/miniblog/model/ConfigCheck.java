package io.github.jiashunx.miniblog.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiashunx
 */
public interface ConfigCheck {

    static final Logger logger = LoggerFactory.getLogger(ConfigCheck.class);

    boolean check0();

    default boolean check() {
        try {
            return check0();
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("{} object check failed.", getClass(), throwable);
            }
        }
        return false;
    }

}
