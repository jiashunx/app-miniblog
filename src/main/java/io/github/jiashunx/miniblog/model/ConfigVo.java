package io.github.jiashunx.miniblog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.miniblog.util.Constants;

/**
 * 博客配置总入口
 * @author jiashunx
 */
public class ConfigVo implements ConfigCheck {

    private LoginUserVo loginUserVo;
    private String jwtSecretKey;
    @JsonIgnore
    private MRestJWTHelper jwtHelper;

    private ConfigVo() {}

    public static ConfigVo buildDefault() {
        ConfigVo vo = new ConfigVo();
        vo.setLoginUserVo(new LoginUserVo(Constants.DEFAULT_AUTH_USERNAME, Constants.DEFAULT_AUTH_PASSWORD_BASE64));
        vo.setJwtSecretKey(Constants.DEFAULT_JWT_SECRET_KEY);
        vo.initialize();
        return vo;
    }

    public ConfigVo initialize() {
        this.jwtHelper = new MRestJWTHelper(getJwtSecretKey());
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
}
