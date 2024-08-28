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
import com.ericsson.cifwk.tm.application.requests.CreateSyncRequest;
import com.ericsson.cifwk.tm.application.security.SecretService;
import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class SyncRequirementsCommand implements Command<CreateSyncRequest> {

    @Inject
    private RequirementService requirementService;

    @Inject
    private SecretService secretService;

    @Override
    public Response apply(CreateSyncRequest request) {

        if (!secretService.validate(request.getSecret())) {
            return Responses.badCredentials("Wrong Password");
        }
        requirementService.fetchAndSaveUpdatedRequirements(request.getJql());
        return Responses.ok();
    }

}
