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
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class UpdateTechnicalComponentCommand implements Command<TechnicalComponentInfo> {

    @Inject
    private TechnicalComponentRepository technicalComponentRepository;

    @Inject
    private TechnicalComponentMapper technicalComponentMapper;

    @Inject
    private ProductFeatureRepository productFeatureRepository;

    @Override
    public Response apply(TechnicalComponentInfo input) {
        technicalComponentRepository.disableFilter();
        TechnicalComponent technicalComponent = technicalComponentRepository
                .findByFeatureAndName(input.getFeature().getId(), input.getName());
        if (technicalComponent != null) {
            if (technicalComponent.isDeleted()) {
                technicalComponentRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted entity of the same name still exists." +
                        " Please try again using a different name"));
            } else if (!technicalComponent.getId().equals(input.getId())) {
                technicalComponentRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Component already exists"));
            }
        }
        TechnicalComponent entity = technicalComponentRepository.find(input.getId());
        if (entity == null) {
            technicalComponentRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        technicalComponentRepository.enableFilter();
        technicalComponentMapper.mapDto(input, entity);
        entity.setFeature(productFeatureRepository.find(input.getFeature().getId()));
        technicalComponentRepository.save(entity);
        return Responses.ok();
    }
}
