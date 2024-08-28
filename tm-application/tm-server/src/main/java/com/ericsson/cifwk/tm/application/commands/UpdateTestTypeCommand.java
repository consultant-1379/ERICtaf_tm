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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestTypeMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class UpdateTestTypeCommand implements Command<TestTypeInfo> {

    @Inject
    private TestTypeRepository testTypeRepository;

    @Inject
    private TestTypeMapper testTypeMapper;

    @Override
    public Response apply(TestTypeInfo input) {
        testTypeRepository.disableFilter();
        Optional<TestType> testType = testTypeRepository.findByNameAndProductId(input.getName(), input.getProduct().getId());
        if (testType.isPresent()) {
            if (testType.get().isDeleted()) {
                testTypeRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted test type of the same name still exists." +
                        " Please try again using a different name"));
            } else if (!testType.get().getId().equals(input.getId())) {
                testTypeRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Test type already exists"));
            }
        }
        TestType entity = testTypeRepository.find(input.getId());
        if (entity == null) {
            testTypeRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        testTypeRepository.enableFilter();
        testTypeMapper.mapDto(input, entity);
        testTypeRepository.save(entity);
        return Responses.ok();
    }
}
