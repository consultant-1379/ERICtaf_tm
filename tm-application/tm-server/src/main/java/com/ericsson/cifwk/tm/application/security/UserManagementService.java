package com.ericsson.cifwk.tm.application.security;

import com.ericsson.cifwk.tm.application.security.impl.UserManagementServiceImpl;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.google.inject.ImplementedBy;

/**
 *
 */
@ImplementedBy(UserManagementServiceImpl.class)
public interface UserManagementService {

    UserProfile fetchOrCreateUserProfile(String userId);

    User fetchUser(String userId);

    boolean isAuthorized();
}
