package io.github.jiashunx.app.miniblog;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.app.miniblog.servlet.CategoryManageServlet;
import io.github.jiashunx.app.miniblog.servlet.FileManageServlet;
import io.github.jiashunx.app.miniblog.servlet.TagManageServlet;
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
    }

    public void start() {
        serviceBus.init();
        new MRestServer("mini-blog")
                .listenPort(serviceBus.getArgumentService().getListenPort())
                .context(serviceBus.getArgumentService().getContextPath())
                .addDefaultClasspathResource()
                .filter(new MRestFilter[]{ new AuthFilter(serviceBus) })
                .servlet(new FileManageServlet(serviceBus.getFileService()))
                .servlet(new CategoryManageServlet(serviceBus.getCategoryService()))
                .servlet(new TagManageServlet(serviceBus.getTagService()))
                .getRestServer()
                .start();
    }

}
