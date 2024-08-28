/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.application.security.AuthenticationService;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public abstract class AbstractAuthenticationService implements AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticationService.class);
    private static final String DEFAULT_ROLE = "VIEWER";

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private UserRepository userRepository;

    @Override
    public boolean isLoggedIn() {
        return userSessionService.hasCurrentUser();
    }


    @Override
    @Transactional
    public String login(String username, String credentials, boolean rememberMe) {
        boolean isAuthenticated = authenticate(getToken(username, credentials, rememberMe));
        if (isAuthenticated) {
            SecurityUtils.getSubject().hasRole(DEFAULT_ROLE); // to force sync of roles and other LDAP info
            User user = userRepository.findByExternalId(username);
            userSessionService.setCurrentUser(user);
            userSessionService.startSessionRecord();
            return userSessionService.getCurrentSessionId();
        }
        LOGGER.info("Authentication of user {} failed ({})", username, this.getClass().getSimpleName());
        return null;
    }

    @Override
    @Transactional
    public String login(String sessionId) {
        SecurityUtils.getSubject().hasRole(DEFAULT_ROLE); // to force sync of roles and other LDAP info
        UserSession userSession = userSessionService.findSessionId(sessionId);

        Subject subject = SecurityUtils.getSubject();

        if (userSession != null && subject.isRemembered()) {
            userSessionService.setCurrentUser(userSession.getUser());
            userSessionService.startSessionRecord();
            return userSessionService.getCurrentSessionId();
        }
        return null;
    }

    @Override
    public void logout() {
        userSessionService.endSessionRecord();
        userSessionService.removeCurrentUser();
        SecurityUtils.getSubject().logout();
    }

    protected abstract AuthenticationToken getToken(String username, String credentials, boolean rememberMe);

    private boolean authenticate(AuthenticationToken token) {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return subject.isAuthenticated();
        } catch (AuthenticationException e) {
            // ignore
        }
        return false;
    }

}
