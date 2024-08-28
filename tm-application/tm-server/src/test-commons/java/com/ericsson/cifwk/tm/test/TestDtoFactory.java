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

import com.ericsson.cifwk.tm.common.HasName;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.users.NotificationType;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.Date;

public class TestDtoFactory {

    public static TestCaseInfo getTestCase(int id) {
        TestCaseInfo dto = new TestCaseInfo();
        dto.setId((long) id);
        dto.setTestCaseId("testCaseId" + id);
        dto.setTitle("testCaseTitle" + id);
        dto.setType(new ReferenceDataItem("", "testType" + id));
        dto.setExecutionType(getReferenceData(TestExecutionType.values(), id));
        dto.setDescription("description" + id);
        dto.setPriority(getReferenceData(Priority.values(), id));
        dto.setRequirements(Lists.newArrayList(getRequirement(id)));
        dto.setPrecondition("precondition" + id);
        dto.setComment("comment" + id);
        dto.setVersion(10 + "." + id);
        dto.addTechnicalComponent(new ReferenceDataItem("", "component" + id));

        dto.clearGroups();
        dto.addGroup(new ReferenceDataItem("", "scope" + (id * 10 + 1)));
        dto.addGroup(new ReferenceDataItem("", "scope" + (id * 10 + 2)));

        dto.clearContexts();
        dto.addContext(new ReferenceDataItem("", "context" + (id * 10 + 1)));
        dto.addContext(new ReferenceDataItem("", "context" + (id * 10 + 1)));

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(1L);
        productInfo.setName("test");
        productInfo.setExternalId("test");

        FeatureInfo feature = new FeatureInfo(1L, "Other");
        feature.setProduct(productInfo);
        dto.setFeature(feature);
        return dto;
    }

    public static TestCaseInfo getTestCaseWithSteps(int id) {
        TestCaseInfo dto = getTestCase(id);
        dto.addTestStep(getTestStep(id * 10 + 1));
        dto.addTestStep(getTestStep(id * 10 + 2));
        return dto;
    }

    private static RequirementInfo getRequirement(int id) {
        RequirementInfo dto = new RequirementInfo();
        dto.setId((long) id);
        dto.setSummary("summary" + id);
        dto.setLabel("title" + id);
        dto.setType("type" + id);
        return dto;
    }

    public static TestStepInfo getTestStep(int id) {
        TestStepInfo dto = new TestStepInfo();
        dto.setId((long) id);
        dto.setName("testStep" + id);
        dto.setComment("comment" + id);
        dto.setData("data" + id);
        dto.addVerify(getVerifyStep(id * 10 + 1));
        dto.addVerify(getVerifyStep(id * 10 + 2));
        return dto;
    }

    public static VerifyStepInfo getVerifyStep(int id) {
        VerifyStepInfo dto = new VerifyStepInfo();
        dto.setId((long) id);
        dto.setName("verifyStep" + id);
        return dto;
    }

    public static DefectInfo getDefectInfo(long id, ProjectInfo project) {
        DefectInfo defect = new DefectInfo();
        defect.setId(id);
        defect.setExternalId("defect" + id);
        defect.setExternalTitle("defect" + id);
        defect.setExternalSummary("defect" + id);
        defect.setProject(project);
        return defect;
    }

    public static DefectInfo getDefectInfo(long id) {
        return getDefectInfo(id, null);
    }

    public static ReferenceDataItem getScopeInfo(String id) {
        ReferenceDataItem scope = new ReferenceDataItem();
        scope.setId(id);
        scope.setTitle("scope" + id);

        return scope;
    }

    public static ProjectInfo getProjectInfo(int id) {
        ProjectInfo project = new ProjectInfo();
        project.setExternalId("ID" + id);
        project.setName("projectName" + id);
        return project;
    }

    private static <T extends Identifiable & HasName> ReferenceDataItem getReferenceData(T[] values, int id) {
        T value = values[id % values.length];
        return new ReferenceDataItem(value.getId().toString(), value.getName());
    }

    public static TestCampaignInfo getTestPlan(int id) {
        TestCampaignInfo dto = new TestCampaignInfo();
        dto.setId((long) id);
        dto.setLocked(false);
        dto.setName("testPlan" + id);
        dto.setDescription("description" + id);
        dto.setEnvironment("environment" + id);
        dto.setSystemVersion("systemVersion" + id);
        dto.setStartDate(new Date());
        dto.setEndDate(DateTime.now().plusHours(1).toDate());
        dto.getTestCampaignItems().add(getTestPlanItemInfo(id * 10 + 1));
        dto.getTestCampaignItems().add(getTestPlanItemInfo(id * 10 + 2));
        dto.setProject(getProjectInfo(id));
        return dto;
    }

    public static TestCampaignItemInfo getTestPlanItemInfo(int id) {
        TestCampaignItemInfo dto = new TestCampaignItemInfo();
        dto.setId((long) id);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("mruser" + id);

        dto.setUser(userInfo);

        TestCaseInfo testCase = getTestCase(id * 11);
        dto.setResult(new ReferenceDataItem("", "result" + id));
        dto.addDefect(getDefectInfo(id));
        dto.addDefect(getDefectInfo(id * 2));
        dto.setTestCase(testCase);

        return dto;
    }

    public static NotificationInfo getNotification(int id) {
        NotificationInfo dto = new NotificationInfo();
        dto.setId((long) id);
        dto.setType(getReferenceData(NotificationType.values(), id));
        dto.setText("text" + id);
        dto.setStartDate(new Date());
        dto.setEndDate(DateTime.now().plusDays(1).toDate());
        return dto;
    }

    public static TestExecutionInfo getTestExecution(int id) {
        TestExecutionInfo dto = new TestExecutionInfo();
        dto.setId((long) id);
        dto.setTestPlan((long) id);
        dto.setTestCase((long) id);
        dto.setResult(getReferenceData(TestExecutionResult.values(), id));
        dto.setAuthor("externalId" + id);
        dto.setComment("comment" + id);
        return dto;
    }

    public static TestStepExecutionInfo getTestStepExecution(int id, int testExecutionId, int testStepId) {
        TestStepExecutionInfo dto = new TestStepExecutionInfo();
        dto.setId((long) id);
        dto.setTestExecution((long) testExecutionId);
        dto.setTestStep((long) testStepId);
        dto.setResult(getReferenceData(TestStepResult.values(), 0));
        dto.setData("data" + id);
        return dto;
    }

    public static VerifyStepExecutionInfo getVerifyStepExecution(int id, int testExecutionId, int testStepId, int verifyStepId) {
        VerifyStepExecutionInfo dto = new VerifyStepExecutionInfo();
        dto.setId((long) id);
        dto.setTestExecution((long) testExecutionId);
        dto.setTestStep((long) testStepId);
        dto.setVerifyStep((long) verifyStepId);
        dto.setActualResult("Actual result" + id);
        return dto;
    }

}
