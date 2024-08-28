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
import com.ericsson.cifwk.tm.application.services.validation.ExecutionValidation;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.events.TrsUpdateEvent;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.IsoRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
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
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTestExecutionCommand implements Command<CreateTestExecutionRequest> {

    private static final String EXECUTION_TIME_REGEX = "^([0-9]+):([0-5][0-9]):([0-5][0-9])$";

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
    private ExecutionValidation executionValidation;

    @Inject
    private IsoRepository isoRepository;

    @Inject
    private EventBus eventBus;

    @Override
    public Response apply(CreateTestExecutionRequest input) {
        long testPlanId = input.getTestPlanId();
        String testCaseId = input.getTestCaseId();
        TestExecutionInfo testExecutionInfo = input.getTestExecutionInfo();

        if (hasDefectsWithResultPass(testExecutionInfo)) {
            throw new BadRequestException(
                    Responses.badRequest("Test Execution shouldn't have any Defects when result is 'Pass'"));
        }
        if (hasBadExecutionTimeFormat(testExecutionInfo)) {
            throw new BadRequestException(Responses.badRequest("Test execution time format is incorrect (HH:MM:SS)"));

        }

        List<Requirement> requirements = requirementService.findAllOrImport(testExecutionInfo.getRequirementIds());
        if (!requirements.isEmpty()) {
            try {
                executionValidation.validateRequirementsTypes(requirements);
            } catch (ServiceValidationException e) {
                throw new BadRequestException(Responses.badRequest(e.getMessage(), e.getDeveloperMessage()));
            }
        }

        TestCampaignItem testCampaignItem = testCampaignItemRepository.findByTestPlanAndTestCase(testPlanId,
                testCaseId);

        TestExecution testExecution = testExecutionMapper.mapDto(testExecutionInfo, new TestExecution());

        testExecution.setTestCampaign(testCampaignItem.getTestCampaign());
        testExecution.setTestCaseVersion(testCampaignItem.getTestCaseVersion());
        testExecution.setAuthor(getUserByExternalId(testExecutionInfo.getAuthor()));
        testExecution.addDefects(defectService.findAll(testExecutionInfo.getDefectIds()));
        testExecution.addRequirements(requirements);
        if (testExecutionInfo.getIso() != null) {
            testExecution.setIso(findIso(testExecutionInfo.getIso()));
        }

        testExecutionRepository.save(testExecution);
        notifyRecordsToBeUpdated(testCampaignItem.getTestCampaign(), testExecution);

        TestExecutionInfo dto = testExecutionMapper.mapEntity(testExecution, TestExecutionInfo.class);

        return Responses.created(dto);
    }

    private boolean hasDefectsWithResultPass(TestExecutionInfo testExecutionInfo) {
        ReferenceDataItem result = testExecutionInfo.getResult();
        Set<String> defectIds = testExecutionInfo.getDefectIds();
        return !defectIds.isEmpty() && TestExecutionResult.PASS.getId().toString().equals(result.getId());
    }

    private boolean hasBadExecutionTimeFormat(TestExecutionInfo testExecutionInfo) {
        String executionTime = testExecutionInfo.getExecutionTime();
        if (!StringUtils.isEmpty(executionTime)) {
            Pattern pattern = Pattern.compile(EXECUTION_TIME_REGEX);
            Matcher matcher = pattern.matcher(executionTime);
            return !matcher.find();
        }
        return false;
    }

    User getUserByExternalId(String externalId) {
        if (Strings.isNullOrEmpty(externalId)) {
            return userRepository.find(
                    userSessionService.getCurrentUser().getId());
        }
        return userRepository.findByExternalId(externalId);
    }

    private ISO findIso(IsoInfo dto) {
        if (dto == null) {
            return null;
        }
        return isoRepository.find(dto.getId());
    }

    private void notifyRecordsToBeUpdated(TestCampaign testCampaign, TestExecution testExecution) {
        TrsUpdateEvent trsUpdateEvent = new TrsUpdateEvent(testCampaign, Lists.newArrayList(testExecution));
        eventBus.post(trsUpdateEvent);
    }

}
