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
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class UpdateFeatureCommand implements Command<FeatureInfo> {

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Override
    public Response apply(FeatureInfo input) {
        productFeatureRepository.disableFilter();
        ProductFeature feature = productFeatureRepository.findByProductAndName(input.getProduct().getId(), input.getName());
        if (feature != null) {
            if (feature.isDeleted()) {
                productFeatureRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted feature of the same name still exists." +
                        " Please try again using a different name"));
            } else if (!feature.getId().equals(input.getId())) {
                productFeatureRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Feature already exists"));
            }
        }
        ProductFeature entity = productFeatureRepository.find(input.getId());
        if (entity == null) {
            productFeatureRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        productFeatureRepository.enableFilter();
        featureMapper.mapDto(input, entity);
        entity.setProduct(productRepository.find(input.getProduct().getId()));
        productFeatureRepository.save(entity);
        return Responses.ok();
    }
}
