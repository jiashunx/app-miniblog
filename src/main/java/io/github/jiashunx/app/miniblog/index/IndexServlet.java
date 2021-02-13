package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/*")
public class IndexServlet extends AbstractRestServlet {

    public IndexServlet(ServiceBus serviceBus) {

    }

    @GetMapping(url = "/")
    public void root(MRestRequest request, MRestResponse response) {
        index(request, response);
    }

    /**
     * 首页数据页面
     */
    @GetMapping(url = "/public/index")
    public void index(MRestRequest request, MRestResponse response) {

    }

    /**
     * 时间轴页面
     */
    @GetMapping(url = "/public/timeline")
    public void timeline(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有分类页面
     */
    @GetMapping(url = "/public/categories")
    public void categories(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有tag页面
     */
    @GetMapping(url = "/public/tags")
    public void tags(MRestRequest request, MRestResponse response) {

    }

}
