package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.model.entity.LoginUserEntity;
import io.github.jiashunx.app.miniblog.util.Constants;
import io.github.jiashunx.tools.sqlite3.SQLite3JdbcTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @author jiashunx
 */
public class UserService implements IService {

    private final ServiceBus serviceBus;

    private volatile LoginUserEntity loginUserEntity;

    UserService(ServiceBus serviceBus) {
        this.serviceBus = Objects.requireNonNull(serviceBus);
    }

    @Override
    public synchronized void init() {
        ArgumentService argumentService = serviceBus.getArgumentService();
        DatabaseService databaseService = serviceBus.getDatabaseService();
        SQLite3JdbcTemplate jdbcTemplate = databaseService.getJdbcTemplate();
        List<LoginUserEntity> userVoList = jdbcTemplate.queryForList(
                databaseService.getDQL(Constants.DQL_QUERY_ALL_USER), LoginUserEntity.class);
        LoginUserEntity loginUserEntity = null;
        if (userVoList != null && !userVoList.isEmpty()) {
            loginUserEntity = userVoList.get(0);
            jdbcTemplate.executeUpdate(databaseService.getDML(Constants.DML_DELETE_ALL_USER));
        }
        if (loginUserEntity == null || !argumentService.getLoginUserEntity().isDefaultUser()) {
            loginUserEntity = argumentService.getLoginUserEntity();
        }
        jdbcTemplate.insert(loginUserEntity);
        this.loginUserEntity = loginUserEntity;
    }

    public boolean checkValidation(String username, String base64Password) {
        return loginUserEntity.getUsername().equals(username) && loginUserEntity.getPassword().equals(base64Password);
    }

}
