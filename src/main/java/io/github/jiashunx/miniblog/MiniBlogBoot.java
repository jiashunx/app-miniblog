package io.github.jiashunx.miniblog;

import io.github.jiashunx.masker.rest.framework.MRestServer;
import io.github.jiashunx.masker.rest.framework.filter.MRestFilter;
import io.github.jiashunx.masker.rest.framework.util.FileUtils;
import io.github.jiashunx.masker.rest.framework.util.MRestJWTHelper;
import io.github.jiashunx.masker.rest.framework.util.MRestUtils;
import io.github.jiashunx.miniblog.console.AuthFilter;
import io.github.jiashunx.miniblog.file.FileHolder;
import io.github.jiashunx.miniblog.file.FileLock;
import io.github.jiashunx.miniblog.model.ConfigVo;
import io.github.jiashunx.miniblog.model.LoginUserVo;
import io.github.jiashunx.miniblog.util.BlogUtils;
import io.github.jiashunx.miniblog.util.Constants;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * @author jiashunx
 */
public class MiniBlogBoot {

    public static void main(String[] args) throws ParseException {
        new MiniBlogBoot(args).start();
    }

    /************************************************ SEP **************************************************/

    private static final Logger logger = LoggerFactory.getLogger(MiniBlogBoot.class);

    private final String contextPath;
    private final int listenPort;
    private final FileHolder fileHolder;
    private final ConfigVo configVo;

    public MiniBlogBoot(String[] args) throws ParseException {
        CommandLineParser commandLineParser = new BasicParser();
        Options options = new Options();
        // java -jar xx.jar --port 8080 --path /root/mypath --auth --auser admin --apwd admin
        options.addOption("context", true, "context path, default: /");
        options.addOption("port", true, "server port, default: 8080");
        options.addOption("path", true, "workspace root path");
        options.addOption("auser", true, "auth user, default: admin");
        options.addOption("apwd", true, "auth password, default: admin");
        CommandLine commandLine = commandLineParser.parse(options, args);
        this.contextPath = commandLine.hasOption("context") ? commandLine.getOptionValue("context") : "/";
        this.listenPort = Integer.parseInt(commandLine.hasOption("port") ? commandLine.getOptionValue("port") : "8080");
        String rootPath = BlogUtils.formatPath(commandLine.hasOption("path") ? commandLine.getOptionValue("path") : MRestUtils.getUserDirPath());
        this.fileHolder = new FileHolder(rootPath);
        String authUsername = commandLine.hasOption("auser") ? commandLine.getOptionValue("auser") : Constants.DEFAULT_AUTH_USERNAME;
        String authPassword = Base64.getEncoder().encodeToString((commandLine.hasOption("apwd") ? commandLine.getOptionValue("apwd") : Constants.DEFAULT_AUTH_PASSWORD).getBytes());
        this.configVo = this.fileHolder.getConfigVo();
        configVo.setLoginUserVo(new LoginUserVo(authUsername, authPassword));
        // 进行配置回写
        this.fileHolder.storeConfigVo();

        if (logger.isInfoEnabled()) {
            logger.info("blog service start: {}", this);
        }
    }

    public void start() {
        new MRestServer("mini-blog")
                .contextPath(getContextPath())
                .listenPort(getListenPort())
                .filter(new MRestFilter[]{ new AuthFilter(this) })
                .start();
    }

    @Override
    public String toString() {
        return "MiniBlogBoot{" +
                "contextPath=" + contextPath +
                ", listenPort=" + listenPort +
                ", fileHolder='" + fileHolder + '\'' +
                '}';
    }

    public int getListenPort() {
        return listenPort;
    }
    public FileHolder getFileHolder() {
        return fileHolder;
    }
    public String getContextPath() {
        return contextPath;
    }
    public ConfigVo getConfigVo() {
        return configVo;
    }
}
