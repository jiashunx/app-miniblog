package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/public/page/*")
public class PageServlet implements MRestServlet {

    public PageServlet(ServiceBus serviceBus) {

    }

    @Override
    public void service(MRestRequest restRequest, MRestResponse restResponse) {
        // TODO 获取url，获取页数，返回相应页数据
    }

}
