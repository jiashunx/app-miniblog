package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;

/**
 * @author jiashunx
 */
public class CategoryServlet implements MRestServlet {

    public CategoryServlet(ServiceBus serviceBus) {

    }

    @Override
    public void service(MRestRequest restRequest, MRestResponse restResponse) {
        // TODO 拦截url，获取分类名称，查找匹配分类，返回指定分类的所有文章列表
    }

}
