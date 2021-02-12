package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/public/tags/*")
public class TagServlet implements MRestServlet {

    public TagServlet(ServiceBus serviceBus) {

    }

    @Override
    public void service(MRestRequest restRequest, MRestResponse restResponse) {
        // TODO 拦截url，获取tag名称，查找匹配tag，返回指定tag的所有文章列表
    }

}
