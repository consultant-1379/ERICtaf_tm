/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmbeddedServer {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

    private Server server;

    private WebAppContext context;

    private int httpPort;

    public EmbeddedServer() {
        httpPort = Integer.parseInt(System.getProperty("server.port", "8080"));
    }

    void start() throws Exception {
        String staticPath = System.getProperty("server.paths.static", "../tm-client/target/deployFolder");
        String descriptorPath = System.getProperty("server.paths.descriptor", "src/main/webapp/WEB-INF/web.xml");

        logger.info("Starting on port {}", httpPort);

        System.setProperty("embedded", "true");

        server = new Server(httpPort);
        context = new WebAppContext();
        context.setResourceBase(staticPath);
        context.setDescriptor(descriptorPath);
        context.setContextPath("/tm-server");
        context.setParentLoaderPriority(true);
        context.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        server.setHandler(context);
        server.start();
    }

    public boolean isStarted() {
        return server != null;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    void stop() {
        try {
            server.stop();
        } catch (Exception ignored) {
        }
        server = null;
        LeakCleaner.clean();
    }

    protected int getHttpPort() {
        return httpPort;
    }

}
