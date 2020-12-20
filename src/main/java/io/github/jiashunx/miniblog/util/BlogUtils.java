package io.github.jiashunx.miniblog.util;

/**
 * @author jiashunx
 */
public class BlogUtils {

    public static String formatPath(String path) {
        String _path = String.valueOf(path).replace("\\", "/");
        if (!_path.endsWith("/")) {
            _path += "/";
        }
        return _path;
    }

}
