package io.github.jiashunx.miniblog.util;

import java.util.Base64;

/**
 * @author jiashunx
 */
public class Constants {
    public static final String DEFAULT_AUTH_USERNAME = "admin";
    public static final String DEFAULT_AUTH_PASSWORD = "admin";
    public static final String DEFAULT_AUTH_PASSWORD_BASE64 = Base64.getEncoder().encodeToString(DEFAULT_AUTH_PASSWORD.getBytes());
    public static final String DEFAULT_JWT_SECRET_KEY = "zxcvmnblkjhgfdsaqwertyupoi";
}
