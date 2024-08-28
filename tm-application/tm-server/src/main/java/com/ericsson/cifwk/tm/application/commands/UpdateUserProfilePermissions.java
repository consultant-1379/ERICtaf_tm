package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.UpdateUserProfileRequest;
import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;

/**
 * @author egergle
 */
public class UpdateUserProfilePermissions implements Command<UpdateUserProfileRequest> {

    @Inject
    private UserManagementService userManagementService;

    @Override
    public Response apply(UpdateUserProfileRequest input) {
        if (!userManagementService.isAuthorized()) {
            throw new NotAuthorizedException(Responses.notAuthorized("User has no permissions to perform this operation"));
        }
        String userId = input.getUserId();
        UserProfile existingProfile = userManagementService.fetchOrCreateUserProfile(userId);

        existingProfile.setAdministrator(input.isAdministrator());
        return Responses.ok();
    }

}
