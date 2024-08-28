package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.GenerateReportRequest;
import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.ReportType;
import com.ericsson.cifwk.tm.application.services.ReportingService;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReportObjectMapper;
import com.ericsson.cifwk.tm.presentation.dto.view.ReportTypeView;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.io.Resources;
import com.netflix.governator.annotations.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;


/**
 * @author ebuzdmi
 */
public class GenerateReportCommand implements Command<GenerateReportRequest> {

    private final Logger logger = LoggerFactory.getLogger(GenerateReportCommand.class);

    @Inject
    private ReportingService reportingService;

    @Inject
    private ReportObjectMapper reportObjectMapper;

    @Configuration("reporting.testExecution.paragraph.text")
    private String referenceToAttachTests;

    @Configuration("reporting.testCases.paragraph.text")
    private String referenceToAttachTestCases;

    @Configuration("reporting.template.testExecution")
    private String testExecutionTemplate;

    @Configuration("reporting.template.testCases")
    private String testCasesTemplate;

    @Override
    public Response apply(GenerateReportRequest input) {
        final  long testPlanId = input.getTestPlanId();
        final String extension = input.getExtension();
        final String queryString = input.getQuery();
        final Class view = input.getRequestView();
        final Class reportTypeView = input.getRequestTypeView();

        try {
            final ReportType outputFormat = ReportType.valueOf(extension.toUpperCase());

            StreamingOutput streamingOutput = new StreamingOutput() {
                @Override
                public void write(OutputStream output) throws IOException {
                    generateReport(output, reportTypeView, queryString, view, outputFormat, testPlanId);
                }
            };

            return Response.ok(streamingOutput)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = report." + extension.toLowerCase())
                    .header(HttpHeaders.CONTENT_TYPE, outputFormat.getMime())
                    .build();

        } catch (IllegalArgumentException e) {
            throw new BadRequestException(Responses.badRequest("Unknown format: " + extension));
        }
    }

    private void generateReport(OutputStream output, Class reportTypeView, String queryString,
                                Class view, ReportType outputFormat, long testPlanId) {
        try {
            List<ReportObject> listOfReportObj;
            if (ReportTypeView.TestCase.class.equals(reportTypeView)) {
                URI specificTestCaseTemplate = Resources.getResource(testCasesTemplate).toURI();
                listOfReportObj = reportObjectMapper.mapSpecificTestCases(queryString, view);

                reportingService.generateReportFromTemplate(outputFormat, output, listOfReportObj,
                        specificTestCaseTemplate, referenceToAttachTestCases);
            } else {
                URI documentTemplate = Resources.getResource(testExecutionTemplate).toURI();
                if (testPlanId !=   0) {
                    listOfReportObj = reportObjectMapper.mapTestCasesForCampaigns(queryString, view, testPlanId);
                } else {
                    listOfReportObj = reportObjectMapper.mapTestCases(queryString, view);
                }
                reportingService.generateReportFromTemplate(outputFormat, output, listOfReportObj, documentTemplate,
                        referenceToAttachTests);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
