package io.github.jiashunx.app.miniblog.model.entity;

import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_USER")
public class LoginUserEntity implements Cloneable {

    @SQLite3Id
    @SQLite3Column(columnName = "USERNAME")
    private String username;
    @SQLite3Column(columnName = "PASSWORD")
    private String password;

    private boolean defaultUser;

    public LoginUserEntity() {}

    public LoginUserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public LoginUserEntity clone() {
        try {
            return (LoginUserEntity) super.clone();
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

    public boolean isDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(boolean defaultUser) {
        this.defaultUser = defaultUser;
    }
}
