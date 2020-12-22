package io.github.jiashunx.miniblog.cache;

/**
 * @author jiashunx
 */
public interface ICacheVo<T> {

    T getCacheObj();
    void setCacheObj(T t);

}
