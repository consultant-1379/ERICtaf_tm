/*
 * COPYRIGHT Ericsson (c) 2017.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateTestTypeCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTestTypeCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTestTypeCommand;
import com.ericsson.cifwk.tm.application.queries.TestTypeQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import com.ericsson.cifwk.tm.presentation.resources.TestTypeResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class TestTypeController implements TestTypeResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTestTypeCommand createTestTypeCommand;

    @Inject
    private UpdateTestTypeCommand updateTestTypeCommand;

    @Inject
    private DeleteTestTypeCommand deleteTestTypeCommand;

    @Inject
    private TestTypeQuerySet testTypeQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getTestType(Long id) {
        return testTypeQuerySet.getTestType(id);
    }

    @Override
    public Response getTestTypes(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return testTypeQuerySet.getTestTypesByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(TestTypeInfo testTypeInfo) {
        return commandProcessor.process(createTestTypeCommand, testTypeInfo);
    }

    @Override
    public Response update(Long id, TestTypeInfo testTypeInfo) {
        Preconditions.checkArgument(
                id.equals(testTypeInfo.getId()),
                "Test type ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateTestTypeCommand, testTypeInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteTestTypeCommand, id);
    }

}
