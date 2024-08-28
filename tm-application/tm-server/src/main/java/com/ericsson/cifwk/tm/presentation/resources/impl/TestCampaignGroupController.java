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
import com.ericsson.cifwk.tm.application.commands.CreateTestCampaignGroupCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTestCampaignGroupCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTestCampaignGroupCommand;
import com.ericsson.cifwk.tm.application.queries.TestCampaignGroupQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.resources.TestCampaignGroupResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Controller
public class TestCampaignGroupController implements TestCampaignGroupResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTestCampaignGroupCommand createTestCampaignGroupCommand;

    @Inject
    private TestCampaignGroupQuerySet testCampaignQueryQuerySet;

    @Inject
    private UpdateTestCampaignGroupCommand updateTestCampaignGroupCommand;

    @Inject
    private DeleteTestCampaignGroupCommand deleteTestCampaignGroupCommand;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getTestCampaignGroups(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return testCampaignQueryQuerySet.getTestCampaignGroupsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response getTestCampaignGroup(Long id) {
        return testCampaignQueryQuerySet.getTestCampaignGroup(id);
    }

    @Override
    public Response create(TestCampaignGroupInfo testCampaignGroupInfo) {
        return commandProcessor.process(createTestCampaignGroupCommand, testCampaignGroupInfo);
    }

    @Override
    public Response update(Long id, TestCampaignGroupInfo testCampaignGroupInfo) {
        return commandProcessor.process(updateTestCampaignGroupCommand, testCampaignGroupInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteTestCampaignGroupCommand, id);
    }

    @Override
    public Response getTestCampaignGroupCSV(Long id) {
        return testCampaignQueryQuerySet.getTestCampaignGroupTestCasesCSV(id);
    }

}
