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
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TechnicalComponentMapper;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public class CreateTechnicalComponentCommand implements Command<TechnicalComponentInfo> {

    @Inject
    private TechnicalComponentRepository technicalComponentRepository;

    @Inject
    private TechnicalComponentMapper technicalComponentMapper;

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Override
    public Response apply(TechnicalComponentInfo input) {
        technicalComponentRepository.disableFilter();
        TechnicalComponent entity = technicalComponentMapper.mapDto(input, new TechnicalComponent());

        if (input.getFeature() == null) {
            throw new BadRequestException(Responses.badRequest("Feature information not attached"));
        }
        TechnicalComponent technicalComponent = technicalComponentRepository
                .findByFeatureAndName(input.getFeature().getId(), input.getName());

        if (technicalComponent != null) {
            if (!technicalComponent.isDeleted()) {
                technicalComponentRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Component already exists"));
            } else {
                technicalComponent.undelete();
                TechnicalComponentInfo dto = technicalComponentMapper.mapEntity(new TechnicalComponentInfo(), entity);
                technicalComponentRepository.enableFilter();
                return Responses.created(dto);
            }
        }
        entity.setFeature(productFeatureRepository.find(entity.getFeature().getId()));
        technicalComponentRepository.persist(entity);
        TechnicalComponentInfo dto = technicalComponentMapper.mapEntity(new TechnicalComponentInfo(), entity);
        technicalComponentRepository.enableFilter();
        return Responses.created(dto);
    }

}
