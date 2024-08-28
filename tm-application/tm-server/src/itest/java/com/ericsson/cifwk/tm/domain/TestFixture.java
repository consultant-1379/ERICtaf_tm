/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsResultRecord;
import com.ericsson.cifwk.tm.domain.model.trs.TrsSessionRecord;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.ericsson.cifwk.tm.infrastructure.PersistenceHelper;
import com.ericsson.cifwk.tm.test.fixture.Faker;
import com.google.common.collect.Sets;

import java.util.Date;
import java.util.UUID;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;

public final class TestFixture {

    private final PersistenceHelper persistence;

    public TestFixture(PersistenceHelper persistence) {
        this.persistence = persistence;
    }

    public User persistUser() {
        return persistUser(Faker.getUUID());
    }

    public User persistUser(String externalId) {
        User user = buildUser()
                .withExternalId(externalId)
                .build();
        persistence.persistInTransaction(user);
        return user;
    }

    public User persistUser(String externalId, String externalEmail, String userName) {
        User user = buildUser()
                .withExternalId(externalId)
                .withExternalEmail(externalEmail)
                .withUserName(userName)
                .build();
        persistence.persistInTransaction(user);
        return user;
    }

    public UserSession persistUserSession(User user, Date create_at) {
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setCreatedAt(create_at);
        userSession.setSessionId(Faker.getUUID());

        persistence.persistInTransaction(userSession);
        return userSession;
    }

    public Project persistProject(String id) {
        return persistProject(id, id);
    }

    public Project persistProject(String projectId, String name) {
        Product product = persistProduct();
        return persistProject(projectId, name, product);
    }

    public Project persistProject(String projectId, String name, Product product) {
        Project project = buildProject(product)
                .withName(name)
                .withExternalId(projectId)
                .build();
        persistence.persistInTransaction(project);
        return project;
    }

    public Requirement persistRequirement(String requirementId, Project project) {
        Requirement requirement = buildRequirement()
                .withProject(project)
                .withExternalId(requirementId)
                .withExternalTitle("EXT")
                .withExternalType("Story")
                .withExternalStatusName("externalStatusName")
                .withDeliveredIn("15.14")
                .build();
        persistence.persistInTransaction(requirement);
        return requirement;
    }

    public Requirement persistRequirementOfType(String requirementId, String type, Project project) {
        Requirement requirement = buildRequirement()
                .withProject(project)
                .withExternalId(requirementId)
                .withExternalTitle("EXT")
                .withExternalType(type)
                .withExternalStatusName("externalStatusName")
                .withDeliveredIn("15.14")
                .build();
        persistence.persistInTransaction(requirement);
        return requirement;
    }

    public TestCase persistTestCase() {
        return persistTestCase(Faker.getUUID());
    }

    public TestCase persistTestCase(String testCaseId) {
        Product product = persistProduct();
        TestType testType = persistTestType("testType", product);
        ProductFeature feature = persistFeature("feature", product);
        TechnicalComponent component = persistTechnicalComponent("component", feature);

        TestCase testCase = persistTestCase(testCaseId, component, feature, testType);
        persistence.persistInTransaction(testCase);
        return testCase;
    }

    public TestCase persistTestCase(String testCaseId, Product product) {
        TestType testType = persistTestType("testType_" + testCaseId, product);
        ProductFeature feature = persistFeature("feature_" + testCaseId, product);
        TechnicalComponent component = persistTechnicalComponent("component", feature);

        TestCase testCase = persistTestCase(testCaseId, component, feature, testType);
        persistence.persistInTransaction(testCase);
        return testCase;
    }

    public TestCase persistTestCase(String testCaseId, TechnicalComponent technicalComponent,
                                    ProductFeature feature,
                                    TestType testType) {
        TestCase testCase = buildTestCase()
                .withTestCaseId(testCaseId)
                .withTitle("testDefaultTest")
                .addTechnicalComponent(technicalComponent)
                .withProductFeature(feature)
                .withType(testType)
                .withPriority(Priority.NORMAL)
                .withExecutionType(TestExecutionType.AUTOMATED)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .withAutomationCandidate(AutomationCandidate.NO)
                .build();

        persistence.persistInTransaction(testCase);
        return testCase;
    }

    public TestExecution persistTestExecution(TestCampaign testCampaign, TestCaseVersion testCaseVersion, ISO iso) {
        TestExecution testExecution = new TestExecution();
        testExecution.setTestCampaign(testCampaign);
        testExecution.setTestCaseVersion(testCaseVersion);
        testExecution.setIso(iso);
        testExecution.setResult(TestExecutionResult.PASS);
        persistence.persistInTransaction(testExecution);
        return testExecution;
    }

    public TestCampaign persistTestCampaignWithFeature(String productName, String featureName) {
        Product product = buildProduct(productName).build();
        ProductFeature feature = buildFeature(featureName, product, Sets.newHashSet()).build();
        TestCampaign testCampaign = buildTestPlan(feature).build();
        persistence.persistInTransaction(product, feature, testCampaign);
        return testCampaign;
    }

    public TestCampaign persistTestCampaignWithFeature(Product product, String featureName) {
        ProductFeature feature = buildFeature(featureName, product, Sets.newHashSet()).build();
        TestCampaign testCampaign = buildTestPlan(feature).build();
        persistence.persistInTransaction(product, feature, testCampaign);
        return testCampaign;
    }

