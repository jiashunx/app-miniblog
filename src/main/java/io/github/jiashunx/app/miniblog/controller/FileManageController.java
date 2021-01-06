package io.github.jiashunx.app.miniblog.controller;

import io.github.jiashunx.app.miniblog.model.FileVo;
import io.github.jiashunx.app.miniblog.service.FileService;

import java.util.List;
import java.util.Objects;

/**
 * @author jiashunx
 */
public class FileManageController {

    private final FileService fileService;

    public FileManageController(FileService fileService) {
        this.fileService = Objects.requireNonNull(fileService);
    }

    public List<FileVo> listAll() {
        return fileService.listAll();
    }

}
