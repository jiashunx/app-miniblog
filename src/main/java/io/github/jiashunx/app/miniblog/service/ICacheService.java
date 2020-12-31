package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.cache.ICacheVo;

/**
 * @author jiashunx
 */
public interface ICacheService<T extends ICacheVo<T>> {

    T getCacheObj(T t);
    T buildCacheObj(T t);

}
