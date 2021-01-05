package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jiashunx
 */
public class ServiceBus implements IService {

    private final ArgumentService argumentService;
    private final DatabaseService databaseService;

    private final List<IService> serviceList = new LinkedList<>();

    public ServiceBus(String[] args) throws MiniBlogException {
        this.argumentService = new ArgumentService(this, args);
        this.databaseService = new DatabaseService();
        serviceList.add(databaseService);
        serviceList.add(argumentService);
    }

    @Override
    public void init() {
        serviceList.forEach(IService::init);
    }

    public ArgumentService getArgumentService() {
        return argumentService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }
}
