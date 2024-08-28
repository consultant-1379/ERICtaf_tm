/*
 * COPYRIGHT Ericsson (c) 2016.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestSuiteMapper;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class UpdateSuiteCommand implements Command<SuiteInfo> {

    @Inject
    private TestSuiteRepository suiteRepository;

    @Inject
    private TestSuiteMapper suiteMapper;

    @Override
    public Response apply(SuiteInfo input) {
        suiteRepository.disableFilter();
        Optional<TestSuite> testSuite = suiteRepository.findByNameAndFeatureId(input.getName(), input.getFeature().getId());
        if (testSuite.isPresent()) {
            if (testSuite.get().isDeleted()) {
                suiteRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted suite of the same name still exists. " +
                        "Please try again using a different name"));
            } else if (!testSuite.get().getId().equals(input.getId())) {
                suiteRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Test suite already exists"));
            }
        }
        TestSuite entity = suiteRepository.find(input.getId());
        if (entity == null) {
            suiteRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        suiteRepository.enableFilter();
        suiteMapper.mapDto(input, entity);
        suiteRepository.save(entity);
        return Responses.ok();
    }

}
