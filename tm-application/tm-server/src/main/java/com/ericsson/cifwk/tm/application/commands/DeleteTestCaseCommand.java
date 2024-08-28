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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteTestCaseCommand implements Command<Long> {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Override
    public Response apply(Long input) {
        TestCase entity = testCaseRepository.find(input);
        if (entity == null || entity.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        entity.delete();
        return Responses.noContent();
    }

}
