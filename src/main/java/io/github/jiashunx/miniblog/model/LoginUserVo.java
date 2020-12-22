package io.github.jiashunx.miniblog.model;

/**
 * @author jiashunx
 */
public class LoginUserVo implements Cloneable {

    private String username;
    private String password;

    public LoginUserVo() {}

    public LoginUserVo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public LoginUserVo clone() {
        try {
            return (LoginUserVo) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
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
