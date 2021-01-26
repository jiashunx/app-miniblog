package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jiashunx
 */
public class ServiceBus implements IService {

    private final ArgumentService argumentService;
    private final DatabaseService databaseService;
    private final UserService userService;
    private final FileService fileService;
    private final CategoryService categoryService;
    private final TagService tagService;

    private final List<IService> serviceList = new LinkedList<>();

    public ServiceBus(String[] args) throws MiniBlogException {
        this.argumentService = new ArgumentService(args);
        this.databaseService = new DatabaseService();
        this.userService = new UserService(this);
        this.fileService = new FileService(this);
        this.categoryService = new CategoryService(this);
        this.tagService = new TagService(this);
        serviceList.add(databaseService);
        serviceList.add(argumentService);
        serviceList.add(userService);
        serviceList.add(fileService);
        serviceList.add(categoryService);
        serviceList.add(tagService);
    }

    @Override
    public void init() {
        serviceList.forEach(IService::init);
    }

    public ArgumentService getArgumentService() {
        return argumentService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public UserService getUserService() {
        return userService;
    }

    public FileService getFileService() {
        return fileService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public TagService getTagService() {
        return tagService;
    }
}
