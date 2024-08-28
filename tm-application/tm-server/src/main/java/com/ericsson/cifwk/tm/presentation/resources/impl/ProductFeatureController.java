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
import com.ericsson.cifwk.tm.application.commands.CreateFeatureCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteFeatureCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateFeatureCommand;
import com.ericsson.cifwk.tm.application.queries.ProductFeatureQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.resources.ProductFeatureResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class ProductFeatureController implements ProductFeatureResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateFeatureCommand createFeatureCommand;

    @Inject
    private UpdateFeatureCommand updateFeatureCommand;

    @Inject
    private DeleteFeatureCommand deleteFeatureCommand;

    @Inject
    private ProductFeatureQuerySet productFeatureQuerySet;

    @Context
    private UriInfo uriInfo;

    @Override
    public Response getFeatures(String q, int page, int perPage, String orderBy, String orderMode) {
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return productFeatureQuerySet.getFeaturesByQuery(query, page, perPage, uriInfo);
    }

    @Override
    public Response getFeature(Long id) {
        return productFeatureQuerySet.getFeature(id);
    }

    @Override
    public Response create(FeatureInfo featureInfo) {
        return commandProcessor.process(createFeatureCommand, featureInfo);
    }

    @Override
    public Response update(Long id, FeatureInfo featureInfo) {
        Preconditions.checkArgument(
                id.equals(featureInfo.getId()),
                "Feature ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateFeatureCommand, featureInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteFeatureCommand, id);
    }

}
