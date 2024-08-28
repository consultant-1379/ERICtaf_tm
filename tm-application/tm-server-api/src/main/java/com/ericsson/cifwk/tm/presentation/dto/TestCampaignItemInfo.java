/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemView;
import com.ericsson.cifwk.tm.presentation.validation.TestCampaignItem;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@TestCampaignItem
public class TestCampaignItemInfo implements Identifiable<Long> {

    private static final String JIRA_LINK = "https://eteamproject.internal.ericsson.com/browse/";

    private Long id;

    private TestCaseInfo testCase = new TestCaseInfo();

    private ReferenceDataItem result;

    private UserInfo resultAuthor;

    private String executionTime;

    private String kpiMeasurement;

    private Set<DefectInfo> defects = new LinkedHashSet<>();

    private Set<String> requirementIds = new LinkedHashSet<>();

    @JsonView(TestCampaignItemView.Detailed.class)
    private ReferenceDataItem testCampaign;

    @JsonView(TestCampaignItemView.Detailed.class)
    private Set<ReferenceDataItem> testCaseVersions = new LinkedHashSet<>();

    private UserInfo user;

    private FeatureInfo feature;

    private boolean fileAttached;

    private String comment;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCaseInfo getTestCase() {
        return testCase;
    }

    public void setTestCase(TestCaseInfo testCase) {
        this.testCase = testCase;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public FeatureInfo getFeature() {
        return feature;
    }

    public void setFeature(FeatureInfo feature) {
        this.feature = feature;
    }

    public ReferenceDataItem getResult() {
        return result;
    }

    public void setResult(ReferenceDataItem result) {
        this.result = result;
    }

    public Set<DefectInfo> getDefects() {
        return Collections.unmodifiableSet(defects);
    }

    public void addDefect(DefectInfo defect) {
        this.defects.add(defect);
    }

    public void setDefects(Set<DefectInfo> defects) {
        this.defects = defects;
    }

    public Set<String> getDefectIds() {
        Set<String> defectIds = new LinkedHashSet<>();
        for (DefectInfo defect : getDefects()) {
            defectIds.add(defect.getExternalId());
        }
        return defectIds;
    }


    public Set<String> getDefectIdsForCSV() {
        Set<String> defectIds = new LinkedHashSet<>();
        for (DefectInfo defect : getDefects()) {
            defectIds.add(JIRA_LINK+defect.getExternalId());
        }
        return defectIds;
    }

    public ReferenceDataItem getTestCampaign() {
        return testCampaign;
    }

    public void setTestCampaign(ReferenceDataItem testCampaign) {
        this.testCampaign = testCampaign;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public void addRequirementIds(String requirementId) {
        this.requirementIds.add(requirementId);
    }

    public Set<String> getRequirementIds() {
        return requirementIds;
    }

    public Set<ReferenceDataItem> getTestCaseVersions() {
        return testCaseVersions;
    }

    public void setTestCaseVersions(Set<ReferenceDataItem> testCaseVersions) {
        this.testCaseVersions = testCaseVersions;
    }

    public UserInfo getResultAuthor() {
        return resultAuthor;
    }

    public void setResultAuthor(UserInfo resultAuthor) {
        this.resultAuthor = resultAuthor;
    }

    public String getKpiMeasurement() {
        return kpiMeasurement;
    }

    public void setKpiMeasurement(String kpiMeasurement) {
        this.kpiMeasurement = kpiMeasurement;
    }

    public boolean isFileAttached() {
        return fileAttached;
    }

    public void setFileAttached(boolean fileAttached) {
        this.fileAttached = fileAttached;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
