/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.common.ReportDtoFactory;
import com.ericsson.cifwk.tm.application.services.ReportObject;
import com.ericsson.cifwk.tm.application.services.ReportType;
import com.ericsson.cifwk.tm.application.services.impl.ReportingServiceImpl;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.docx4j.TextUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;

public class ReportingServiceImplTest {

    @InjectMocks
    private ReportingServiceImpl service;

    private static final String TEST_CASE_HEADER = "Test Cases";


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        service.configure();
    }

    @Test
    public void testGenerateReport() throws Docx4JException {

        String title = "test title";
        String description = "test description";
        String precondition = "test precondition";

        List<LinkedHashMap> listofReports = ReportDtoFactory.createReportData();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        service.generateReport(ReportType.DOCX, output, getReportObject(listofReports));

        String result = extractPlainText(WordprocessingMLPackage.load(
                new ByteArrayInputStream(output.toByteArray())).getMainDocumentPart().getJaxbElement());

        assertThat(result, allOf(containsString(title),
                containsString("description" + description),
                containsString("precondition" + precondition),
                containsString("Test Step 1: " + "testStep"),
                containsString("Verify Step 1.1: " + "verifyStep"),
                containsString("Verify Step 1.2: " + "verifyStep")));
    }

    @Test
    public void testGenerateReportFromTemplate() throws Docx4JException, URISyntaxException {

        String title = "test title";
        String description = "test description";
        String precondition = "test precondition";

        final URI documentTemplate = Resources.getResource("reporting/TestTemplate.docx").toURI();

        List<LinkedHashMap> listofReports = ReportDtoFactory.createReportData();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        service.generateReportFromTemplate(ReportType.DOCX, output, getReportObject(listofReports),
                documentTemplate, TEST_CASE_HEADER);

        String result = extractPlainText(WordprocessingMLPackage.load(
                new ByteArrayInputStream(output.toByteArray())).getMainDocumentPart().getJaxbElement());

        int index = result.indexOf(TEST_CASE_HEADER) + TEST_CASE_HEADER.length();
        String position = result.substring(index);

        assertThat(position, startsWith(title)); // check position of added data


        assertThat(result, allOf(containsString(title),
                containsString("description" + description),
                containsString("precondition" + precondition),
                containsString("Test Step 1: " + "testStep"),
                containsString("Verify Step 1.1: " + "verifyStep"),
                containsString("Verify Step 1.2: " + "verifyStep")));
    }



    @Test
    public void testPagingMultiple() throws Docx4JException {

        String title = "test title";
        String description = "test description";
        String precondition = "test precondition";


        LinkedHashMap<String, String> headers = new LinkedHashMap();
        headers.put("title", title);
        headers.put("description", description);
        headers.put("precondition", precondition);

        List<LinkedHashMap> listofReports = Lists.newArrayList();
        listofReports.add(headers);
        listofReports.add(headers);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        service.generateReport(ReportType.DOCX, output, getReportObject(listofReports));

        String result = extractPlainText(WordprocessingMLPackage.load(
                new ByteArrayInputStream(output.toByteArray())).getMainDocumentPart().getJaxbElement());

        assertThat(count(title, result), equalTo(listofReports.size()));
    }

    @Test
    public void testPrintTestCaseNulls() throws JAXBException, Docx4JException {
        String title = null;
        String description = null;
        String precondition = null;


        LinkedHashMap<String, String> headers = new LinkedHashMap();
        headers.put("title", title);
        headers.put("description", description);
        headers.put("precondition", precondition);

        List<LinkedHashMap> listofReports = Lists.newArrayList();
        listofReports.add(headers);
        listofReports.add(headers);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        service.generateReport(ReportType.DOCX, output, getReportObject(listofReports));

        String result = extractPlainText(WordprocessingMLPackage.load(
                new ByteArrayInputStream(output.toByteArray())).getMainDocumentPart().getJaxbElement());

        assertThat(result, allOf(
                containsString("description"),
                containsString("precondition")));
    }

    @Test
    public void testPrintTestCaseSpecialCharacters() throws JAXBException, Docx4JException {
        String title = "Launch PCI<> UI";
        String description = "PCI launcher&&";
        String precondition = "<enter any setup steps needed before the test case is run>";

        LinkedHashMap<String, String> headers = new LinkedHashMap();
        headers.put("title", service.prepareString(title));
        headers.put("description", service.prepareString(description));
        headers.put("precondition", service.prepareString(precondition));

        List<LinkedHashMap> listOfReports = Lists.newArrayList();
        listOfReports.add(headers);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        service.generateReport(ReportType.DOCX, output, getReportObject(listOfReports));

        String result = extractPlainText(WordprocessingMLPackage.load(
                new ByteArrayInputStream(output.toByteArray())).getMainDocumentPart().getJaxbElement());

        assertThat(result, allOf(containsString(title),
                containsString("description" + description),
                containsString("precondition" + precondition),
                containsString("Test Step 1: " + "testStep"),
                containsString("Verify Step 1.1: " + "verifyStep")));
    }

    private String extractPlainText(Object output) {
        try {
            StringWriter writer = new StringWriter();
            TextUtils.extractText(output, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int count(String what, String where) {
        return Iterables.size(Splitter.on(what).split(where)) - 1;
    }

    private List<ReportObject> getReportObject(List<LinkedHashMap> listofReports) {
        List<ReportObject> reportObjects = Lists.newArrayList();
        for (LinkedHashMap<String, String> headers: listofReports) {

            ReportObject parent = new ReportObject();
            parent.addHeaderInfo(headers);

            parent.addChild(getTestStep());
            parent.addChild(getTestStep());

            reportObjects.add(parent);
        }
        return reportObjects;
    }

    private ReportObject getTestStep() {
        ReportObject testStep = new ReportObject();
        for (int testStepIndex = 1; testStepIndex <= 1; testStepIndex++) {
            testStep.setName("Test Step " + testStepIndex + ": ");
            testStep.setValue("testStep");
            for (int i = 1; i <= 2; i++) {
                testStep.addChild(getVerifyStep(i));
            }
        }

        return testStep;
    }

    private ReportObject getVerifyStep(int index) {
        ReportObject testStep = new ReportObject();
        testStep.setName("Verify Step 1." + index + ": ");
        testStep.setValue("verifyStep");
        return testStep;
    }

}
