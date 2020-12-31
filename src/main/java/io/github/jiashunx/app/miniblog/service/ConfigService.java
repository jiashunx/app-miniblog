package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.ConfigVo;
import io.github.jiashunx.app.miniblog.model.LoginUserVo;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.app.miniblog.database.Database;
import io.github.jiashunx.app.miniblog.util.Constants;

/**
 * @author jiashunx
 */
public class ConfigService implements IService<ConfigVo>, ICacheService<ConfigVo> {

    private final Database database;

    private volatile ConfigVo instance;

    public ConfigService(Database database) {
        this.database = database;
    }

    @Override
    public ConfigVo get() {
        if (instance != null) {
            return instance;
        }
        synchronized (this) {
            if (instance == null) {
                ConfigVo vo = database.readObject("/config.mb", ConfigVo.class);
                if (vo == null) {
                    vo = new ConfigVo();
                    vo.setLoginUserVo(new LoginUserVo(Constants.DEFAULT_AUTH_USERNAME, Constants.DEFAULT_AUTH_PASSWORD_BASE64));
                    vo.setJwtSecretKey(Constants.DEFAULT_JWT_SECRET_KEY);
                }
                initialize(vo);
                vo.setCacheObj(initialize(buildCacheObj(vo)));
                instance = vo;
            }
        }
        return instance;
    }

    @Override
    public void save() {
        ConfigVo configVo = get();
        if (database.write("/config.mb", configVo)) {
            configVo.setCacheObj(initialize(buildCacheObj(configVo)));
        }
    }

    @Override
    public ConfigVo getCacheObj(ConfigVo configVo) {
        return configVo.getCacheObj();
    }

    @Override
    public ConfigVo buildCacheObj(ConfigVo configVo) {
        ConfigVo cache = new ConfigVo();
        cache.setLoginUserVo(configVo.getLoginUserVo().clone());
        cache.setJwtSecretKey(configVo.getJwtSecretKey());
        cache.setLastBootTimeMillis(configVo.getLastBootTimeMillis());
        cache.setLastBootTimeStr(configVo.getLastBootTimeStr());
        return cache;
    }

    private ConfigVo initialize(ConfigVo vo) {
        vo.setJwtHelper(new MRestJWTHelper(vo.getJwtSecretKey()));
        return vo;
    }

}
