package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

/**
 *
 */
public class CreateTestCaseVersionCommand implements Command<String> {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private TestCaseMapper testCaseMapper;

    @Inject
    private UserHelper userHelper;

    @Override
    public Response apply(String testCaseId) {
        TestCase testCase = testCaseRepository.findByAnyId(testCaseId);
        if (testCase == null) {
            throw new NotFoundException(Responses.notFound());
        }
        TestCaseVersion newVersion = testCase.addNewMinorVersion();

        userHelper.setUpdateUser(newVersion.getTestCase());
        newVersion.setTestCaseStatus(TestCaseStatus.PRELIMINARY);
        newVersion.setReviewGroup(null);
        testCaseRepository.persist(testCase);

        TestCaseInfo result = testCaseMapper.mapEntity(testCase, TestCaseInfo.class, TestCaseView.Detailed.class);
        return Responses.created(result);
    }
}
