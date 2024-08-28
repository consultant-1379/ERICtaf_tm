/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.NotificationMapper;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@QuerySet
public class NotificationQuerySet {

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    public Response getNotifications(long timestamp) {
        List<Notification> entities = notificationRepository.findForTimestamp(timestamp);
        List<NotificationInfo> dtos = Lists.newArrayList();
        for (Notification entity : entities) {
            dtos.add(notificationMapper.mapEntity(entity, NotificationInfo.class));
        }
        return Responses.ok(dtos);
    }

}
