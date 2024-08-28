/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.common;

import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

import java.util.LinkedHashSet;

public class TestDtoFactory {

    public static TestCaseInfo getTestCase(int id) {
        TestCaseInfo dto = new TestCaseInfo();
        dto.setId((long) id);
        dto.setTestCaseId("testCaseId" + id);
        dto.setTitle("testCaseTitle" + id);
        ReferenceDataItem type = new ReferenceDataItem("1", "Scalability");

        dto.setType(type);

        ReferenceDataItem executionType = new ReferenceDataItem("1", "Automated");

        dto.setExecutionType(executionType);
        dto.setDescription("description" + id);

        ReferenceDataItem priority = new ReferenceDataItem("1", "High");

        String requirement = "ENM-43242";
        LinkedHashSet<String> requirements = new LinkedHashSet();
        requirements.add(requirement);

        dto.setPriority(priority);
        dto.setRequirements(Lists.newArrayList(getRequirement(id)));
        dto.setRequirementIds(requirements);
        dto.setPrecondition("precondition" + id);
        dto.setComment("comment" + id);
        dto.setVersion("" + id * 10);
        dto.addTechnicalComponent(new ReferenceDataItem("", "component" + id));

        dto.clearGroups();
        dto.addGroup(new ReferenceDataItem("", "scope" + (id * 10 + 1)));
        dto.addGroup(new ReferenceDataItem("", "scope" + (id * 10 + 2)));

        dto.clearContexts();
        dto.addContext(new ReferenceDataItem("", "context" + (id * 10 + 1)));
        dto.addContext(new ReferenceDataItem("", "context" + (id * 10 + 1)));
        dto.setFeature(new FeatureInfo(1L, "SM"));

        dto.setTestSuite(new ReferenceDataItem("1", "suite" + id));
        dto.setTestTeam(new ReferenceDataItem("1", "team" + id));
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

    public static ProjectInfo getProjectInfo(int id) {
        ProjectInfo project = new ProjectInfo();
        project.setExternalId("ID" + id);
        project.setName("projectName" + id);
        return project;
    }

    public static TestCampaignInfo getTestPlan(int id) {
        TestCampaignInfo dto = new TestCampaignInfo();
        dto.setId((long) id);
        dto.setLocked(false);
        dto.setName("testPlan" + id);
        dto.setDescription("description" + id);
        dto.setEnvironment("environment" + id);
        dto.setSystemVersion("systemVersion" + id);
        dto.getTestCampaignItems().add(getTestPlanItemInfo(id * 10 + 1, false));
        dto.getTestCampaignItems().add(getTestPlanItemInfo(id * 10 + 2, true));
        dto.setProject(getProjectInfo(id));
        return dto;
    }

    public static TestCampaignItemInfo getTestPlanItemInfo(int id, boolean fileAttached) {
        TestCampaignItemInfo dto = new TestCampaignItemInfo();
        dto.setId((long) id);

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("mruser" + id);
        userInfo.setUserName("someUser");

        dto.setUser(userInfo);

        TestCaseInfo testCase = getTestCase(id * 11);
        dto.setResult(new ReferenceDataItem("", "result" + id));
        dto.addDefect(getDefectInfo(id));
        dto.addDefect(getDefectInfo(id * 2));
        dto.setTestCase(testCase);
        dto.setExecutionTime("11:23:45");
        dto.addRequirementIds("improvement" + id);
        dto.setKpiMeasurement("kpi " + id);
        dto.setFileAttached(fileAttached);


        return dto;
    }
}
