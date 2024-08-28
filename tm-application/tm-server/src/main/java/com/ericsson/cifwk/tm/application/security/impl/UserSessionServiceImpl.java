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

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.ericsson.cifwk.tm.domain.model.users.UserSessionRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class UserSessionServiceImpl implements UserSessionService {

    private static final String USER_ATTRIBUTE_NAME = "user";
    private final Logger logger = LoggerFactory.getLogger(UserSessionServiceImpl.class);
    @Inject
    private UserSessionRepository userSessionRepository;
    @Inject
    private UserMapper userMapper;
    @Inject
    private UserRepository userRepository;


    @Override
    public void setCurrentUser(User user) {
        Session session = getSession();
        if (session != null) {
            session.setAttribute(USER_ATTRIBUTE_NAME,
                    userMapper.mapEntity(user, new UserInfo()));
        }
    }

    @Override
    public UserSession findSessionId(String sessionId) {
        return userSessionRepository.findActiveSessionId(sessionId);
    }

    @Override
    @Transactional
    public void startSessionRecord() {
        UserInfo user = getCurrentUser();
        String sessionId = getCurrentSessionId();
        if (user == null || sessionId == null) {
            return;
        }
        UserSession currentUserSession = findSessionId(sessionId);
        if (currentUserSession != null) {
            return;
        }
        logger.info("Starting user session: {}", sessionId);
        UserSession userSession = new UserSession();
        userSession.setUser(userRepository.find(user.getId()));
        userSession.setSessionId(sessionId);
        userSessionRepository.save(userSession);
    }

    @Override
    @Transactional
    public void endSessionRecord() {
        String sessionId = getCurrentSessionId();
        SecurityUtils.getSubject().getSession().stop();
        updateSessionToClosed(sessionId);
    }

    @Transactional
    public void updateSessionToClosed(String sessionId) {
        if (sessionId == null) {
            return;
        }
        logger.info("Closing user session: {}", sessionId);
        UserSession userSession = findSessionId(sessionId);
        if (userSession == null) {
            logger.warn("User session not found: {}", sessionId);
            return;
        }
        userSession.setDeletedAt(new Date());
        userSessionRepository.save(userSession);
    }

    @Override
    public UserInfo getCurrentUser() {
        try {
            Session session = getSession();
            return session == null ? null : (UserInfo) session.getAttribute(USER_ATTRIBUTE_NAME);
        } catch (UnavailableSecurityManagerException e) {
            return null;
        }
    }

    @Override
    public boolean hasCurrentUser() {
        return getCurrentUser() != null;
    }

    @Override
    public void removeCurrentUser() {
        Session session = getSession();
        if (session != null) {
            session.removeAttribute(USER_ATTRIBUTE_NAME);
        }
    }

    @Override
    public String getCurrentSessionId() {
        Session session = getSession();
        return session == null ? null : session.getId().toString();
    }

    private Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }


}
