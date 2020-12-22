package io.github.jiashunx.miniblog.cache;

/**
 * @author jiashunx
 */
public interface ICache<T> {

    T getCacheObj();

    T buildCacheObj();

}
