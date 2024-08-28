/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.test.fixture;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.AutomationCandidate;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.users.NotificationType;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.test.fixture.builders.DefectBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.DropBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.FeatureBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.FeatureToggleBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.IsoBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.NotificationBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.PostBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.ProductBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.ProjectBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.RequirementBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.ScopeBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TechnicalComponentBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestCaseBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestExecutionBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestFieldBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestCampaignBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestCampaignItemBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestStepBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestStepExecutionBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.TestTypeBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.UserBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.VerifyStepBuilder;
import com.ericsson.cifwk.tm.test.fixture.builders.VerifyStepExecutionBuilder;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Set;

import static com.ericsson.cifwk.tm.test.fixture.Faker.*;

public class TestEntityFactory {

    public static TestCaseBuilder buildTestCase(Requirement requirement) {
        return buildTestCase()
                .addRequirement(requirement);
    }

    public static TestCaseBuilder buildTestCase() {
        return new TestCaseBuilder()
                .withTestCaseId(getUUID())
                .withTitle(randomString())
                .withDescription(randomString(140))
                .withPriority(enumRandomValue(Priority.class))
                .withPrecondition(randomString())
                .withComment(randomString())
                .withAutomationCandidate(AutomationCandidate.NO);
    }

    public static RequirementBuilder buildRequirement() {
        return new RequirementBuilder()
                .withExternalId(getUUID())
                .withExternalTitle(getUUID())
                .withExternalType(getUUID())
                .withExternalSummary(randomString(100));
    }

    public static TestStepBuilder buildTestStep() {
        return new TestStepBuilder()
                .withExecute(randomString())
                .withComment(randomString())
                .withData(randomString());
    }

    public static VerifyStepBuilder buildVerifyStep() {
        return new VerifyStepBuilder()
                .withVerifyStep(randomString());
    }

    public static TestCampaignBuilder buildTestPlan() {
        return new TestCampaignBuilder()
                .withName(randomString())
                .withDescription(randomString())
                .withEnvironment(randomString());
    }

    public static TestCampaignBuilder buildTestPlan(ProductFeature feature) {
        return new TestCampaignBuilder()
                .withName(randomString())
                .withDescription(randomString())
                .withEnvironment(randomString())
                .withFeature(feature);
    }

    public static TestCampaignBuilder buildTestPlan(String name, Drop drop, ProductFeature feature, Set<TechnicalComponent> components) {
        return new TestCampaignBuilder()
                .withName(name)
                .withDrop(drop)
                .withFeature(feature)
                .withComponents(components);
    }

    public static UserBuilder buildUser() {
        String uuid = getUUID();
        return new UserBuilder()
                .withExternalId(uuid)
                .withExternalEmail(uuid + "@example.com")
                .withExternalName(getUUID())
                .withExternalSurname(getUUID())
                .withUserName(getUUID());
    }

    public static TestCampaignItemBuilder buildTestPlanItem(User user, TestCampaign testCampaign, TestCaseVersion testCaseVersion) {
        return buildTestPlanItem()
                .withUser(user)
                .withTestPlan(testCampaign)
                .withTestCaseVersion(testCaseVersion);
    }

    public static TestCampaignItemBuilder buildTestPlanItem() {
        return new TestCampaignItemBuilder();
    }

    public static ProductBuilder buildProduct() {
        return new ProductBuilder()
                .withExternalId(getUUID())
                .withName(getUUID());
    }

    public static TestTypeBuilder buildTestType(Product product) {
        return new TestTypeBuilder()
                .withName(getUUID())
                .withProduct(product);
    }

    public static ProductBuilder buildProduct(String name) {
        return new ProductBuilder()
                .withName(name)
                .withExternalId(name);
    }

    public static ProjectBuilder buildProject(Product product) {
        return new ProjectBuilder()
                .withExternalId(getUUID())
                .withName(randomString())
                .withProduct(product);
    }

    public static FeatureBuilder buildProductFeature(Product product) {
        return new FeatureBuilder()
                .withName(randomString())
                .withProduct(product);
    }

    public static DefectBuilder buildDefect(Project project) {
        return buildDefect().withProject(project);
    }

    public static DefectBuilder buildDefect() {
        return new DefectBuilder()
                .withExternalId(getUUID())
                .withExternalTitle(randomString())
                .withExternalSummary(randomString(200))
                .withExternalStatusName(randomString(20));
    }

    public static NotificationBuilder buildNotification(int daysFromNow, int daysLength) {
        DateTime now = DateTime.now();
        DateTime start = now.plusDays(daysFromNow);
        DateTime end = start.plusDays(daysLength);

        return buildNotification(start.toDate(), end.toDate());
    }

    public static NotificationBuilder buildNotification(Date startDate, Date endDate) {
        return new NotificationBuilder()
                .withType(enumRandomValue(NotificationType.class))
                .withText(randomString())
                .withStartDate(startDate)
                .withEndDate(endDate);
    }

    public static TestExecutionBuilder buildTestExecution(
            User user,
            TestCampaign testCampaign,
            TestCaseVersion testCaseVersion) {
        return buildTestExecution()
                .withTestPlan(testCampaign)
                .withTestCaseVersion(testCaseVersion)
                .withAuthor(user);
    }

    public static TestExecutionBuilder buildTestExecution() {
        return new TestExecutionBuilder()
                .withResult(enumRandomValue(TestExecutionResult.class))
                .withComment(randomString());
    }

    public static TestStepExecutionBuilder buildTestStepExecution() {
        return new TestStepExecutionBuilder()
                .withResult(enumRandomValue(TestStepResult.class))
                .withData(randomString());
    }

    public static VerifyStepExecutionBuilder buildVerifyStepExecution() {
        return new VerifyStepExecutionBuilder();
    }

    public static ScopeBuilder buildScope() {
        return new ScopeBuilder();
    }

    public static TechnicalComponentBuilder buildTechnicalComponent() {
        return new TechnicalComponentBuilder();
    }

    public static FeatureToggleBuilder buildFeatureToggle() {
        return new FeatureToggleBuilder();
    }

    public static PostBuilder buildPost() {
        return new PostBuilder();
    }

    public static PostBuilder buildPost(
            long objectId,
            User user,
            PostObjectNameReference objNameReference) {
        return new PostBuilder()
                .withObjectId(objectId)
                .withUser(user)
                .withObjectNameReference(objNameReference);
    }

    public static TestFieldBuilder buildTestField() {
        return new TestFieldBuilder();
    }

    public static DropBuilder buildDrop(Product product, String name) {
        return new DropBuilder()
                .withProduct(product)
                .withName(name);
    }

    public static FeatureBuilder buildFeature(String name, Product product, Set<TechnicalComponent> components) {
        return new FeatureBuilder()
                .withName(name)
                .withProduct(product)
                .withComponents(components);
    }

    public static IsoBuilder buildIso(Drop drop, String name, String version) {
        return new IsoBuilder()
                .withDrop(drop)
                .withName(name)
                .withVersion(version);
    }
}
