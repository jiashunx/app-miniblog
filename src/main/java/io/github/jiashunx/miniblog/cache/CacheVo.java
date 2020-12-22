package io.github.jiashunx.miniblog.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author jiashunx
 */
public abstract class CacheVo<T extends CacheVo<?>> implements ICacheVo<T> {

    @JsonIgnore
    private T cacheObj;

    public CacheVo() {}

    /**
     * 获取缓存对象.
     */
    public T getCacheObj() {
        return cacheObj;
    }

    @Override
    public void setCacheObj(T cacheObj) {
        this.cacheObj = cacheObj;
    }

}
