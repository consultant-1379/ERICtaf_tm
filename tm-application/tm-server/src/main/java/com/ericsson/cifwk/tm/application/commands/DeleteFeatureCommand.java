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
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteFeatureCommand implements Command<Long> {

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Override
    public Response apply(Long input) {
        ProductFeature feature = productFeatureRepository.find(input);
        if (feature == null || feature.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        feature.delete();
        feature.getComponents().stream().forEach(item -> item.delete());
        return Responses.noContent();
    }

}
