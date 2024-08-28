/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.security;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicLong;

public class TmSessionListener implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TmSessionListener.class);

    private static AtomicLong totalActiveSessions = new AtomicLong();

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        String sessionId = Preconditions.checkNotNull(httpSessionEvent.getSession().getId());
        LOGGER.info("Session created: {}", sessionId);
        LOGGER.info("There are {} live sessions", totalActiveSessions.incrementAndGet());
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent httpSessionEvent) {
        String sessionId = Preconditions.checkNotNull(httpSessionEvent.getSession().getId());
        LOGGER.info("Session destroyed: {}", sessionId);
        LOGGER.info("There are {} live sessions", totalActiveSessions.decrementAndGet());
    }

    public static long getTotalActiveSessionCount() {
        return totalActiveSessions.get();
    }
}
