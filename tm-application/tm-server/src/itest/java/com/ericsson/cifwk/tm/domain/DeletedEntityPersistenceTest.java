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
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.infrastructure.GuiceTestRunner;
import com.ericsson.cifwk.tm.infrastructure.PersistenceModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(GuiceTestRunner.class)
@GuiceTestRunner.GuiceModules({
        PersistenceModule.class
})
public class DeletedEntityPersistenceTest extends BaseServiceLayerTest {

    @Inject
    private TestCaseVersionRepository testCaseVersionRepository;

    @Inject
    private TestCaseRepository testCaseRepository;

    private TestCase testCase;

    private TestCaseVersion testCaseVersionEntity;

    private TestField field1;

    private Scope scope1;

    @Before
    public void setUp() {
        testCase = TestCaseFactory.createTestCase();
        testCase.setTestCaseId("TC1");
        testCaseVersionEntity = testCase.getCurrentVersion();
        testCaseVersionEntity.setTitle("title");

        testCaseVersionEntity.setPriority(Priority.BLOCKER);
        testCaseVersionEntity.setAutomationCandidate(AutomationCandidate.NO);

        field1 = new TestField("variable1", "value1");
        testCaseVersionEntity.addOptionalField(field1);
        testCaseVersionEntity.addOptionalField(new TestField("variable2", "value2"));
        testCaseVersionEntity.setTestCaseStatus(TestCaseStatus.PRELIMINARY);

        Product product = new Product();
        product.setName("test");
        product.setExternalId("test");

        TestType testType = new TestType();
        testType.setName("Functional");
        testType.setProduct(product);
        testCaseVersionEntity.setType(testType);

        ProductFeature productFeature = new ProductFeature();
        productFeature.setProduct(product);
        productFeature.setName("feature");
        productFeature.setComponents(null);

        testCaseVersionEntity.setProductFeature(productFeature);

        scope1 = new Scope("scope1");
        Scope scope2 = new Scope("scope2");
        testCaseVersionEntity.addScopes(Arrays.asList(scope1, scope2));

        persistence.persistInTransaction(product, productFeature, testType, scope1, scope2, testCase);
    }

    @Test
    public void deletedDetailsOnTestCase() {
        // deleting field and scope
        field1.delete();
        scope1.delete();
        persistence.persistInTransaction(testCase, scope1);

        persistence.em().clear();

        TestCase newTestCase = testCaseRepository.find(testCase.getId());
        TestCaseVersion newVersion = newTestCase.getCurrentVersion();
        assertEquals(1, newVersion.getOptionalFields().size()); // @OneToMany
        assertEquals(1, newVersion.getScopes().size()); // @ManyToMany

        // deleting test case
        newTestCase.delete();
        persistence.persistInTransaction(newTestCase);

        List<TestCaseVersion> testCaseVersions = persistence.em()
                .createQuery("select t from TestCaseVersion t where t.title = :title", TestCaseVersion.class)
                .setParameter("title", "title")
                .getResultList();
        assertTrue(testCaseVersions.isEmpty());
        assertNotNull(testCaseVersionRepository.find(newVersion.getId())); // findOrImport by id does not use filter
    }

    @Test
    public void deletedTestCaseVersions() {
        TestCaseVersion second = testCase.addNewMinorVersion();
        second.delete();
        testCase.addNewMinorVersion();

        assertThat(testCase.getVersions().size(), equalTo(3));

        persistence.persistInTransaction(testCase);
        persistence.em().clear();

        TestCase newTestCase = testCaseRepository.find(testCase.getId());

        assertThat(newTestCase.getVersions().size(), equalTo(2));
    }

}
