package com.ericsson.cifwk.tm.application.requests;

import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;

/**
 * @author ebuzdmi
 */
public final class UpdateUserProfileRequest {

    private String userId;
    private UserProfileInfo userProfile;
    private boolean administrator;

    public UpdateUserProfileRequest(String userId, UserProfileInfo userProfile) {
        this.userId = userId;
        this.userProfile = userProfile;
    }

    public String getUserId() {
        return userId;
    }

    public UserProfileInfo getUserProfile() {
        return userProfile;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
