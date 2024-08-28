/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries.helper;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.integration.fileStorage.FileService;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static com.ericsson.cifwk.tm.files.FileCategory.TEST_EXECUTION_FILE;

public class TestCampaignQuerySetHelper {

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Inject
    private FileService fileService;

    public void updateFileInfo(Long testCampaignId, Set<TestCampaignItemInfo> testCampaignItemInfos) {
        testCampaignItemInfos.stream()

                .forEach(testCampaignItemInfo -> {
                        String testCaseId = testCampaignItemInfo.getTestCase().getTestCaseId();
                        Optional<TestExecution> testExecutionId = Optional.ofNullable(
                            testExecutionRepository.findLatestByTestPlanAndTestCase(testCampaignId, testCaseId));
                        boolean filesAttached = false;
                        if (testExecutionId.isPresent()) {
                            filesAttached = fileService.getFilesAttached(
                                testExecutionId.get().getId(), TEST_EXECUTION_FILE);

                        }
                        testCampaignItemInfo.setFileAttached(filesAttached);
                    });

    }


}
