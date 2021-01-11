package io.github.jiashunx.app.miniblog.servlet;

import com.jfinal.kit.Kv;
import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.app.miniblog.model.entity.FileEntity;
import io.github.jiashunx.app.miniblog.service.FileService;
import io.github.jiashunx.app.miniblog.util.BlogUtils;
import io.github.jiashunx.masker.rest.framework.MRestFileUploadRequest;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.cons.Constants;
import io.github.jiashunx.masker.rest.framework.model.MRestFileUpload;
import io.github.jiashunx.masker.rest.framework.servlet.MRestServlet;
import io.github.jiashunx.masker.rest.framework.servlet.Servlet;
import io.github.jiashunx.masker.rest.framework.util.FileUtils;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestHeaderBuilder;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author jiashunx
 */
@Servlet(urlPattern = "/console/file/*")
public class FileManageServlet implements MRestServlet {

    private static final Logger logger = LoggerFactory.getLogger(FileManageServlet.class);

    private static final String FILE_MANAGE_HTML = IOUtils.loadContentFromClasspath("template/console/file-index.html");

    public static final String FILE_OVERVIEW_URL_PREFIX = "/console/file/overview/";
    public static final String FILE_DOWNLOAD_URL_PREFIX = "/console/file/download/";

    private final FileService fileService;

    public FileManageServlet(FileService fileService) {
        this.fileService = Objects.requireNonNull(fileService);
    }

    @Override
    public void service(MRestRequest request, MRestResponse response) {
        String requestURL = request.getUrl();
        if (requestURL.startsWith(FILE_OVERVIEW_URL_PREFIX)) {
            overview(request, response);
            return;
        }
        if (requestURL.startsWith(FILE_DOWNLOAD_URL_PREFIX)) {
            download(request, response);
            return;
        }
        switch (requestURL) {
            case "/console/file/index.html":
                index(request, response);
                break;
            case "/console/file/save":
                save(request, response);
                break;
            case "/console/file/delete":
                delete(request, response);
                break;
            default:
                break;
        }
    }

    private void overview(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        String fileId = request.getUrl().substring(FILE_OVERVIEW_URL_PREFIX.length());
        FileEntity fileEntity = fileService.find(fileId);
        if (fileEntity == null) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        response.write(HttpResponseStatus.OK, fileEntity.getFileBytes());
    }

    private void download(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        String fileId = request.getUrl().substring(FILE_DOWNLOAD_URL_PREFIX.length());
        FileEntity fileEntity = fileService.find(fileId);
        if (fileEntity == null) {
            response.writeStatusPageAsHtml(HttpResponseStatus.NOT_FOUND);
            return;
        }
        String tempFilePath = BlogUtils.getTempPath()
                + "/" + System.currentTimeMillis() + "_" + fileEntity.getFileName();
        FileUtils.newFile(tempFilePath);
        File tempFile = new File(tempFilePath);
        try {
            IOUtils.copy(new ByteArrayInputStream(fileEntity.getFileBytes()), tempFile);
        } catch (IOException exception) {
            throw new MiniBlogException(String.format("download file[id=%s, name=%s] failed."
                    , fileEntity.getFileId(), fileEntity.getFileName()), exception);
        }
        response.write(tempFile, f -> {
            FileUtils.deleteFile(tempFile);
        });
    }

    private void index(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.GET) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        List<FileEntity> fileEntityList = fileService.listAll();
        List<Map<String, Object>> mapList = new ArrayList<>(fileEntityList.size());
        for (FileEntity fileEntity : fileEntityList) {
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("fileId", fileEntity.getFileId());
            objectMap.put("fileName", fileEntity.getFileName());
            objectMap.put("createTime", fileEntity.getCreateTime() == null ? "undefined" : BlogUtils.format(fileEntity.getCreateTime(), BlogUtils.yyyyMMddHHmmssSSS));
            objectMap.put("fileByteSize", fileEntity.getFileByteSize());
            mapList.add(objectMap);
        }
        Kv kv = new Kv();
        kv.put("fileVoList", mapList);
        response.write(BlogUtils.render(FILE_MANAGE_HTML, kv)
                , MRestHeaderBuilder.Build(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE_TEXT_HTML));
    }

    private void save(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        List<FileEntity> fileEntityList = new LinkedList<>();
        try {
            MRestFileUploadRequest fileUploadRequest = (MRestFileUploadRequest) request;
            List<MRestFileUpload> fileUploadList = fileUploadRequest.getFileUploadList();
            for (MRestFileUpload fileUpload: fileUploadList) {
                InputStream inputStream = fileUpload.getFileInputStream();
                FileEntity fileEntity = new FileEntity();
                fileEntity.setFileId(UUID.randomUUID().toString());
                fileEntity.setFileName(fileUpload.getFilename());
                ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());
                IOUtils.copy(inputStream, baos);
                fileEntity.setFileBytes(baos.toByteArray());
                fileEntity.setFileByteSize(fileEntity.getFileBytes().length);
                fileEntity.setCreateTime(new Date());
                fileEntityList.add(fileEntity);
            }
        } catch (Throwable throwable) {
            if (logger.isErrorEnabled()) {
                logger.error("upload file failed.", throwable);
            }
            response.writeStatusPageAsHtml(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        fileService.insert(fileEntityList);
        response.write(HttpResponseStatus.OK);
    }

    private void delete(MRestRequest request, MRestResponse response) {
        if (request.getMethod() != HttpMethod.POST) {
            response.write(HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        fileService.deleteById(request.getParameter("fileId"));
        response.write(HttpResponseStatus.OK);
    }

}
