package io.github.jiashunx.app.miniblog.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private static final ThreadLocal<Map<String, SimpleDateFormat>> FORMAT_MAP = new ThreadLocal<>();

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        Map<String, SimpleDateFormat> formatMap = FORMAT_MAP.get();
        if (formatMap == null) {
            formatMap = new HashMap<>();
        }
        SimpleDateFormat sdf = formatMap.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            formatMap.put(pattern, sdf);
        }
        FORMAT_MAP.set(formatMap);
        return sdf;
    }

    public static final String yyyyMMdd = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String format(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    public static String format(long date, String pattern) {
        return format(new Date(date), pattern);
    }

    public static Date parse(String timeStr, String pattern) {
        try {
            return getSimpleDateFormat(pattern).parse(timeStr);
        } catch (Throwable throwable) {}
        return null;
    }

}
