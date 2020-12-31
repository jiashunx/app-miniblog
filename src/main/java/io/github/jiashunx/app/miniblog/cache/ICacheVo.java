package io.github.jiashunx.app.miniblog.cache;

/**
 * @author jiashunx
 */
public interface ICacheVo<T> {

    T getCacheObj();
    void setCacheObj(T t);

}
