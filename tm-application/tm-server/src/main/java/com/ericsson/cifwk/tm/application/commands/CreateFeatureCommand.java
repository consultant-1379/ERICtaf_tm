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
import javax.ws.rs.core.Response;

public class CreateFeatureCommand implements Command<FeatureInfo> {

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Inject
    private ProductRepository productRepository;

    @Override
    public Response apply(FeatureInfo input) {
        productFeatureRepository.disableFilter();
        ProductFeature entity = featureMapper.mapDto(input, ProductFeature.class);
        if (input.getProduct() == null) {
            throw new BadRequestException(Responses.badRequest("Product information not attached"));
        }
        ProductFeature feature = productFeatureRepository.findByProductAndName(input.getProduct().getId(), input.getName());
        if (feature != null) {
            if (!feature.isDeleted()) {
                productFeatureRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Feature already exists"));
            } else {
                feature.undelete();
                FeatureInfo featureDto = featureMapper.mapEntity(feature, new FeatureInfo());
                productFeatureRepository.enableFilter();
                return Responses.created(featureDto);
            }
        }
        entity.setProduct(productRepository.find(input.getProduct().getId()));
        productFeatureRepository.persist(entity);
        productFeatureRepository.enableFilter();
        FeatureInfo dto = featureMapper.mapEntity(entity, new FeatureInfo());
        return Responses.created(dto);
    }

}
