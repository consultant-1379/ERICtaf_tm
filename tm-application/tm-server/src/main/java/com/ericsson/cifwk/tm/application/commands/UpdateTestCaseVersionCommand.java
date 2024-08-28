package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.UpdateTestCaseVersionRequest;
import com.ericsson.cifwk.tm.domain.helper.UserHelper;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 *
 */
public class UpdateTestCaseVersionCommand implements Command<UpdateTestCaseVersionRequest> {

    @Inject
    private TestCampaignItemRepository testCampaignItemRepository;

    @Inject
    private UserHelper userHelper;

    @Override
    public Response apply(UpdateTestCaseVersionRequest input) {
        long testPlanId = input.getTestPlanId();
        String testCaseId = Long.toString(input.getTestCaseId());
        TestCampaignItem testCampaignItem = testCampaignItemRepository.findByTestPlanAndTestCase(testPlanId,
                testCaseId);

        TestCaseVersion latestVersion = getLatestTestCaseVersion(testCampaignItem);
        testCampaignItem.setTestCaseVersion(latestVersion);

        return Responses.ok();
    }

    private TestCaseVersion getLatestTestCaseVersion(TestCampaignItem testCampaignItem) {
        TestCaseVersion testCaseVersion = testCampaignItem.getTestCaseVersion();
        TestCase testCase = testCaseVersion.getTestCase();
        userHelper.setUpdateUser(testCase);
        return testCase.getCurrentVersion();
    }

}
