package io.github.jiashunx.app.miniblog;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.app.miniblog.service.ArgumentService;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestServer;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.app.miniblog.console.AuthFilter;

/**
 * @author jiashunx
 */
public class MiniBlogBoot {

    public static void main(String[] args) throws MiniBlogException {
        new MiniBlogBoot(args).start();
    }

    private final ServiceBus serviceBus;

    public MiniBlogBoot(String[] args) throws MiniBlogException {
        this.serviceBus = new ServiceBus(args);
        this.serviceBus.init();
    }

    public void start() {
        ArgumentService argumentService = serviceBus.getArgumentService();
        new MRestServer("mini-blog")
                .listenPort(argumentService.getListenPort())
                .context(argumentService.getContextPath())
                .addDefaultClasspathResource()
                .filter(new MRestFilter[]{ new AuthFilter(serviceBus) })
                .getRestServer()
                .start();
    }

}
