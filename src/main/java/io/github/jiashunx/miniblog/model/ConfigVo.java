package io.github.jiashunx.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.miniblog.cache.CacheVo;

import java.util.List;

/**
 * 博客配置总入口
 * @author jiashunx
 */
public class ConfigVo extends CacheVo<ConfigVo> implements IVo {

    private LoginUserVo loginUserVo;
    private String jwtSecretKey;
    private long lastBootTimeMillis;
    private String lastBootTimeStr;

    @JsonIgnore
    private MRestJWTHelper jwtHelper;

    public ConfigVo() {
        super();
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
}
