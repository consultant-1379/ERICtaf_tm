/*
 * COPYRIGHT Ericsson (c) 2016.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.NotificationCleanUpService;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationRepository;
import com.google.inject.persist.Transactional;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class NotificationCleanUpServiceImpl implements NotificationCleanUpService {

    @Inject
    NotificationRepository notificationRepository;

    private final Logger logger = LoggerFactory.getLogger(NotificationCleanUpServiceImpl.class);

    @Override
    @Transactional
    public void cleanUp(int weeks) {
        logger.info("Cleaning up older Notifications");
        List<Notification> beforeDate = notificationRepository.findBeforeDate(new DateTime().minusWeeks(weeks).toDate());
        if (beforeDate != null) {
            for (Notification notification : beforeDate) {
                notificationRepository.remove(notification);
            }
        }
    }
}



