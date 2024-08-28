package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.googlecode.genericdao.search.Search;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.getLast;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 *
 */
public class TestCaseVersionRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    TestCaseVersionRepository testCaseVersionRepository;

    @Test
    public void shouldSearchWithPaging() {
        fixture().persistTestCase("TC1");
        fixture().persistTestCase("TC2");
        fixture().persistTestCase("TC3");

        Paginated<TestCaseVersion> result = testCaseVersionRepository.searchPaginated(new Search(), 1, 2);

        assertThat(result.getTotal(), equalTo(3L));
        assertThat(result.getResults(), hasSize(2));
    }

    @Test
    public void shouldCreateTestCase() {
        Product product = fixture().persistProduct();
        TestType testType = fixture().persistTestType("Functional", product);
        ProductFeature feature = fixture().persistFeature("feature", product);
        TechnicalComponent technicalComponent = fixture().persistTechnicalComponent("technicalComponentName", feature);
        TestCase testCase = fixture().persistTestCase("TC1", technicalComponent, feature, testType);
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        TestStep testStep = constructTestStep(testCaseVersion);
        constructVerifyStep(testStep);

        persistence.persistInTransaction(testCase);

        TestStep persistedTestStep = persistence.em()
                .createQuery("select ts from TestStep ts where ts.title = :execute", TestStep.class)
                .setParameter("execute", "testDefaultTestStep")
                .getSingleResult();

        assertEquals(0, (long) persistedTestStep.getSequenceOrder());
        assertEquals(1, persistedTestStep.getVerifications().size());
        assertEquals("verify", persistedTestStep.getVerifications().iterator().next().getVerifyStep());

        TestCaseVersion persistedTestCaseVersion = persistedTestStep.getTestCaseVersion();

        assertEquals(Priority.NORMAL, persistedTestCaseVersion.getPriority());
        assertEquals("testDefaultTest", persistedTestCaseVersion.getTitle());
        assertEquals("Functional", persistedTestCaseVersion.getType().getName());
        assertEquals(TestExecutionType.AUTOMATED, persistedTestCaseVersion.getExecutionType());
        Set<String> componentNames = persistedTestCaseVersion.getTechnicalComponents()
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());

        assertTrue(componentNames.contains("technicalComponentName"));
    }

    @Test
    public void testScopes() {
        EntityManager em = persistence.em();
        EntityTransaction trans = em.getTransaction();

        TestCase testCase1 = fixture().persistTestCase("TC1");
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        TestCase testCase2 = fixture().persistTestCase("TC2");
        TestCaseVersion testCaseVersion2 = testCase2.getCurrentVersion();

        Scope scope = new Scope();
        scope.setName("scopeName");

        testCaseVersion1.addScope(scope);
        testCaseVersion2.addScope(scope);

        trans.begin();
        em.persist(scope);
        em.persist(testCase1);
        em.persist(testCase2);
        em.flush();
        trans.commit();

        em.clear();

        TestCaseVersion savedTestCaseVersion1 = em.find(TestCaseVersion.class, testCaseVersion1.getId());
        TestCaseVersion savedTestCaseVersion2 = em.find(TestCaseVersion.class, testCaseVersion2.getId());
        Scope savedScope = em.find(Scope.class, scope.getId());

        assertThat(savedTestCaseVersion1.getScopes(), hasSize(1));
        assertThat(getLast(savedTestCaseVersion1.getScopes()).getName(), equalTo(savedScope.getName()));
        assertThat(savedTestCaseVersion2.getScopes(), hasSize(1));
        assertThat(getLast(savedTestCaseVersion2.getScopes()).getName(), equalTo(savedScope.getName()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldFailWithUniqueConstraint() {
        Requirement requirementEntity = new Requirement("requirement");
        persistence.persistInTransaction(requirementEntity);

        fixture().persistTestCase("TC1");

        fixture().persistTestCase("TC1");

        persistence.em().flush();
    }

    @Test
    public void testPersistence() {
        fixture().persistTestCase("TC1");

        TestCaseVersion persistedTestCaseVersion = persistence.em()
                .createQuery("select t from TestCaseVersion t where t.title = :title", TestCaseVersion.class)
                .setParameter("title", "testDefaultTest")
                .getSingleResult();

        assertEquals("testDefaultTest", persistedTestCaseVersion.getTitle());
    }

    @Test
    public void testVersionIncrementsAfterModification() {
        TestCase testCase = fixture().persistTestCase("TC1");
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

        Long id = testCaseVersion.getId();
        TestCaseVersion persistedTestCaseVersion = testCaseVersionRepository.find(id);
        persistedTestCaseVersion.setTestCaseStatus(TestCaseStatus.APPROVED);
        persistence.persistInTransaction(persistedTestCaseVersion);

        TestCaseVersion updatedTestCaseVersion = persistence.em()
                .createQuery("select t from TestCaseVersion t where t.title = :title", TestCaseVersion.class)
                .setParameter("title", "testDefaultTest")
                .getSingleResult();

        assertThat(updatedTestCaseVersion.getTestCaseStatus(), equalTo(TestCaseStatus.APPROVED));
    }

    private TestStep constructTestStep(TestCaseVersion testCaseVersion) {
        TestStep testStepEntity = new TestStep();
        testCaseVersion.addTestStep(testStepEntity);
        testStepEntity.setTitle("testDefaultTestStep");
        return testStepEntity;
    }

    private void constructVerifyStep(TestStep testStep) {
        VerifyStep verify = new VerifyStep();
        verify.setVerifyStep("verify");
        testStep.addVerification(verify);
    }
}
