package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;

/**
 * @author jiashunx
 */
public class ServiceBus {

    private final ArgumentService argumentService;

    public ServiceBus(String[] args) throws MiniBlogException {
        this.argumentService = new ArgumentService(args);
    }

    public ArgumentService getArgumentService() {
        return argumentService;
    }
}
