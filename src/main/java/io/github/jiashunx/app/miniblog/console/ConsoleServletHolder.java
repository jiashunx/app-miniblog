package io.github.jiashunx.app.miniblog.console;

import io.github.jiashunx.app.miniblog.index.IndexServlet;
import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;

import java.util.Objects;

/**
 * @author jiashunx
 */
public class ConsoleServletHolder {

    private final ServiceBus serviceBus;

    public ConsoleServletHolder(ServiceBus serviceBus) {
        this.serviceBus = Objects.requireNonNull(serviceBus);
    }

    public MRestServlet[] getConsoleServletArr() {
        return new MRestServlet[] {
                new FileManageServlet(serviceBus.getFileService())
                , new CategoryManageServlet(serviceBus.getCategoryService(), serviceBus.getArticleCategoryService())
                , new TagManageServlet(serviceBus.getTagService(), serviceBus.getArticleTagService())
                , new ArticleManageServlet(serviceBus)
                , new IndexServlet(serviceBus)
        };
    }

}
