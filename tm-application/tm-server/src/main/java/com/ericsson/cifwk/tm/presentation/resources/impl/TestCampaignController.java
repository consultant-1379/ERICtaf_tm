/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;


import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.AttachTestCasesCommand;
import com.ericsson.cifwk.tm.application.commands.CreateTestCampaignCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteTestCampaignCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTestCampaignCommand;
import com.ericsson.cifwk.tm.application.commands.CreateTestExecutionCommand;
import com.ericsson.cifwk.tm.application.commands.CreateMultipleTestExecutionCommand;
import com.ericsson.cifwk.tm.application.commands.ChangeLockStateCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateTestCaseVersionCommand;
import com.ericsson.cifwk.tm.application.commands.CopyTestCampaignsCommand;
import com.ericsson.cifwk.tm.application.commands.GenerateReportCommand;
import com.ericsson.cifwk.tm.application.params.TestCampaignCriteria;
import com.ericsson.cifwk.tm.application.params.TestCampaignCriteriaBuilder;
import com.ericsson.cifwk.tm.application.queries.TestCampaignQuerySet;
import com.ericsson.cifwk.tm.application.requests.AttachTestCasesRequest;
import com.ericsson.cifwk.tm.application.requests.ChangeLockStateRequest;
import com.ericsson.cifwk.tm.application.requests.CreateTestExecutionRequest;
import com.ericsson.cifwk.tm.application.requests.UpdateTestCaseVersionRequest;
import com.ericsson.cifwk.tm.application.requests.GenerateReportRequest;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.RequestPreconditions;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestInfoList;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportTypeViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.resources.TestCampaignResource;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;


@Controller
public class TestCampaignController implements TestCampaignResource {

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateTestCampaignCommand createTestCampaignCommand;

    @Inject
    private UpdateTestCampaignCommand updateTestCampaignCommand;

    @Inject
    private DeleteTestCampaignCommand deleteTestCampaignCommand;

    @Inject
    private AttachTestCasesCommand attachTestCasesCommand;

    @Inject
    private CreateTestExecutionCommand createTestExecutionCommand;

    @Inject
    private CreateMultipleTestExecutionCommand createMultipleTestExecutionCommand;

    @Inject
    private ChangeLockStateCommand changeLockStateCommand;

    @Inject
    private UpdateTestCaseVersionCommand updateTestCaseVersionCommand;

    @Inject
    private CopyTestCampaignsCommand copyTestCampaignsCommand;

    @Inject
    private TestCampaignQuerySet testCampaignQuerySet;

    @Inject
    private TestCampaignViewFactory testCampaignViewFactory;

    @Inject
    private TestCampaignItemViewFactory testCampaignItemViewFactory;

    @Inject
    private TestExecutionViewFactory testExecutionViewFactory;

    @Context
    private UriInfo uriInfo;

    @Inject
    private ReportViewFactory reportViewFactory;

    @Inject
    private ReportTypeViewFactory reportTypeViewFactory;

    @Inject
    private GenerateReportCommand generateReportCommand;


    @Override
    public Response getTestPlan(long testPlanId, String view) {
        Class<? extends DtoView<TestCampaignInfo>> dtoView = testCampaignViewFactory.getByName(view);
        return testCampaignQuerySet.getTestPlan(testPlanId, dtoView);
    }

    @Override
    public Response getTestCampaigns(String q,
                                     Long productId, Long dropId, List<Long> featureIds, List<Long> componentIds,
                                     int page, int perPage,
                                     String orderBy, String orderMode,
                                     String view) {

        Class<? extends DtoView<TestCampaignInfo>> dtoView = testCampaignViewFactory.getByName(view);
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);

        TestCampaignCriteria criteria = new TestCampaignCriteriaBuilder()
                .withQuery(query)
                .withProduct(productId)
                .withDrop(dropId)
                .withFeatures(featureIds)
                .withComponents(componentIds)
                .build();

