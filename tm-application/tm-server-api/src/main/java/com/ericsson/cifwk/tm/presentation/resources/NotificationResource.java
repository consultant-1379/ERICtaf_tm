/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface NotificationResource {

    /**
     * Returns a list of notifications active at a given point in time.
     *
     * @param timestamp Timestamp for notification snapshot.
     * @return List of notification objects.
     */
    @GET
    Response getNotifications(@QueryParam("timestamp") long timestamp);


    /**
     * Request to create a notification.
     *
     * @param secret           Secret key.
     * @param notificationInfo Notification data.
     * @return Created notification info.
     */
    @POST
    Response createNotification(@QueryParam("secret") @NotNull String secret,
                                @Valid NotificationInfo notificationInfo);

    /**
     * Returns a real-time stream of notification events.
     * For use with SSE clients.
     *
     * @return Stream of notification events.
     */
    @GET
    @Path("events")
    @Produces("text/event-stream")
    Response getNotificationEvents();

}
