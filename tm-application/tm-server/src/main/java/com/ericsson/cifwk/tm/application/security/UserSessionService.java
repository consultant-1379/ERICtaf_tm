/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.security;

import com.ericsson.cifwk.tm.application.security.impl.UserSessionServiceImpl;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.ImplementedBy;

@ImplementedBy(UserSessionServiceImpl.class)
public interface UserSessionService {
    void startSessionRecord();

    void endSessionRecord();

    UserInfo getCurrentUser();

    boolean hasCurrentUser();

    void removeCurrentUser();

    String getCurrentSessionId();

    void setCurrentUser(User user);

    UserSession findSessionId(String sessionId);
}
