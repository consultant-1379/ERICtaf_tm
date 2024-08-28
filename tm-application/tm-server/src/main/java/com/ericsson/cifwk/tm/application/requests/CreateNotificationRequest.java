/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;

public class CreateNotificationRequest {

    private final String secret;
    private final NotificationInfo notificationInfo;

    public CreateNotificationRequest(String secret, NotificationInfo notificationInfo) {
        this.secret = secret;
        this.notificationInfo = notificationInfo;
    }

    public String getSecret() {
        return secret;
    }

    public NotificationInfo getNotificationInfo() {
        return notificationInfo;
    }

}
