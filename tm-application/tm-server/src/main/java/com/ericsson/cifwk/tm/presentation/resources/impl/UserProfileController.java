/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.DeleteSavedSearchCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateUserProfileCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateUserProfilePermissions;
import com.ericsson.cifwk.tm.application.queries.UserQuerySet;
import com.ericsson.cifwk.tm.application.requests.SavedSearchRequest;
import com.ericsson.cifwk.tm.application.requests.UpdateUserProfileRequest;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.resources.UserProfileResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class UserProfileController implements UserProfileResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private UpdateUserProfileCommand updateUserProfileCommand;

    @Inject
    private DeleteSavedSearchCommand deleteSavedSearchCommand;

    @Inject
    private UserQuerySet userQuerySet;

    @Inject
    private UpdateUserProfilePermissions updateUserProfilePermissions;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getUserProfile(String userId) {
        return userQuerySet.loadUserProfile(userId);
    }

    @Override
    public Response getUserProfile(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return userQuerySet.getUserProfileByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response updateUserProfile(String userId, UserProfileInfo userProfile) {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest(userId, userProfile);
        return commandProcessor.process(updateUserProfileCommand, request);
    }

    @Override
    public Response deleteSavedSearch(String userId, Long id) {
        SavedSearchInfo savedSearchInfo = new SavedSearchInfo();
        savedSearchInfo.setId(id);
        return commandProcessor.process(deleteSavedSearchCommand, new SavedSearchRequest(userId, savedSearchInfo));
    }

    @Override
    public Response getCompletion(String externalId, int limit) {
        return userQuerySet.getCompletion(externalId, limit);
    }

    @Override
    public Response updatePermissions(String userId, boolean permission) {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest(userId, null);
        request.setAdministrator(permission);
        return commandProcessor.process(updateUserProfilePermissions, request);
    }
}
