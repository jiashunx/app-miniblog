package io.github.jiashunx.miniblog.model;

import io.github.jiashunx.masker.rest.framework.util.StringUtils;

/**
 * @author jiashunx
 */
public class LoginUserVo implements ConfigCheck {

    private String username;
    private String password;

    public LoginUserVo() {}

    public LoginUserVo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean check0() {
        return StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password);
    }

    @Override
    public String toString() {
        return "LoginUserVo{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
