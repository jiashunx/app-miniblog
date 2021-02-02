package io.github.jiashunx.app.miniblog.console;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.model.entity.TagEntity;
import io.github.jiashunx.app.miniblog.service.TagService;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.servlet.AbstractRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.GetMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.PostMapping;
import io.github.jiashunx.masker.rest.framework.servlet.mapping.RequestMapping;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/tag/*")
@RequestMapping(url = "/console/tag")
public class TagManageServlet extends AbstractRestServlet {

    private static final String TAG_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/tag-index.html");

    private final TagService tagService;

    public TagManageServlet(TagService tagService) {
        this.tagService = Objects.requireNonNull(tagService);
    }

    @PostMapping(url = "/create")
    public void create(MRestRequest request, MRestResponse response) {
        TagEntity entity = request.parseBodyToObj(TagEntity.class);
        entity.setTagId(UUID.randomUUID().toString());
        entity.setTagName(String.valueOf(entity.getTagName()));
        entity.setCreateTime(new Date());
        tagService.insert(entity);
    }

    @PostMapping(url = "/update")
    private void update(MRestRequest request, MRestResponse response) {
        TagEntity entity = request.parseBodyToObj(TagEntity.class);
        TagEntity storedEntity = tagService.find(entity.getTagId());
        // may throw NullPointerException
        storedEntity.setTagName(entity.getTagName());
        tagService.update(storedEntity);
    }

    @PostMapping(url = "/delete")
    private void delete(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        TagEntity entity = request.parseBodyToObj(TagEntity.class);
        tagService.deleteById(entity.getTagId());
    }

    @GetMapping(url = "/index.html")
    private void index(MRestRequest request, MRestResponse response) {
        List<TagEntity> entityList = tagService.listAll();
        List<Map<String, Object>> mapList = new ArrayList<>(entityList.size());
        for (TagEntity entity : entityList) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("tagId", entity.getTagId());
            objectMap.put("tagName", entity.getTagName());
            objectMap.put("createTime", BlogUtils.format(entity.getCreateTime(), BlogUtils.yyyyMMddHHmmssSSS));
            mapList.add(objectMap);
        }
        Kv kv = new Kv();
        kv.put("tagVoList", mapList);
        response.write(BlogUtils.render(TAG_MANAGE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

}
