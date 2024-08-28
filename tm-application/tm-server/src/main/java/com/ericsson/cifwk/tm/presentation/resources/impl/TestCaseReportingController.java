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
import com.ericsson.cifwk.tm.application.commands.GenerateReportCommand;
import com.ericsson.cifwk.tm.application.requests.GenerateReportRequest;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportTypeViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportViewFactory;
import com.ericsson.cifwk.tm.presentation.resources.TestCaseReportingResource;
import com.ericsson.cifwk.tm.application.queries.TestCaseQuerySet;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class TestCaseReportingController implements TestCaseReportingResource {

    @Inject
    private TestCaseQuerySet testCaseQuerySet;

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private ReportViewFactory reportViewFactory;

    @Inject
    private ReportTypeViewFactory reportTypeViewFactory;

    @Inject
    private GenerateReportCommand generateReportCommand;

    @Override
    public Response getTest(final String query, final String extension, final String view, final String reportType) {
        Class requestView = reportViewFactory.getByName(view);
        Class requestTypeView = reportTypeViewFactory.getByName(reportType);

        GenerateReportRequest request = new GenerateReportRequest(query, extension, requestView, requestTypeView, 0);
        return commandProcessor.process(generateReportCommand, request);
    }

    @Override
    public Response getTestCampaignGroupId(long testCampaignGroupId) {
        return testCaseQuerySet.generateDocReport(testCampaignGroupId);

    }

    @Override
    public Response getTestCampaignSubGroupId(long testCampaignGroupId, String testCampaignSubGroupId) {
        return testCaseQuerySet.generateDocSubReport(testCampaignGroupId, testCampaignSubGroupId);
    }

    @Override
    public Response getSovReport(long campaignId, long id, String testCampaignId) {
        return testCaseQuerySet.generateSovStatusReport(campaignId);
    }

    @Override
    public Response getSovSubReport(long testCampaignGroupId, long campaignId, long id, String testCampaignId) {
        return testCaseQuerySet.generateSovStatusSubReport(testCampaignGroupId, testCampaignId);
    }


}
