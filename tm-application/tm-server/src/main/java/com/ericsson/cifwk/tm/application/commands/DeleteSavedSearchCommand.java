/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.SavedSearchRequest;
import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearch;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearchRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class DeleteSavedSearchCommand implements Command<SavedSearchRequest> {

    @Inject
    private UserManagementService userManagementService;

    @Inject
    private SavedSearchRepository savedSearchRepository;

    @Override
    public Response apply(SavedSearchRequest input) {

        UserProfile userProfile = userManagementService.fetchOrCreateUserProfile(input.getUserId());
        if (userProfile == null) {
            throw new NotFoundException(Responses.notFound());
        }

        Optional<SavedSearch> savedSearch = userProfile.getSavedSearch().stream()
                .filter(item -> item.getId().equals(input.getSavedSearchInfo().getId()))
                .findFirst();

        if (!savedSearch.isPresent()) {
            throw new NotFoundException(Responses.notFound());
        }

        SavedSearch savedItem = savedSearch.get();
        userProfile.getSavedSearch().remove(savedItem);

        savedSearchRepository.remove(savedItem);


        return Responses.noContent();
    }

}
