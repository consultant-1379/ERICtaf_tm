/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApplicationServer {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServer.class);

    public static void main(String[] args) throws Exception {
        new ApplicationServer().run();
    }

    public void run() throws Exception {
        System.out.println("Starting local TMS Server. Press ^C to stop.");
        final EmbeddedServer server = new EmbeddedServer();
        server.start();
        stopServerOnShutdown(server);

        server.join();
    }

    public void stopServerOnShutdown(final EmbeddedServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Shutting down");
                server.stop();
            }
        });
    }
}
