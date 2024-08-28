package com.ericsson.cifwk.tm.domain.helper;

import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class UserHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private UserRepository userRepository;

    public String setUpdateUser(TestCase testCase) {
        UserInfo currentUser = userSessionService.getCurrentUser();
        if (currentUser != null) {
            testCase.setUpdatedByUser(
                    userRepository.find(currentUser.getId()));
            return currentUser.getId().toString();
        } else {
            LOGGER.warn("Cannot get current user");
            return null;
        }
    }

    public UserInfo getCurrentUser() {
        return userSessionService.getCurrentUser();
    }

}
