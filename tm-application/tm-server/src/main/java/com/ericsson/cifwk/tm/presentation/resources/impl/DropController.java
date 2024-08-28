package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateDropCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteDropCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateDropCommand;
import com.ericsson.cifwk.tm.application.queries.DropQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.resources.DropResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Controller
public class DropController implements DropResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateDropCommand createDropCommand;

    @Inject
    private UpdateDropCommand updateDropCommand;

    @Inject
    private DeleteDropCommand deleteDropCommand;

    @Inject
    private DropQuerySet dropQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getDrop(Long id) {
        return dropQuerySet.getDrop(id);
    }

    @Override
    public Response getDrops(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return dropQuerySet.getDropsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(DropInfo drop) {
        return commandProcessor.process(createDropCommand, drop);
    }

    @Override
    public Response update(Long id, DropInfo drop) {
        return commandProcessor.process(updateDropCommand, drop);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteDropCommand, id);
    }
}
