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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestSuiteMapper;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CreateSuiteCommand implements Command<SuiteInfo> {

    @Inject
    private TestSuiteRepository testSuiteRepository;

    @Inject
    private TestSuiteMapper suiteMapper;

    @Inject
    private ProductFeatureRepository featureRepository;

    @Override
    public Response apply(SuiteInfo input) {
        testSuiteRepository.disableFilter();
        TestSuite entity = suiteMapper.mapDto(input, TestSuite.class);
        Optional<TestSuite> testSuite = testSuiteRepository.findByNameAndFeatureId(input.getName(), input.getFeature().getId());
        if (testSuite.isPresent()) {
            if (!testSuite.get().isDeleted()) {
                testSuiteRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Test suite already exists"));
            } else {
                testSuite.get().undelete();
                SuiteInfo dto = suiteMapper.mapEntity(entity, new SuiteInfo());
                testSuiteRepository.enableFilter();
                return Responses.created(dto);
            }
        }
        entity.setFeature(featureRepository.find(entity.getFeature().getId()));
        testSuiteRepository.persist(entity);
        SuiteInfo dto = suiteMapper.mapEntity(entity, new SuiteInfo());
        testSuiteRepository.enableFilter();
        return Responses.created(dto);
    }

}
