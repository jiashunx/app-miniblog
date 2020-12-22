package io.github.jiashunx.miniblog.service;

import io.github.jiashunx.miniblog.cache.ICacheVo;

/**
 * @author jiashunx
 */
public interface ICacheService<T extends ICacheVo<T>> {

    T getCacheObj(T t);
    T buildCacheObj(T t);

}
