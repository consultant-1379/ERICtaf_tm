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
import com.ericsson.cifwk.tm.application.commands.CreateScopeCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteScopeCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateScopeCommand;
import com.ericsson.cifwk.tm.application.queries.ScopeQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.resources.ScopeResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class ScopeController implements ScopeResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateScopeCommand createScopeCommand;

    @Inject
    private UpdateScopeCommand updateScopeCommand;

    @Inject
    private DeleteScopeCommand deleteScopeCommand;

    @Inject
    private ScopeQuerySet scopeQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getScope(Long scopeId) {
        return scopeQuerySet.getScope(scopeId);
    }

    @Override
    public Response getGroups(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return scopeQuerySet.getGroupsByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response create(GroupInfo scope) {
        return commandProcessor.process(createScopeCommand, scope);
    }

    @Override
    public Response update(Long scopeId, GroupInfo scope) {
        Preconditions.checkArgument(
                scopeId.equals(scope.getId()),
                "Scope ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateScopeCommand, scope);
    }

    @Override
    public Response delete(Long scopeId) {
        return commandProcessor.process(deleteScopeCommand, scopeId);
    }

}
