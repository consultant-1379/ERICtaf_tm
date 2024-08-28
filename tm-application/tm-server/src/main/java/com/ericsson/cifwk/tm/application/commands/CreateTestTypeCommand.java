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
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestTypeMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CreateTestTypeCommand implements Command<TestTypeInfo> {

    @Inject
    private TestTypeRepository testTypeRepository;

    @Inject
    private TestTypeMapper testTypeMapper;

    @Inject
    private ProductRepository productRepository;

    @Override
    public Response apply(TestTypeInfo input) {
        testTypeRepository.disableFilter();
        TestType entity = testTypeMapper.mapDto(input, TestType.class);
        Optional<TestType> testType = testTypeRepository.findByNameAndProductId(input.getName(), input.getProduct().getId());
        if (testType.isPresent()) {
            if (!testType.get().isDeleted()) {
                throw new BadRequestException(Responses.badRequest("Test type already exists"));
            } else {
                testType.get().undelete();
                testTypeRepository.enableFilter();
                TestTypeInfo dto = testTypeMapper.mapEntity(new TestTypeInfo(), entity);
                return Responses.created(dto);
            }
        }
        entity.setProduct(productRepository.find(entity.getProduct().getId()));
        testTypeRepository.persist(entity);
        TestTypeInfo dto = testTypeMapper.mapEntity(new TestTypeInfo(), entity);
        testTypeRepository.enableFilter();
        return Responses.created(dto);
    }

}
