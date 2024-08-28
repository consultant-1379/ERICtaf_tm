/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateNotificationCommand;
import com.ericsson.cifwk.tm.application.queries.NotificationQuerySet;
import com.ericsson.cifwk.tm.application.requests.CreateNotificationRequest;
import com.ericsson.cifwk.tm.presentation.RequestPreconditions;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.resources.NotificationResource;
import com.ericsson.cifwk.tm.presentation.responses.CloseableSseBroadcaster;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import org.glassfish.jersey.media.sse.EventOutput;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class NotificationController implements NotificationResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateNotificationCommand createNotificationCommand;

    @Inject
    private NotificationQuerySet notificationQuerySet;

    @Inject
    private CloseableSseBroadcaster sseBroadcaster;

    @Override
    public Response getNotifications(long timestamp) {
        return notificationQuerySet.getNotifications(timestamp);
    }

    @Override
    public Response createNotification(String secret, NotificationInfo notificationInfo) {
        RequestPreconditions.checkArgument(
                notificationInfo.getId() == null,
                "Given notification has already been created"
        );
        CreateNotificationRequest request = new CreateNotificationRequest(secret, notificationInfo);
        return commandProcessor.process(createNotificationCommand, request);
    }

    @Override
    public Response getNotificationEvents() {
        final EventOutput eventOutput = new EventOutput();
        sseBroadcaster.add(eventOutput);
        return Responses.ok(eventOutput);
    }

}
