package io.github.jiashunx.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.miniblog.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客配置总入口
 * @author jiashunx
 */
public class ConfigVo implements ConfigCheck {

    @JsonIgnore
    private boolean cloned;
    private LoginUserVo loginUserVo;
    private String jwtSecretKey;
    @JsonIgnore
    private MRestJWTHelper jwtHelper;
    @JsonIgnore
    private ConfigVo cachedConfigVo;
    private long lastBootTimeMillis;
    private String lastBootTimeStr;
    private List<ImageVo> imageVoList;

    private ConfigVo() {
        this(false);
    }
    private ConfigVo(boolean cloned) {
        this.cloned = cloned;
    }
    private ConfigVo(boolean cloned, ConfigVo vo) {
        this(cloned);
        this.loginUserVo = vo.loginUserVo.clone();
        this.jwtSecretKey = vo.jwtSecretKey;
        this.imageVoList = new ArrayList<>();
        if (vo.imageVoList != null && !vo.imageVoList.isEmpty()) {
            for (ImageVo imageVo: vo.imageVoList) {
                this.imageVoList.add(imageVo.clone());
            }
        }
        this.initialize();
    }

    public static ConfigVo buildDefault() {
        ConfigVo vo = new ConfigVo(false);
        vo.setLoginUserVo(new LoginUserVo(Constants.DEFAULT_AUTH_USERNAME, Constants.DEFAULT_AUTH_PASSWORD_BASE64));
        vo.setJwtSecretKey(Constants.DEFAULT_JWT_SECRET_KEY);
        vo.setImageVoList(new ArrayList<>());
        vo.initialize();
        return vo;
    }

    public ConfigVo initialize() {
        this.jwtHelper = new MRestJWTHelper(getJwtSecretKey());
        return cacheUpdate();
    }

    public ConfigVo getCachedConfigVo() {
        return this.cachedConfigVo;
    }

    public ConfigVo cacheUpdate() {
        if (!this.cloned) {
            this.cachedConfigVo = new ConfigVo(true, this);
        }
        return this;
    }

    @Override
    public boolean check0() {
        return loginUserVo != null && loginUserVo.check();
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