    public TestCampaign persistTestCampaignWithFeature(String testCampaignName, String productName, String featureName) {
        TestCampaign testCampaign = persistTestCampaignWithFeature(productName, featureName);
        testCampaign.setName(testCampaignName);
        return testCampaign;
    }

    public TestCampaign persistTestCampaignWithFeature(ProductFeature feature) {
        TestCampaign testCampaign = buildTestPlan(feature).build();
        persistence.persistInTransaction(feature, testCampaign);
        return testCampaign;
    }

    public Defect persistDefect(Project project) {
        Defect defect = buildDefect()
                .withExternalId("externalId")
                .withExternalTitle("externalTitle")
                .withExternalSummary("externalSummary")
                .withExternalStatusName("externalStatusName")
                .withProject(project)
                .build();
        persistence.persistInTransaction(defect);
        return defect;
    }

    public Defect persistDefect(String externalId) {
        Defect defect = buildDefect()
                .withExternalId(externalId)
                .withExternalTitle("externalTitle" + externalId)
                .withExternalSummary("externalSummary" + externalId)
                .withExternalStatusName("externalStatusName" + externalId)
                .build();
        persistence.persistInTransaction(defect);
        return defect;
    }

    public Product persistProduct(String name) {
        Product product = buildProduct()
                .withExternalId(name)
                .withName(name)
                .build();
        persistence.persistInTransaction(product);
        return product;
    }

    public TestType persistTestType(String name, Product product) {
        TestType testType = buildTestType(product)
                .withName(name)
                .build();
        persistence.persistInTransaction(testType);
        return testType;
    }

    public Product persistProduct() {
        return persistProduct(Faker.getUUID());
    }

    public Product persistProduct(String name, Project project) {
        Product product = buildProduct()
                .withExternalId(name)
                .withName(name)
                .addProject(project)
                .build();
        persistence.persistInTransaction(product);
        return product;
    }

    public Product persistProduct(String externalId, String name, Project project) {
        Product product = buildProduct()
                .withExternalId(externalId)
                .withName(name)
                .addProject(project)
                .build();
        persistence.persistInTransaction(product);
        return product;
    }

    public ProductFeature persistProductFeature() {
        Product product = persistProduct(Faker.getUUID());
        ProductFeature productFeature = new ProductFeature();
        productFeature.setProduct(product);
        productFeature.setName(Faker.getUUID());
        persistence.persistInTransaction(productFeature);
        return productFeature;
    }

    public TestCampaignItem persistTestPlanItem(User user, TestCampaign testCampaign, TestCaseVersion testCaseVersion) {
        TestCampaignItem testCampaignItem = buildTestPlanItem(user, testCampaign, testCaseVersion).build();
        persistence.persistInTransaction(testCampaignItem);
        return testCampaignItem;
    }

    public TestExecution constructTestExecution(TestCampaign testCampaign, TestCaseVersion testCaseVersion) {
        return buildTestExecution()
                .withTestPlan(testCampaign)
                .withTestCaseVersion(testCaseVersion)
                .withResult(TestExecutionResult.NOT_STARTED)
                .build();
    }

    public Scope persistScope(String name, Product product) {
        Scope scope = buildScope()
                .withName(name)
                .withProduct(product)
                .withEnabled(true)
                .build();
        persistence.persistInTransaction(scope);
        return scope;
    }

    public Scope persistScope(String name, Product product, boolean enabled) {
        Scope scope = buildScope()
                .withName(name)
                .withProduct(product)
                .withEnabled(enabled)
                .build();
        persistence.persistInTransaction(scope);
        return scope;
    }

    public TechnicalComponent persistTechnicalComponent(String name, ProductFeature feature) {
        TechnicalComponent component = buildTechnicalComponent()
                .withName(name)
                .withFeature(feature)
                .build();
        persistence.persistInTransaction(component);
        return component;
    }

    public Post persistPost(Post post) {
        persistence.persistInTransaction(post);
        return post;
    }

    public ProductFeature persistFeature(String name, Product product) {
        ProductFeature feature = buildFeature(name, product, Sets.newHashSet()).build();
        persistence.persistInTransaction(feature);
        return feature;
    }

    public Drop persistDrop(Product product, String name) {
        Drop drop = new Drop(product, name);
        persistence.persistInTransaction(drop);
        return drop;
    }

    public ISO persistIso(String name, String version) {
        ISO iso = new ISO();
        iso.setName(name);
        iso.setVersion(version);
        persistence.persistInTransaction(iso);
        return iso;
    }

    public TrsJobRecord persistTvsJob(TestCampaign testCampaign) {
        TrsJobRecord job = new TrsJobRecord();
        job.setJobId(UUID.randomUUID().toString());
        job.setJobName(testCampaign.getName());
        job.addTestCampaign(testCampaign);
        persistence.persistInTransaction(job);
        return job;
    }

    public TrsSessionRecord persistTvsSession(ISO iso, TrsJobRecord trsJobRecord) {
        TrsSessionRecord session = new TrsSessionRecord(UUID.randomUUID().toString(), iso, trsJobRecord);
        persistence.persistInTransaction(session);
        return session;
    }

    public TrsResultRecord persistTvsResult(TestCase testCase, TrsSessionRecord trsSessionRecord) {
        TrsResultRecord result = new TrsResultRecord(UUID.randomUUID().toString(), testCase, trsSessionRecord);
        persistence.persistInTransaction(result);
        return result;
    }
}
