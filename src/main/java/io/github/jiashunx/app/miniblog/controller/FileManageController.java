package io.github.jiashunx.app.miniblog.controller;

import io.github.jiashunx.app.miniblog.model.FileVo;
import io.github.jiashunx.app.miniblog.service.FileService;
import io.github.jiashunx.masker.rest.framework.MRestFileUploadRequest;
import io.github.jiashunx.masker.rest.framework.MRestRequest;
import io.github.jiashunx.masker.rest.framework.MRestResponse;
import io.github.jiashunx.masker.rest.framework.model.MRestFileUpload;
import io.github.jiashunx.masker.rest.framework.util.IOUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author jiashunx
 */
public class FileManageController {

    private static final Logger logger = LoggerFactory.getLogger(FileManageController.class);

    private final FileService fileService;

    public FileManageController(FileService fileService) {
        this.fileService = Objects.requireNonNull(fileService);
    }

    public List<FileVo> listAll() {
        return fileService.listAll();
    }

    public void save(MRestRequest request, MRestResponse response) {
        List<FileVo> fileVoList = new LinkedList<>();
        try {
            MRestFileUploadRequest fileUploadRequest = (MRestFileUploadRequest) request;
            List<MRestFileUpload> fileUploadList = fileUploadRequest.getFileUploadList();
            for (MRestFileUpload fileUpload: fileUploadList) {
                InputStream inputStream = fileUpload.getFileInputStream();
                FileVo fileVo = new FileVo();
                fileVo.setFileId(UUID.randomUUID().toString());
                fileVo.setFileName(fileUpload.getFilename());
                ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());
                IOUtils.copy(inputStream, baos);
                fileVo.setFileBytes(baos.toByteArray());
                fileVo.setFileByteSize(fileVo.getFileBytes().length);
                fileVoList.add(fileVo);
            }
        } catch (Throwable throwable) {
            logger.error("upload file failed.", throwable);
            response.write(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        fileService.insert(fileVoList);
    }

}
