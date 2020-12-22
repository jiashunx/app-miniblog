package io.github.jiashunx.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.miniblog.database.Database;
import io.github.jiashunx.miniblog.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客配置总入口
 * @author jiashunx
 */
public class ConfigVo extends Vo<ConfigVo> {

    private LoginUserVo loginUserVo;
    private String jwtSecretKey;
    private long lastBootTimeMillis;
    private String lastBootTimeStr;
    private List<ImageVo> imageVoList;

    @JsonIgnore
    private MRestJWTHelper jwtHelper;

    private ConfigVo() {
        super();
    }

    private ConfigVo(boolean cacheEnable) {
        super(cacheEnable);
    }

    public static ConfigVo buildDefault0() {
        ConfigVo vo = new ConfigVo();
        vo.setLoginUserVo(new LoginUserVo(Constants.DEFAULT_AUTH_USERNAME, Constants.DEFAULT_AUTH_PASSWORD_BASE64));
        vo.setJwtSecretKey(Constants.DEFAULT_JWT_SECRET_KEY);
        return vo;
    }

    protected ConfigVo getObj() {
        return this;
    }

    protected void init0() {
        this.jwtHelper = new MRestJWTHelper(getJwtSecretKey());
    }

    public ConfigVo buildCacheObj() {
        ConfigVo cache = new ConfigVo(false);
        cache.loginUserVo = this.loginUserVo.clone();
        cache.jwtSecretKey = this.jwtSecretKey;
        cache.imageVoList = new ArrayList<>();
        if (this.imageVoList != null && !this.imageVoList.isEmpty()) {
            for (ImageVo imageVo: this.imageVoList) {
                cache.imageVoList.add(imageVo.clone());
            }
        }
        return cache;
    }

    public void save(Database database) {
        super.save(database, "/config.mb");
    }

    public LoginUserVo getLoginUserVo() {
        return loginUserVo;
    }
    public void setLoginUserVo(LoginUserVo loginUserVo) {
        this.loginUserVo = loginUserVo;
    }
    public String getJwtSecretKey() {
        return jwtSecretKey;
    }
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }
    public MRestJWTHelper getJwtHelper() {
        return jwtHelper;
    }
    public void setJwtHelper(MRestJWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }
    public long getLastBootTimeMillis() {
        return lastBootTimeMillis;
    }
    public void setLastBootTimeMillis(long lastBootTimeMillis) {
        this.lastBootTimeMillis = lastBootTimeMillis;
    }
    public String getLastBootTimeStr() {
        return lastBootTimeStr;
    }
    public void setLastBootTimeStr(String lastBootTimeStr) {
        this.lastBootTimeStr = lastBootTimeStr;
    }
    public List<ImageVo> getImageVoList() {
        return imageVoList;
    }
    public void setImageVoList(List<ImageVo> imageVoList) {
        this.imageVoList = imageVoList;
    }
}
