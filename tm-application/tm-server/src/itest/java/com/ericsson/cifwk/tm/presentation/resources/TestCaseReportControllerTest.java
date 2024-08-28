/**
 * ****************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * <p>
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 * ****************************************************************************
 */

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.FieldType;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.docx4j.TextUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TestCaseReportControllerTest extends BaseControllerLevelTest {

    public static final String TEST_CASE_REPORTS_URL_DOC = "/tm-server/api/test-cases.docx";

    private static final String PROJECT_1 = "Project1";
    private static final String REQUIREMENT_ID1 = "CIP-4200";
    private static final String REQUIREMENT_ID2 = "CIP-4177";
    private static final String TEST_CASE_TITLE = "The Test Case";
    private static final String TEST_CASE_TYPE = "Performance";

    private Scope scope;
    private Project project;
    private TechnicalComponent component;
    private Product product;
    private ProductFeature feature;
    private TestType testType;
    private URL fileResource;

    public final String TEST_QUERY = "any~Test";
    public final String TEST_BAD_QUERY = "any~ImNotGoingToSeeThis";

    private static final MediaType[] ACCEPTED = {MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.WILDCARD_TYPE};
    public static final String TEST_CASES_URL = "/tm-server/api/test-cases/";
    public static final String FILES_URL = "/tm-server/api/files/";
    public static final String FILE_NAME = "iTest.txt";
    public static final String FILE_CONTENTS = "I live here";

    @Before
    public void setUp() {
        project = fixture().persistProject(PROJECT_1);
        product = project.getProduct();
        scope = fixture().persistScope("Acceptance", product);
        feature = fixture().persistFeature("ENM_FM", product);
        testType = fixture().persistTestType(TEST_CASE_TYPE, product);
        component = fixture().persistTechnicalComponent("Component", feature);

        fileResource = getClass().getClassLoader().getResource("fileStorage/" + FILE_NAME);
    }

    @Test
    public void testGenerateReport() throws IOException, Docx4JException, OutOfMemoryError {
        createDefaultTestCase();

        Response response = app.client().path(TEST_CASE_REPORTS_URL_DOC)
                .queryParam("q", TEST_QUERY)
                .request().get();
        assertStatus(response, Response.Status.OK);

        InputStream entity = response.readEntity(InputStream.class);
        
        String result = extractPlainText(WordprocessingMLPackage.load(entity).getMainDocumentPart().getJaxbElement());

        assertThat(result, containsString("Action 1"));
        assertThat(result, containsString("Result 1.1"));
        assertThat(result, containsString("Result 1.2"));

        assertThat(result, containsString("Action 2"));
        assertThat(result, containsString("Result 2.1"));
        assertThat(result, containsString("Result 2.2"));

    }

    @Test
    public void testGenerateReportWithNoTestCases() throws IOException, Docx4JException {
        createDefaultTestCase();

        Response response = app.client().path(TEST_CASE_REPORTS_URL_DOC)
                .queryParam("q", TEST_BAD_QUERY)
                .request().get();
        assertStatus(response, Response.Status.OK);

        InputStream entity = response.readEntity(InputStream.class);

        String result = extractPlainText(WordprocessingMLPackage.load(entity).getMainDocumentPart().getJaxbElement());

        assertThat(result, not(containsString("Test Step 1")));
        assertThat(result, not(containsString("Verify Step 1.1")));
        assertThat(result, not(containsString("Verify Step 1.2")));

        assertThat(result, not(containsString("Test Step 2")));
        assertThat(result, not(containsString("Verify Step 2.1")));
        assertThat(result, not(containsString("Verify Step 2.2")));

    }

    @Test
    public void testGenerateReportWithFile() throws IOException, Docx4JException, OutOfMemoryError, URISyntaxException {
        String title = "FileTest";
        String author = "tafuser";
        TestCase testCase = createTestCase(title);
        app.persistence().persistInTransaction(testCase);

        File fileToUpload = new File(fileResource.toURI());

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        multiPart.bodyPart(new FormDataBodyPart("author", author));

        String product = testCase.getCurrentVersion().getProductFeature().getProduct().getName();
        String path = FILES_URL + product + "/test-cases/" + testCase.getId();

        Response createTestCaseResponse = app.client()
                .path(path)
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));

        assertStatus(createTestCaseResponse, Response.Status.CREATED);

        Response response = app.client().path(TEST_CASE_REPORTS_URL_DOC)
                .queryParam("q", "any~" + title)
                .queryParam("view", "detailed")
                .request().get();
        assertStatus(response, Response.Status.OK);

        InputStream entity = response.readEntity(InputStream.class);
        String result = extractPlainText(WordprocessingMLPackage.load(entity).getMainDocumentPart().getJaxbElement());

        assertThat(result, containsString(FILE_NAME));
        assertThat(result, containsString(FILE_CONTENTS));
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

    private TestCase createDefaultTestCase() {
        TestCase testCase = createTestCase(TEST_CASE_TITLE);
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        Requirement requirement1 = fixture().persistRequirement(REQUIREMENT_ID1, project);
        Requirement requirement2 = fixture().persistRequirement(REQUIREMENT_ID2, project);
        testCaseVersion.addRequirement(requirement1);
        testCaseVersion.addRequirement(requirement2);
        testCaseVersion.setProductFeature(feature);
        testCaseVersion.setTestCaseStatus(TestCaseStatus.PRELIMINARY);
        app.persistence().persistInTransaction(requirement1, requirement2, testCase);
        return testCase;
    }

    private TestCase createTestCase(String title) {
        TestCase testCase = TestEntityFactory
                .buildTestCase()
                .withTestCaseId(title)
                .withTitle(title)
                .withProductFeature(feature)
                .withType(testType)
                .withDescription(title)
                .withPriority(Priority.NORMAL)
                .addScope(scope)
                .addTechnicalComponent(component)
                .withOptionalField(createComponentField(component.getName()))
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();
        TestCaseVersion result = testCase.getCurrentVersion();
        TestStep testStep1 = createTestStep("testStep1");
        TestStep testStep2 = createTestStep("testStep2");
        result.addTestStep(testStep1);
        result.addTestStep(testStep2);

        return testCase;
    }

    private TestField createComponentField(String componentName) {
        return createOptionalField(TestField.COMPONENT, componentName);
    }

    private TestField createOptionalField(String name, String value) {
        return TestEntityFactory.buildTestField()
                .withFieldType(FieldType.STRING)
                .withName(name)
                .withValue(value)
                .build();
    }

    private TestStep createTestStep(String name) {
        TestStep testStep = new TestStep();
        testStep.setTitle(name);
        testStep.addVerification(createVerification(name + "Verification1"));
        testStep.addVerification(createVerification(name + "Verification2"));
        return testStep;
    }

    private VerifyStep createVerification(String name) {
        VerifyStep verifyStep = new VerifyStep();
        verifyStep.setVerifyStep(name);
        return verifyStep;
    }

}
