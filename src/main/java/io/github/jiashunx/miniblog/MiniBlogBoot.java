package io.github.jiashunx.miniblog;

import io.github.jiashunx.masker.rest.framework.MRestServer;

/**
 * @author jiashunx
 */
public class MiniBlogBoot {
    public static void main(String[] args) {
        new MRestServer("mini-blog").listenPort(8080).start();
    }
}
