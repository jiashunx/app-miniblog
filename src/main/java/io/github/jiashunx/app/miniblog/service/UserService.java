package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.LoginUserVo;
import io.github.jiashunx.app.miniblog.util.Constants;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author jiashunx
 */
public class UserService implements IService {

    private final ServiceBus serviceBus;

    private volatile LoginUserVo loginUserVo;

    UserService(ServiceBus serviceBus) {
        this.serviceBus = Objects.requireNonNull(serviceBus);
    }

    @Override
    public synchronized void init() {
        ArgumentService argumentService = serviceBus.getArgumentService();
        DatabaseService databaseService = serviceBus.getDatabaseService();
        SQLite3JdbcTemplate jdbcTemplate = databaseService.getJdbcTemplate();
        List<LoginUserVo> userVoList = jdbcTemplate.queryForList(
                databaseService.getDQL(Constants.DQL_QUERY_ALL_USER), LoginUserVo.class);
        LoginUserVo loginUserVo = null;
        if (userVoList != null && !userVoList.isEmpty()) {
            loginUserVo = userVoList.get(0);
            jdbcTemplate.executeUpdate(databaseService.getDML(Constants.DML_DELETE_ALL_USER));
        }
        if (loginUserVo == null || !argumentService.getLoginUserVo().isDefaultUser()) {
            loginUserVo = argumentService.getLoginUserVo();
        }
        jdbcTemplate.insert(loginUserVo);
        this.loginUserVo = loginUserVo;
    }

    public boolean checkValidation(String username, String base64Password) {
        return loginUserVo.getUsername().equals(username) && loginUserVo.getPassword().equals(base64Password);
    }

}
