package io.github.jiashunx.app.miniblog.model;

import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Column;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Id;
import io.github.jiashunx.tools.sqlite3.mapping.SQLite3Table;

/**
 * @author jiashunx
 */
@SQLite3Table(tableName = "MB_USER")
public class LoginUserVo implements Cloneable {

    @SQLite3Id
    @SQLite3Column(columnName = "USERNAME")
    private String username;
    @SQLite3Column(columnName = "PASSWORD")
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
