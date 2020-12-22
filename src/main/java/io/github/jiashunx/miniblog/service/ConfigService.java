package io.github.jiashunx.miniblog.service;

import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.miniblog.database.Database;
import io.github.jiashunx.miniblog.model.*;
import io.github.jiashunx.miniblog.util.Constants;

import java.util.ArrayList;
import java.util.List;

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
        List<ImageVo> imageVoList = new ArrayList<>();
        List<ImageVo> $imageVoList = configVo.getImageVoList();
        if ($imageVoList != null && !$imageVoList.isEmpty()) {
            for (ImageVo imageVo: $imageVoList) {
                imageVoList.add(imageVo.clone());
            }
        }
        cache.setImageVoList(imageVoList);
        List<TagVo> tagVoList = new ArrayList<>();
        List<TagVo> $tagVoList = configVo.getTagVoList();
        if ($tagVoList != null && !$tagVoList.isEmpty()) {
            for (TagVo tagVo: $tagVoList) {
                tagVoList.add(tagVo.clone());
            }
        }
        cache.setTagVoList(tagVoList);
        List<CategoryVo> categoryVoList = new ArrayList<>();
        List<CategoryVo> $categoryVoList = configVo.getCategoryVoList();
        if ($categoryVoList != null && !$categoryVoList.isEmpty()) {
            for (CategoryVo categoryVo: $categoryVoList) {
                $categoryVoList.add(categoryVo.clone());
            }
        }
        cache.setCategoryVoList(categoryVoList);
        return cache;
    }

    private ConfigVo initialize(ConfigVo vo) {
        vo.setJwtHelper(new MRestJWTHelper(vo.getJwtSecretKey()));
        if (vo.getImageVoList() == null) {
            vo.setImageVoList(new ArrayList<>());
        }
        if (vo.getTagVoList() == null) {
            vo.setTagVoList(new ArrayList<>());
        }
        if (vo.getCategoryVoList() == null) {
            vo.setCategoryVoList(new ArrayList<>());
        }
        return vo;
    }

}
