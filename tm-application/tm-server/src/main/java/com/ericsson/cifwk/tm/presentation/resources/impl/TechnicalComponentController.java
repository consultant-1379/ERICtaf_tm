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
import com.ericsson.cifwk.tm.application.commands.CreateTechnicalComponentCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTechnicalComponentCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTechnicalComponentCommand;
import com.ericsson.cifwk.tm.application.queries.TechnicalComponentQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.resources.TechnicalComponentResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class TechnicalComponentController implements TechnicalComponentResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTechnicalComponentCommand createTechnicalCommand;

    @Inject
    private UpdateTechnicalComponentCommand updateTechnicalComponentCommand;

    @Inject
    private DeleteTechnicalComponentCommand technicalComponentCommand;

    @Inject
    private TechnicalComponentQuerySet technicalComponentQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getTechnicalComponent(Long id) {
        return technicalComponentQuerySet.getTechnicalComponent(id);
    }

    @Override
    public Response getTechnicalComponents(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return technicalComponentQuerySet.getComponentsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(TechnicalComponentInfo technicalComponentInfo) {
        return commandProcessor.process(createTechnicalCommand, technicalComponentInfo);

    }

    @Override
    public Response update(Long id, TechnicalComponentInfo technicalComponentInfo) {
        Preconditions.checkArgument(
                id.equals(technicalComponentInfo.getId()),
                "Technical Component id in URL path and request body do not match"
        );
        return commandProcessor.process(updateTechnicalComponentCommand, technicalComponentInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(technicalComponentCommand, id);
    }

}
