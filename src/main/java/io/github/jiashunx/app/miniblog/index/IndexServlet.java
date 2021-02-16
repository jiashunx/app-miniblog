package io.github.jiashunx.app.miniblog.index;

import io.github.jiashunx.app.miniblog.service.ServiceBus;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.filter.Filter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilterChain;

/**
 * @author jiashunx
 */
@Filter(urlPatterns = "/*", order = Integer.MAX_VALUE)
public class IndexServlet implements MRestFilter {

    @Override
    public void doFilter(MRestRequest request, MRestResponse response, MRestFilterChain filterChain) {
        String requestUrl = request.getUrl();
        switch (requestUrl) {
            case "/":
                root(request, response);
                return;
            case "/timeline":
                timeline(request, response);
                return;
            case "/categories":
                categories(request, response);
                return;
            case "/tags":
                tags(request, response);
                return;
            default:
                //    /2021/02/23/article-short-id
                if (requestUrl.matches("^/\\d{4}/\\d{1,2}/\\d{1,2}/\\S+$")) {
                    return;
                } else if (requestUrl.matches("^/tag/\\S+$")) {
                    //    /tags/tag-name
                    return;
                } else if (requestUrl.matches("^/categories/\\S+$")) {
                    //    /categories/category-name
                    return;
                } else if (requestUrl.matches("^/page/\\d+$")) {
                    //    /page/page-index
                    return;
                } else {
                    filterChain.doFilter(request, response);
                }

        }
    }

    public IndexServlet(ServiceBus serviceBus) {

    }

    public void root(MRestRequest request, MRestResponse response) {
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
