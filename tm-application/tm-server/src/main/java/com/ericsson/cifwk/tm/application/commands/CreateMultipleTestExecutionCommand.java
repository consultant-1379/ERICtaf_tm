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
import com.ericsson.cifwk.tm.application.requests.CreateTestExecutionRequest;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.application.services.DefectService;
import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.domain.events.TrsUpdateEvent;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.IsoRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestExecutionMapper;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

public class CreateMultipleTestExecutionCommand implements Command<List<CreateTestExecutionRequest>> {

    @Inject
    private TestExecutionMapper testExecutionMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private DefectService defectService;

    @Inject
    private RequirementService requirementService;

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Inject
    private TestCampaignItemRepository testCampaignItemRepository;

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private IsoRepository isoRepository;

    @Inject
    private EventBus eventBus;

    @Override
    public Response apply(List<CreateTestExecutionRequest> request) {
        TestCampaign testCampaign = null;
        List<TestExecution> entities = Lists.newArrayList();
        List<TestExecutionInfo> dtos = Lists.newArrayList();

        for (CreateTestExecutionRequest input : request) {
            long testPlanId = input.getTestPlanId();
            String testCaseId = input.getTestCaseId();
            TestExecutionInfo testExecutionInfo = input.getTestExecutionInfo();

            if (hasDefectsWithResultPass(testExecutionInfo)) {
                throw new BadRequestException(
                        Responses.badRequest("Test Execution shouldn't have any Defects when result is 'Pass'"));
            }

            TestCampaignItem testCampaignItem = testCampaignItemRepository.findByTestPlanAndTestCase(testPlanId, testCaseId);

            if (testCampaign == null) {
                testCampaign = testCampaignItem.getTestCampaign();
            }

            if (testCampaignItem == null) {
                throw new BadRequestException(Responses.badRequest("Could not find testPlanItem data"));
            }

            TestExecution testExecution = testExecutionMapper.mapDto(testExecutionInfo, new TestExecution());

            testExecution.setTestCampaign(testCampaignItem.getTestCampaign());
            testExecution.setTestCaseVersion(testCampaignItem.getTestCaseVersion());
            testExecution.setAuthor(getUserByExternalId(testExecutionInfo.getAuthor()));
            testExecution.addDefects(defectService.findAll(testExecutionInfo.getDefectIds()));
            testExecution.addRequirements(requirementService.findAllOrImport(testExecutionInfo.getRequirementIds()));
            if (testExecutionInfo.getIso() != null) {
                testExecution.setIso(findIso(testExecutionInfo.getIso()));
            }

            testExecutionRepository.save(testExecution);
            entities.add(testExecution);

            dtos.add(testExecutionMapper.mapEntity(testExecution, TestExecutionInfo.class));
        }
        notifyRecordsToBeUpdated(testCampaign, entities);

        return Responses.created(dtos);
    }

    private boolean hasDefectsWithResultPass(TestExecutionInfo testExecutionInfo) {
        ReferenceDataItem result = testExecutionInfo.getResult();
        Set<String> defectIds = testExecutionInfo.getDefectIds();
        return !defectIds.isEmpty() && TestExecutionResult.PASS.getId().toString().equals(result.getId());
    }

    User getUserByExternalId(String externalId) {
        if (Strings.isNullOrEmpty(externalId)) {
            return userRepository.find(
                    userSessionService.getCurrentUser().getId());
        }
        return userRepository.findByExternalId(externalId);
    }

    private ISO findIso(IsoInfo dto) {
        return isoRepository.find(dto.getId());
    }

    private void notifyRecordsToBeUpdated(TestCampaign testCampaign, List<TestExecution> testExecutions) {
        TrsUpdateEvent trsUpdateEvent = new TrsUpdateEvent(testCampaign, testExecutions);
        eventBus.post(trsUpdateEvent);
    }

}
