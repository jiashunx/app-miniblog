package io.github.jiashunx.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jiashunx.miniblog.cache.ICache;
import io.github.jiashunx.miniblog.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiashunx
 */
public abstract class Vo<T extends Vo<?>> implements ICache<T> {

    private static final Logger logger = LoggerFactory.getLogger(Vo.class);

    @JsonIgnore
    private boolean cacheEnable;

    @JsonIgnore
    private T cacheObj;

    public Vo() {
        this(true);
    }

    public Vo(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

    public static <T extends Vo<?>> T buildDefault(Class<T> klass) {
        try {
            Method method = klass.getMethod("buildDefault0");
            return (T) method.invoke(null);
        } catch (Throwable throwable) {
            logger.error("create default object failed, {}", klass, throwable);
        }
        return null;
    }

    public static <T extends Vo<?>> T loadObj(Database database, String filePath, Class<T> klass) {
        T obj = database.readObject(filePath, klass);
        if (obj == null) {
            obj = buildDefault(klass);
        }
        if (obj != null) {
            obj.init();
        }
        return obj;
    }

    public static <T extends Vo<?>> List<T> loadList(Database database, String filePath, Class<T> klass) {
        List<T> list = database.readList(filePath, klass);
        if (list == null) {
            list = new ArrayList<>();
        }
        for (T obj: list) {
            obj.init();
        }
        return list;
    }

    public void save(Database database, String filePath) {
        if (database.write(filePath, getObj())) {
            update();
        }
    }

    /**
     * 获取缓存对象.
     */
    public T getCacheObj() {
        return cacheObj;
    }

    /**
     * 获取模型.
     */
    protected abstract T getObj();

    /**
     * 子类实现 - 对象根据已初始化的属性进行复杂属性初始化.
     */
    protected abstract void init0();

    /**
     * 对象创建完成后的初始化
     */
    public void init() {
        init0();
        update();
    }

    /**
     * 对象缓存更新.
     */
    public void update() {
        if (isCacheEnable()) {
            this.cacheObj = buildCacheObj();
            this.cacheObj.setCacheEnable(false);
            this.cacheObj.init();
        }
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }
}
