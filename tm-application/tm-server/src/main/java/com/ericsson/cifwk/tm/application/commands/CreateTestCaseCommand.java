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
import com.ericsson.cifwk.tm.application.services.TestCaseService;
import com.ericsson.cifwk.tm.application.services.TestCaseSubscriptionService;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class CreateTestCaseCommand implements Command<TestCaseInfo> {

    @Inject
    private TestCaseService testCaseService;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private UserHelper userHelper;

    @Inject
    private TestCaseSubscriptionService testCaseSubscriptionService;

    @Override
    public Response apply(TestCaseInfo input) {
        TestCase testCase = TestCaseFactory.createTestCase();
        testCase = setDeletedTestCaseToActive(input, testCase);

        testCaseMapper.mapDto(input, testCase);
        TestCaseVersion currentVersion = testCase.getCurrentVersion();

        try {
            currentVersion.setTestCaseStatus(TestCaseStatus.PRELIMINARY);
            testCaseService.populate(currentVersion, input);
        } catch (ServiceValidationException e) {
            throw new BadRequestException(Responses.badRequest(e.getMessage(), e.getDeveloperMessage()));
        }
        userHelper.setUpdateUser(testCase);
        testCaseRepository.persist(testCase);
        TestCaseInfo dto = testCaseMapper.mapEntity(testCase, TestCaseInfo.class, TestCaseView.Detailed.class);

        testCaseSubscriptionService.silentSubscribe(dto.getId().toString(), userHelper.getCurrentUser().getUserId());
        return Responses.created(dto);
    }

    private TestCase setDeletedTestCaseToActive(TestCaseInfo input, TestCase testCase) {
        testCaseRepository.disableFilter();
        Optional<TestCase> byExternalId = Optional.ofNullable(testCaseRepository.findByExternalId(input.getTestCaseId()));
        if (byExternalId.isPresent()) {
            TestCase deletedTestCase = testCaseRepository.find(byExternalId.get().getId());
            if (deletedTestCase.isDeleted()) {
                deletedTestCase.undelete();
                deletedTestCase.addNewMinorVersion();
                input.setId(deletedTestCase.getId()); //needed to be set for test case mapper
                testCaseRepository.enableFilter();
                return deletedTestCase;
            }
        }
        testCaseRepository.enableFilter();
        return testCase;
    }

}
