package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.IVo;

/**
 * @author jiashunx
 */
public interface IService<T extends IVo> {
    void save();
    T get();
}
