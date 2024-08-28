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
import com.ericsson.cifwk.tm.application.services.TestCaseNotificationService;
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class UpdateTestCaseCommand implements Command<TestCaseInfo> {

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private UserHelper userHelper;

    @Inject
    private TestCaseNotificationService notificationService;

    @Override
    public Response apply(TestCaseInfo input) {
        TestCase entity = testCaseRepository.find(input.getId());
        if (entity == null) {
            throw new NotFoundException(Responses.notFound());
        }
        testCaseRepository.lock(entity);

        Optional<TestCase> byExternalId = Optional.ofNullable(testCaseRepository.findByExternalId(input.getTestCaseId()));
        if (byExternalId.isPresent()) {
            TestCase deletedTestCase = testCaseRepository.find(byExternalId.get().getId());
            if (deletedTestCase.isDeleted()) {
                throw new BadRequestException(Responses.badRequest("Test case ID was deleted and can no longer be used. " +
                        "Please recreate the test case to remove from deleted state."));
            }
        }

        testCaseMapper.mapDto(input, entity);
        TestCaseVersion currentVersion = entity.getCurrentVersion();
        try {
            testCaseService.populate(currentVersion, input);
        } catch (ServiceValidationException e) {
            throw new BadRequestException(Responses.badRequest(e.getMessage(), e.getDeveloperMessage()));
        }
        userHelper.setUpdateUser(entity);
        testCaseRepository.save(entity);
        TestCaseInfo dto = testCaseMapper.mapEntity(entity, TestCaseInfo.class, TestCaseView.Detailed.class);
        String currentUserId = userHelper.getCurrentUser().getUserId();
        notificationService.notifySubscribers(entity, currentUserId);
        return Responses.ok(dto);
    }

}
