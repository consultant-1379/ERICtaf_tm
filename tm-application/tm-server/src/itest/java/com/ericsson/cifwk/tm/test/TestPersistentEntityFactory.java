/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.test;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseFactory;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationType;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.infrastructure.PersistenceHelper;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class TestPersistentEntityFactory {

    private PersistenceHelper persistence;

    public TestPersistentEntityFactory(PersistenceHelper persistence) {
        this.persistence = persistence;
    }

    public TestCase createTestCase(Requirement requirement) {
        TestCase testCase = TestCaseFactory.createTestCase();
        testCase.setTestCaseId(generateUUID());

        TestCaseVersion version = testCase.getCurrentVersion();
        version.setTitle(generateRandomString());
        version.setDescription("description" + generateRandomInt());
        version.setPriority(getNthEnumValue(Priority.class, generateRandomInt()));
        version.addRequirement(requirement);
        version.setPrecondition(generateRandomString());
        version.setComment(generateRandomString());

        persistence.persistInTransaction(testCase, version);

        return testCase;
    }

    public TestCase createTestCase() {
        return createTestCase(createRequirement());
    }

    public Requirement createRequirement() {
        Requirement requirement = new Requirement(generateUUID());
        requirement.setExternalSummary(generateRandomString(100));
        requirement.setExternalLabel(generateUUID());
        requirement.setExternalType(generateUUID());

        persistence.persistInTransaction(requirement);

        return requirement;
    }

    public TestStep createTestStep() {
        TestStep testStep = new TestStep();
        testStep.setTitle(generateRandomString());
        testStep.setComment(generateRandomString());
        testStep.setData(generateRandomString());

        persistence.persistInTransaction(testStep);

        return testStep;
    }

    public VerifyStep createVerifyStep() {
        VerifyStep verifyStep = new VerifyStep();
        verifyStep.setVerifyStep(generateRandomString());

        persistence.persistInTransaction(verifyStep);

        return verifyStep;
    }

    public TestCampaign createTestPlan() {
        TestCampaign testCampaign = new TestCampaign();
        testCampaign.setName(generateRandomString());
        testCampaign.setDescription(generateRandomString());
        testCampaign.setEnvironment(generateRandomString());

        persistence.persistInTransaction(testCampaign);

        return testCampaign;
    }

    public User createUser() {
        User user = new User(generateUUID());

        persistence.persistInTransaction(user);

        return user;
    }

    public User createUser(String userId) {
        User user = new User(userId);

        persistence.persistInTransaction(user);

        return user;
    }


    public TestCampaignItem createTestPlanItem(User user, TestCampaign testCampaign, TestCaseVersion testCaseVersion) {
        TestCampaignItem testCampaignItem = new TestCampaignItem();
        testCampaignItem.setUser(user);
        testCampaignItem.setTestCampaign(testCampaign);
        testCampaignItem.setTestCaseVersion(testCaseVersion);

        persistence.persistInTransaction(testCampaignItem);

        return testCampaignItem;
    }

    public TestCampaignItem createTestPlanItem() {
        TestCase testCase = createTestCase();

        return createTestPlanItem(createUser(), createTestPlan(), testCase.getCurrentVersion());
    }

    public Product createProduct() {
        String externalId = generateUUID();
        Product product = new Product(externalId);
        product.setName(externalId);
        persistence.persistInTransaction(product);
        return product;
    }

    public Project createProject(Product product) {
        Project project = new Project(generateUUID());
        project.setName(generateRandomString());
        product.addProject(project);
        persistence.persistInTransaction(project);

        return project;
    }

    public TestType createTestType(Product product) {
        TestType testType = new TestType();
        testType.setName(generateRandomString());
        testType.setProduct(product);
        persistence.persistInTransaction(testType);

        return testType;
    }

    public Defect createDefect(Project project) {
        Defect defect = new Defect(generateUUID());
        defect.setExternalTitle(generateRandomString());
        defect.setExternalSummary(generateRandomString());
        defect.setProject(project);

        persistence.persistInTransaction(defect);

        return defect;
    }

    public Defect createDefect() {
        return createDefect(createProject(createProduct()));
    }

    public Notification createNotification(int daysFromNow, int daysLength) {
        DateTime now = DateTime.now();
        DateTime start = now.plusDays(daysFromNow);
        DateTime end = start.plusDays(daysLength);

        return createNotification(start.toDate(), end.toDate());
    }

    public Notification createNotification(Date startDate, Date endDate) {
        Notification notification = new Notification();
        notification.setType(getNthEnumValue(NotificationType.class, generateRandomInt()));
        notification.setText(generateRandomString());
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);

        persistence.persistInTransaction(notification);

        return notification;
    }

    public Notification createNotification() {
        return createNotification(0, 1);
    }

    public TestExecution createTestExecution(User user, TestCampaign testCampaign, TestCaseVersion testCaseVersion) {
        TestExecution testExecution = new TestExecution();
        testExecution.setTestCampaign(testCampaign);
        testExecution.setTestCaseVersion(testCaseVersion);
        testExecution.setResult(TestExecutionResult.PASS);
        testExecution.setAuthor(user);
        testExecution.setComment(generateRandomString());

        persistence.persistInTransaction(testExecution);

        return testExecution;
    }

    public TestExecution createTestExecution() {
        TestCase testCase = createTestCase();

        return createTestExecution(createUser(), createTestPlan(), testCase.getCurrentVersion());
    }

    public TestStepExecution createTestStepExecution(TestStep testStep, TestExecution testExecution) {
        TestStepExecution testStepExecution = new TestStepExecution();

        testStepExecution.setTestStep(testStep);
        testStepExecution.setTestExecution(testExecution);
        testStepExecution.setResult(TestStepResult.PASS);
        testStepExecution.setData(generateRandomString());

        persistence.persistInTransaction(testStepExecution);

        return testStepExecution;
    }

    public TestStepExecution createTestStepExecution() {
        return createTestStepExecution(createTestStep(), createTestExecution());
    }

    // Generators
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int CHARACTERS_LENGTH = CHARACTERS.length();

    private final Random random = new Random();

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private String generateRandomString() {
        return generateRandomString(26);
    }

    private String generateRandomString(int length) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < length; i++) {
            double index = random.nextDouble() * CHARACTERS_LENGTH;
            buffer.append(CHARACTERS.charAt((int) index));
        }
        return buffer.toString();
    }

    private int generateRandomInt() {
        return generateRandomInt(100000);
    }

    private int generateRandomInt(int n) {
        return random.nextInt(n);
    }

    // Helpers
    private <T extends Enum<T>> T getNthEnumValue(Class<T> enumType, int n) {
        T[] constants = enumType.getEnumConstants();
        return constants[n % constants.length];
    }
}
