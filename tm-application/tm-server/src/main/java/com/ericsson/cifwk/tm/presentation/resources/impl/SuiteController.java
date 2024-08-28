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
import com.ericsson.cifwk.tm.application.commands.CreateSuiteCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteSuiteCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateSuiteCommand;
import com.ericsson.cifwk.tm.application.queries.SuiteQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import com.ericsson.cifwk.tm.presentation.resources.SuiteResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class SuiteController implements SuiteResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateSuiteCommand createSuiteCommand;

    @Inject
    private UpdateSuiteCommand updateSuiteCommand;

    @Inject
    private DeleteSuiteCommand deleteSuiteCommand;

    @Inject
    private SuiteQuerySet suiteQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getSuites(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return suiteQuerySet.getSuitesByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(SuiteInfo suiteInfo) {
        return commandProcessor.process(createSuiteCommand, suiteInfo);
    }

    @Override
    public Response update(Long id, SuiteInfo suiteInfo) {
        return commandProcessor.process(updateSuiteCommand, suiteInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteSuiteCommand, id);
    }
}
