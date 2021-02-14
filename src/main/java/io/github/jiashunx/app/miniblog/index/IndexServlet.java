package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/*")
public class IndexServlet implements MRestServlet {

    @Override
    public void service(MRestRequest request, MRestResponse response) {
        String requestUrl = request.getUrl();
        switch (requestUrl) {
            case "/":
                root(request, response);
                break;
            case "/public/index":
                index(request, response);
                break;
            case "/public/timeline":
                timeline(request, response);
                break;
            case "/public/categories":
                categories(request, response);
                break;
            case "/public/tags":
                tags(request, response);
                break;
            default:
                break;
        }
    }

    public IndexServlet(ServiceBus serviceBus) {

    }

    public void root(MRestRequest request, MRestResponse response) {
        index(request, response);
    }

    /**
     * 首页数据页面
     */
    public void index(MRestRequest request, MRestResponse response) {

    }

    /**
     * 时间轴页面
     */
    public void timeline(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有分类页面
     */
    public void categories(MRestRequest request, MRestResponse response) {

    }

    /**
     * 所有tag页面
     */
    public void tags(MRestRequest request, MRestResponse response) {

    }

}
