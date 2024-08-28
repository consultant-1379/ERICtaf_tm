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
import com.ericsson.cifwk.tm.application.requests.AttachTestCasesRequest;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.presentation.dto.SimpleTestCaseInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Sets;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

public class AttachTestCasesCommand implements Command<AttachTestCasesRequest> {

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    @Override
    public Response apply(AttachTestCasesRequest input) {
        long testPlanId = input.getTestPlanId();
        List<SimpleTestCaseInfo> testCases = input.getTestInfoList().getTestCases();

        TestCampaign entity = testCampaignRepository.find(testPlanId);
        if (entity.isLocked()) {
            throw new BadRequestException(
                    Responses.badRequest("Attaching test cases is not allowed for locked test plan."));
        }
        Set<Long> existingTestPlanItems = Sets.newHashSet();
        for (TestCampaignItem existingTestCampaignItem : entity.getTestCampaignItems()) {
            existingTestPlanItems.add(existingTestCampaignItem.getTestCaseVersion().getId());
        }

        Set<Long> ids = Mapping.ids(testCases);
        Long[] idsArray = ids.toArray(new Long[ids.size()]);

        TestCase[] testCasesToAttach = testCaseRepository.find(idsArray);

        for (TestCase testCase : testCasesToAttach) {
            TestCaseVersion currentVersion = testCase.getCurrentVersion();
            if (!existingTestPlanItems.contains(currentVersion.getId())) {
                TestCampaignItem testCampaignItem = new TestCampaignItem();
                testCampaignItem.setTestCaseVersion(currentVersion);
                entity.addTestCampaignItem(testCampaignItem);
            }
        }

        return Responses.noContent();
    }

}
