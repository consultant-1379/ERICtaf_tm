package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.test.TestPersistentEntityFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;

import javax.inject.Inject;

public class SecurityStub {
    private PersistenceHelper persistence;
    private UserSessionService userSessionService;

    @Inject
    public SecurityStub(PersistenceHelper persistence, UserSessionService userSessionService) {
        this.persistence = persistence;
        this.userSessionService = userSessionService;
    }

    public void begin(String currentUserId) {
        SecurityUtils.setSecurityManager(new DefaultSecurityManager());
        User currentUser = new TestPersistentEntityFactory(persistence).createUser(currentUserId);
        userSessionService.setCurrentUser(currentUser);
    }

    public void end() {
        userSessionService.setCurrentUser(null);
        SecurityUtils.setSecurityManager(null);
    }
}
