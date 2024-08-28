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
import com.ericsson.cifwk.tm.application.commands.CreateReviewGroupCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteReviewGroupCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateReviewGroupCommand;
import com.ericsson.cifwk.tm.application.queries.ReviewGroupQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.resources.ReviewGroupResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class ReviewGroupController implements ReviewGroupResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private ReviewGroupQuerySet reviewGroupQuerySet;

    @Inject
    private UpdateReviewGroupCommand updateReviewGroupCommand;

    @Inject
    private DeleteReviewGroupCommand deleteReviewGroupCommand;

    @Inject
    private CreateReviewGroupCommand createReviewGroupCommand;

    @Inject
    private CommandProcessor commandProcessor;


    @Override
    public Response getReviewGroups(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return reviewGroupQuerySet.getByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteReviewGroupCommand, id);
    }

    @Override
    public Response update(Long id, ReviewGroupInfo reviewGroupInfo) {
        return commandProcessor.process(updateReviewGroupCommand, reviewGroupInfo);
    }

    @Override
    public Response create(ReviewGroupInfo reviewGroupInfo) {
        return commandProcessor.process(createReviewGroupCommand, reviewGroupInfo);
    }

    @Override
    public Response getCompletion(String search, int limit) {
        return reviewGroupQuerySet.getCompletion(search, limit);
    }
}
