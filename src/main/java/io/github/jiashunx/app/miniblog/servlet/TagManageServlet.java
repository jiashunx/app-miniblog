package io.github.jiashunx.app.miniblog.servlet;

import io.github.jiashunx.app.miniblog.service.TagService;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;

import java.util.Objects;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/tag/*")
public class TagManageServlet implements MRestServlet {

    private final TagService tagService;

    public TagManageServlet(TagService tagService) {
        this.tagService = Objects.requireNonNull(tagService);
    }

    @Override
    public void service(MRestRequest restRequest, MRestResponse restResponse) {

    }
}
