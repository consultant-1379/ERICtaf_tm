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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteTestTypeCommand implements Command<Long> {

    @Inject
    private TestTypeRepository testTypeRepository;

    @Override
    public Response apply(Long input) {

        TestType testType = testTypeRepository.find(input);
        if (testType == null || testType.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        testType.delete();
        return Responses.noContent();
    }

}
