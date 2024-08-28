/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.annotations.Service;
import com.ericsson.cifwk.tm.application.services.NotificationService;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.responses.CloseableSseBroadcaster;
import org.glassfish.jersey.media.sse.OutboundEvent;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Inject
    private CloseableSseBroadcaster sseBroadcaster;

    @Override
    public void broadcast(NotificationInfo notificationInfo) {
        OutboundEvent event = new OutboundEvent.Builder()
                .name("notification")
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .data(notificationInfo)
                .build();

        sseBroadcaster.broadcast(event);
    }

}
