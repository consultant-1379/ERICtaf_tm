package com.ericsson.cifwk.tm.application.security.impl;

import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;

import javax.inject.Inject;

/**
 *
 */
public class UserManagementServiceImpl implements UserManagementService {

    public static final String CURRENT_USER_ALIAS = "me";

    @Inject
    private UserProfileService userProfileService;

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private UserRepository userRepository;

    @Override
    public UserProfile fetchOrCreateUserProfile(String userId) {
        User user = fetchUser(userId);
        return userProfileService.findOrCreate(user);
    }

    @Override
    public User fetchUser(String userId) {
        User user;
        try {
            if (CURRENT_USER_ALIAS.equals(userId)) {
                user = userRepository.find(
                        userSessionService.getCurrentUser().getId());
            } else {
                user = userRepository.findByExternalId(userId);
            }
        } catch (NullPointerException e) {
            return null;
        }
        return user;
    }

    @Override
    public boolean isAuthorized() {
        UserInfo currentUser = userSessionService.getCurrentUser();
        UserProfile userProfile = fetchOrCreateUserProfile(currentUser.getUserId());
        return userProfile.isAdministrator();
    }

}
