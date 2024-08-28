/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.listeners;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.Bootstrap;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.base.Throwables;
import com.google.inject.Injector;
import org.hibernate.envers.RevisionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditListener implements RevisionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditListener.class);


    @Override
    public void newRevision(Object revisionEntity) {
        try {
            doNewRevision((AuditRevisionEntity) revisionEntity);
        } catch (Exception e) {
            // exceptions are swallowed by Hibernate, that is why explicit error message
            LOGGER.error("Exception in Hibernate listener", e);
            Throwables.propagate(e);
        }
    }

    private void doNewRevision(AuditRevisionEntity revisionEntity) {
        Injector injector = Bootstrap.getApplicationInjector();
        UserSessionService userSessionService = injector.getInstance(UserSessionService.class);
        UserRepository userRepository = injector.getInstance(UserRepository.class);
        UserInfo currentUser = userSessionService.getCurrentUser();
        if (currentUser != null) {
            revisionEntity.setUser(
                    userRepository.find(currentUser.getId()));
        }
    }

}
