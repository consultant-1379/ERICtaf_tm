package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


public class TestCaseRepositoryImplTest extends BaseServiceLayerTest {

    private Product product;
    private ProductFeature productFeature;
    private TestType testType;

    @Before
    public void setUp() {
        product = new Product();
        product.setExternalId("test");
        product.setName("test");

        productFeature = new ProductFeature();
        productFeature.setName("CM");
        productFeature.setProduct(product);

        testType = new TestType();
        testType.setName("Functional");
        testType.setProduct(product);

        persistence.persistInTransaction(product, testType, productFeature);
    }


    @Test
    public void shouldCreateNewInstance() {
        TestCase testCase = TestEntityFactory.buildTestCase()
                .withProductFeature(productFeature)
                .withType(testType)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();
        persistence.persistInTransaction(testCase);

        assertThat(testCase.getId(), notNullValue());
        assertThat(testCase.getVersions().size(), equalTo(1));
        assertThat(testCase.getVersions().get(0).getTestCase(), equalTo(testCase));
        assertThat(testCase.getVersions().get(0).getId(), notNullValue());
    }


    @Test
    public void shouldUpdateVersion() {
        TestCase testCase = TestEntityFactory.buildTestCase()
                .withDescription("old")
                .withProductFeature(productFeature)
                .withType(testType)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();

        persistence.persistInTransaction(testCase);

        assertThat(testCase.getCurrentVersion().getDescription(), equalTo("old"));

        testCase.addNewMinorVersion();
        TestCaseVersion newVersion = testCase.getCurrentVersion();
        newVersion.setDescription("new");

        persistence.persistInTransaction(testCase);

        assertThat(testCase.getVersions().size(), equalTo(2));
        assertThat(testCase.getCurrentVersion().getDescription(), equalTo("new"));
    }

    @Test
    public void shouldKeepTrackOfLatestVersion() {
        TestCase testCase = TestEntityFactory.buildTestCase()
                .withProductFeature(productFeature)
                .withType(testType)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();

        testCase.addNewMajorVersion();
        testCase.addNewMinorVersion();
        persistence.persistInTransaction(testCase);

        List<TestCaseVersion> versions = testCase.getVersions();
        assertThat(versions.size(), equalTo(3));
        assertThat(versions.get(0).getMajorVersion(), equalTo(0L));
        assertThat(versions.get(0).getMinorVersion(), equalTo(1L));

        assertThat(versions.get(1).getMajorVersion(), equalTo(1L));
        assertThat(versions.get(1).getMinorVersion(), equalTo(0L));

        assertThat(versions.get(2).getMajorVersion(), equalTo(1L));
        assertThat(versions.get(2).getMinorVersion(), equalTo(1L));
    }

}
