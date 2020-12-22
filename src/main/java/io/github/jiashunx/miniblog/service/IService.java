package io.github.jiashunx.miniblog.service;

import io.github.jiashunx.miniblog.model.IVo;

/**
 * @author jiashunx
 */
public interface IService<T extends IVo> {
    void save();
    T get();
}
