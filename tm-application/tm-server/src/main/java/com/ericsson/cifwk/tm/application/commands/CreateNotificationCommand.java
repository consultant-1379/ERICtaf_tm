/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.CreateNotificationRequest;
import com.ericsson.cifwk.tm.application.security.SecretService;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.application.services.NotificationService;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.NotificationMapper;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class CreateNotificationCommand implements Command<CreateNotificationRequest> {

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationMapper notificationMapper;

    @Inject
    private SecretService secretService;

    @Inject
    private NotificationService notificationService;

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private UserRepository userRepository;

    @Override
    public Response apply(CreateNotificationRequest input) {
        String secret = input.getSecret();
        NotificationInfo notificationInfo = input.getNotificationInfo();

        if (!secretService.validate(secret)) {
            return Responses.badCredentials("Wrong secret");
        }

        Notification entity = notificationMapper.mapDto(notificationInfo, Notification.class);

        entity.setAuthor(userRepository.find(
                userSessionService.getCurrentUser().getId())); // Force session user
        notificationRepository.persist(entity);
        NotificationInfo created = notificationMapper.mapEntity(entity, NotificationInfo.class);

        if (entity.isActive()) {
            notificationService.broadcast(created);
        }

        return Responses.created(created);
    }

}
