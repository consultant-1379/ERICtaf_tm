/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.responses;

import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.server.ChunkedOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;

@Singleton
public class CloseableSseBroadcaster extends SseBroadcaster implements Closeable {

    private final Logger logger = LoggerFactory.getLogger(CloseableSseBroadcaster.class);

    @Override
    public void onException(ChunkedOutput<OutboundEvent> chunkedOutput, Exception exception) {
        try {
            chunkedOutput.close();
        } catch (IOException e) {
            logger.error("Failed to close SSE stream", e);
        }
    }

    @Override
    @PreDestroy
    public void close() throws IOException {
        closeAll();
    }

}
