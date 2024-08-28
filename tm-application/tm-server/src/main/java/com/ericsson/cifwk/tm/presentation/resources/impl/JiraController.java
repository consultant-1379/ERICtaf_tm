package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.SyncDefectsCommand;
import com.ericsson.cifwk.tm.application.commands.SyncRequirementsCommand;
import com.ericsson.cifwk.tm.application.requests.CreateSyncRequest;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.resources.JiraResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 * Created by egergle on 07/10/2015.
 */
@Controller
public class JiraController implements JiraResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private SyncRequirementsCommand syncRequirementsCommand;

    @Inject
    private SyncDefectsCommand syncDefectsCommand;

    @Override
    public Response getRequirements(String jql, String password) {
        CreateSyncRequest request = new CreateSyncRequest(password, jql);
        return commandProcessor.process(syncRequirementsCommand, request);
    }

    @Override
    public Response getDefects(String jql, String password) {
        CreateSyncRequest request = new CreateSyncRequest(password, jql);
        return commandProcessor.process(syncDefectsCommand, request);
    }
}
