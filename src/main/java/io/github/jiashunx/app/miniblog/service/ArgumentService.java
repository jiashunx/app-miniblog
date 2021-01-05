package io.github.jiashunx.app.miniblog.service;

import io.github.jiashunx.app.miniblog.exception.MiniBlogException;
import io.github.jiashunx.app.miniblog.model.LoginUserVo;
import io.github.jiashunx.masker.rest.framework.util.MRestUtils;
import org.apache.commons.cli.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author jiashunx
 */
public class ArgumentService {

    private static final String DEFAULT_CONTEXT_PATH = "/";
    private static final int DEFAULT_LISTEN_PORT = 8080;
    private static final String DEFAULT_AUTH_USER = "admin";
    private static final String DEFAULT_AUTH_PWD ="admin";
    private static final String DEFAULT_JWT_SECRET_KEY = "zxcvmnblkjhgfdsaqwertyupoi";

    private final CommandLine commandLine;

    public ArgumentService(String[] args) throws MiniBlogException {
        CommandLineParser commandLineParser = new BasicParser();
        Options options = new Options();
        // --ctx context-path
        options.addOption("ctx", true, "server context path, default: " + DEFAULT_CONTEXT_PATH);
        // -p 8080 | --port 8080
        options.addOption("p", "port", true, "server port, defualt: " + DEFAULT_LISTEN_PORT);
        // --auser username
        options.addOption("auser", true, "auth user, default: " + DEFAULT_AUTH_USER);
        // --apwd password
        options.addOption("apwd", true, "auth password, default: " + DEFAULT_AUTH_PWD);
        // --jwt_secret_key xxx
        options.addOption("jwt_secret_key", true, "jwt secret key, default: " + DEFAULT_JWT_SECRET_KEY);
        try {
            this.commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new MiniBlogException(String.format(
                    "parse command line failed, args: %s", Arrays.asList(args)), e);
        }
    }

    public String getContextPath() {
        if (commandLine.hasOption("ctx")) {
            return MRestUtils.formatContextPath(commandLine.getOptionValue("ctx"));
        }
        return DEFAULT_CONTEXT_PATH;
    }

    public int getListenPort() {
        if (commandLine.hasOption('p')) {
            return Integer.parseInt(commandLine.getOptionValue('p'));
        }
        if (commandLine.hasOption("port")) {
            return Integer.parseInt(commandLine.getOptionValue("port"));
        }
        return DEFAULT_LISTEN_PORT;
    }

    private volatile LoginUserVo loginUserVo;

    public synchronized LoginUserVo getLoginUserVo() {
        if (this.loginUserVo != null) {
            return loginUserVo;
        }
        loginUserVo = new LoginUserVo(getAuthUsername()
                , Base64.getEncoder().encodeToString(getAuthPassword().getBytes(StandardCharsets.UTF_8)));
        return loginUserVo;
    }

    public String getAuthUsername() {
        if (commandLine.hasOption("auser")) {
            return commandLine.getOptionValue("auser");
        }
        return DEFAULT_AUTH_USER;
    }

    public String getAuthPassword() {
        if (commandLine.hasOption("apwd")) {
            return commandLine.getOptionValue("apwd");
        }
        return DEFAULT_AUTH_PWD;
    }

    public String getJwtSecretKey() {
        if (commandLine.hasOption("jwt_secret_key")) {
            return commandLine.getOptionValue("jwt_secret_key");
        }
        return DEFAULT_JWT_SECRET_KEY;
    }

}