        return testCampaignQuerySet.
                getTestCampaigns(criteria, page, perPage, uriInfo, dtoView);
    }

    @Override
    public Response getCompletion(String search, int limit) {
        return testCampaignQuerySet.getCompletion(search, 1, limit);
    }

    @Override
    public Response create(TestCampaignInfo testCampaignInfo) {
        RequestPreconditions.checkArgument(testCampaignInfo.getId() == null, "Given test campaign has already been created");
        return commandProcessor.process(createTestCampaignCommand, testCampaignInfo);
    }

    @Override
    public Response update(long testPlanId, TestCampaignInfo testCampaignInfo) {
        RequestPreconditions.checkArgument(testPlanId == testCampaignInfo.getId(), "Test campaign ids in URL path and request body do not match");
        return commandProcessor.process(updateTestCampaignCommand, testCampaignInfo);
    }

    @Override
    public Response delete(long testPlanId) {
        return commandProcessor.process(deleteTestCampaignCommand, testPlanId);
    }

    @Override
    public Response copyTestCampaigns(CopyTestCampaignsRequest request) {
        return commandProcessor.process(copyTestCampaignsCommand, request);
    }

    @Override
    public Response getTestCases(long testPlanId, String q) {
        return testCampaignQuerySet.getTestCases(testPlanId, q);
    }

    @Override
    public Response attachTestCases(long testPlanId, TestInfoList testInfoList) {
        AttachTestCasesRequest request = new AttachTestCasesRequest(testPlanId, testInfoList);
        return commandProcessor.process(attachTestCasesCommand, request);
    }

    @Override
    public Response updateTestCaseVersion(long testPlanId, long testCaseId) {
        UpdateTestCaseVersionRequest request = new UpdateTestCaseVersionRequest(testPlanId, testCaseId);
        return commandProcessor.process(updateTestCaseVersionCommand, request);
    }

    @Override
    public Response getTestCaseVersion(long testPlanId, String testCaseId, String view) {
        Class<? extends DtoView<TestCampaignItemInfo>> dtoView = testCampaignItemViewFactory.getByName(view);
        return testCampaignQuerySet.getTestCase(testPlanId, testCaseId, dtoView);
    }

    @Override
    public Response getTestExecutions(long testPlanId, String testCaseId, int page, int perPage, String view) {
        Class<? extends DtoView<TestExecutionInfo>> dtoView = testExecutionViewFactory.getByName(view);
        return testCampaignQuerySet.getTestExecutions(testPlanId, testCaseId, page, perPage, uriInfo, dtoView);
    }

    @Override
    public Response getLatestTestExecutions(long testPlanId, String view) {
        Class<? extends DtoView<TestExecutionInfo>> dtoView = testExecutionViewFactory.getByName(view);
        return testCampaignQuerySet.getTestExecutions(testPlanId, dtoView);
    }

    @Override
    public Response createMultipleTestExecutions(long testPlanId, List<TestExecutionInfo> testExecutionInfos) {
        List<CreateTestExecutionRequest> testExecutionRequests = Lists.newArrayList();
        for (TestExecutionInfo testExecutionInfo : testExecutionInfos) {

            if (testExecutionInfo.getId() != null) {
                return Responses.badRequest("Given test execution has already been created");
            }
            CreateTestExecutionRequest request =
                    new CreateTestExecutionRequest(testPlanId, testExecutionInfo.getTestCase().toString(),
                            testExecutionInfo);
            testExecutionRequests.add(request);
        }
        return commandProcessor.process(createMultipleTestExecutionCommand, testExecutionRequests);
    }

    @Override
    public Response createTestExecution(long testPlanId, String testCaseId, TestExecutionInfo testExecutionInfo) {
        Preconditions.checkArgument(testExecutionInfo.getId() == null, "Given test execution has already been created");
        CreateTestExecutionRequest request = new CreateTestExecutionRequest(testPlanId, testCaseId, testExecutionInfo);
        return commandProcessor.process(createTestExecutionCommand, request);
    }

    @Override
    public Response lockTestPlan(long testPlanId, TestCampaignInfo testCampaignInfo) {
        ChangeLockStateRequest request = new ChangeLockStateRequest(testPlanId, testCampaignInfo.isLocked());
        return commandProcessor.process(changeLockStateCommand, request);
    }

    @Override
    public Response getTestCaseCSV(long testPlanId, String filter) {
        return testCampaignQuerySet.getTestCaseCSV(testPlanId, filter);
    }

    @Override
    public Response getTestCaseGAT(final long testPlanId, final String query, final String extension, final String view, final String reportType) {
        Class requestView = reportViewFactory.getByName(view);
        Class requestTypeView = reportTypeViewFactory.getByName(reportType);
        GenerateReportRequest request = new GenerateReportRequest(query, extension, requestView, requestTypeView, testPlanId);
        return commandProcessor.process(generateReportCommand, request);

    }

}
