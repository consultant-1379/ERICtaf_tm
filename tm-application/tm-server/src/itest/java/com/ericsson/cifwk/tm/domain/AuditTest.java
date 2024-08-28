/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.domain;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.AuditRevisionEntity;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AuditTest extends BaseServiceLayerTest {

    private AuditReader auditor;
    private String defaultDescription = "testCaseDescription";

    private Product product;
    private ProductFeature productFeature;
    private TestType testType;

    @Before
    public void setUp() {
        auditor = AuditReaderFactory.get(persistence.em());
        product = new Product();
        product.setExternalId("test");
        product.setName("test");

        productFeature = new ProductFeature();
        productFeature.setName("FM");
        productFeature.setProduct(product);

        testType = new TestType();
        testType.setName("Functional");
        testType.setProduct(product);

        persistence.persistInTransaction(product, testType, productFeature);
    }

    @Test
    public void testBasicAudit() {
        //Create
        TestCase testCase = getDefaultTestCase();
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        testCaseVersion.setProductFeature(productFeature);
        testCaseVersion.setType(testType);
        persistence.persistInTransaction(testCase);

        List<Number> revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersion.getId());
        assertThat(revisions, hasSize(1));

        //Update
        testCaseVersion.setDescription("newDescription");
        persistence.persistInTransaction(testCase);

        revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersion.getId());
        assertThat(revisions, hasSize(2));
        TestCaseVersion previousRevision = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(0));
        assertThat(previousRevision.getDescription(), equalTo(defaultDescription));

        //Remove
        testCaseVersion.delete();
        persistence.persistInTransaction(testCase);

        revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersion.getId());
        assertThat(revisions, hasSize(3));
        previousRevision = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(1));
        assertTrue(testCaseVersion.isDeleted());
        assertFalse(previousRevision.isDeleted());
    }

    @Test
    public void testAuditChild() {
        // Create
        TestCase testCase = getDefaultTestCase();
        TestCaseVersion testCaseVersionEntity = testCase.getCurrentVersion();
        testCaseVersionEntity.setProductFeature(productFeature);
        testCaseVersionEntity.setType(testType);

        TestStep testStepEntity = new TestStep();
        testCaseVersionEntity.addTestStep(testStepEntity);
        final String testStepExecute = "testDefaultTestStep";
        testStepEntity.setTitle(testStepExecute);

        VerifyStep verify = new VerifyStep();
        final String verifyString = "verify";
        verify.setVerifyStep(verifyString);
        testStepEntity.addVerification(verify);

        persistence.persistInTransaction(testCase);

        List<Number> revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersionEntity.getId());
        assertThat(revisions, hasSize(1));

        //Modify
        testStepEntity.setTitle("newExecute");
        verify.setVerifyStep("newVerifyStep");

        persistence.persistInTransaction(testCase);

        //Validate
        revisions = auditor.getRevisions(VerifyStep.class, verify.getId());
        assertThat(revisions, hasSize(2));
        VerifyStep previousVerify = auditor.find(VerifyStep.class, verify.getId(), revisions.get(0));
        assertThat(previousVerify.getVerifyStep(), equalTo(verifyString));

        revisions = auditor.getRevisions(TestStep.class, testStepEntity.getId());
        assertThat(revisions, hasSize(2));
        TestStep previousTestStep = auditor.find(TestStep.class, testStepEntity.getId(), revisions.get(0));
        assertThat(previousTestStep.getTitle(), equalTo(testStepExecute));
    }

    @Test
    public void testCustomAuditor() {
        TestCase testCase = getDefaultTestCase();
        TestCaseVersion testCaseVersionEntity = testCase.getCurrentVersion();
        testCaseVersionEntity.setProductFeature(productFeature);
        testCaseVersionEntity.setType(testType);
        persistence.persistInTransaction(testCase);

        List<Number> revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersionEntity.getId());

        AuditRevisionEntity revision = auditor.findRevision(AuditRevisionEntity.class, revisions.get(0));

        assertThat(revision, notNullValue());
    }

    @Test
    public void testChangeNotAudited() {
        Product product = fixture().persistProduct();
        TestType testType = fixture().persistTestType("type", product);
        ProductFeature feature = fixture().persistFeature("feature", product);
        final String technicalComponentName1 = "technicalComponent1";
        TechnicalComponent technicalComponent1 = fixture()
                .persistTechnicalComponent(technicalComponentName1, feature);
        final String technicalComponentName2 = "technicalComponent2";
        TechnicalComponent technicalComponent2 = fixture()
                .persistTechnicalComponent(technicalComponentName2, feature);

        TestCase testCase = getDefaultTestCase();
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        testCaseVersion.setProductFeature(productFeature);
        testCaseVersion.setType(testType);
        testCaseVersion.addTechnicalComponent(technicalComponent1);
        persistence.persistInTransaction(testCase);

        testCaseVersion.addTechnicalComponent(technicalComponent2);
        persistence.persistInTransaction(testCase);

        List<Number> revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersion.getId());
        assertThat(revisions, hasSize(2));

        TestCaseVersion previousTestCaseVersion = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(0));

        assertTrue(getComponentNames(previousTestCaseVersion).contains(technicalComponentName1));

        TestCaseVersion currentTestCaseVersion = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(1));
        assertTrue(getComponentNames(currentTestCaseVersion).contains(technicalComponentName2));
    }

    private Set<String> getComponentNames(TestCaseVersion testCaseVersion) {
        return testCaseVersion.getTechnicalComponents().stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());
    }

    @Test
    public void testAddNewChild() {
        TestCase testCase = getDefaultTestCase();
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        testCaseVersion.setProductFeature(productFeature);
        testCaseVersion.setType(testType);
        persistence.persistInTransaction(testCase);

        TestStep testStep = new TestStep();
        testStep.setTitle("execute");
        testCaseVersion.addTestStep(testStep);
        persistence.persistInTransaction(testCase);

        List<Number> revisions = auditor.getRevisions(TestCaseVersion.class, testCaseVersion.getId());
        assertThat(revisions, hasSize(2));

        TestCaseVersion previousTestCaseVersion = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(0));
        assertThat(previousTestCaseVersion.getTestSteps(), empty());

        TestCaseVersion currentTestCaseVersion = auditor.find(TestCaseVersion.class, testCaseVersion.getId(), revisions.get(1));
        assertThat(currentTestCaseVersion.getTestSteps(), hasSize(1));
    }

    private TestCase getDefaultTestCase() {
        TestCase testCase = TestCaseFactory.createTestCase();
        testCase.setTestCaseId("TC1");
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        testCaseVersion.setProductFeature(productFeature);
        testCaseVersion.setType(testType);
        testCaseVersion.setTitle("testDefaultTest");
        testCaseVersion.setDescription(defaultDescription);
        testCaseVersion.setPriority(Priority.NORMAL);
        testCaseVersion.setAutomationCandidate(AutomationCandidate.NO);
        testCaseVersion.setTestCaseStatus(TestCaseStatus.PRELIMINARY);

        return testCase;
    }
}
