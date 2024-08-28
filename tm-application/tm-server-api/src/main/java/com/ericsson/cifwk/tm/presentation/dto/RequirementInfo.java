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
import com.ericsson.cifwk.tm.presentation.dto.view.RequirementView;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.HashSet;
import java.util.Set;

public class RequirementInfo implements Identifiable<Long> {

    private Long id;
    private String externalId;
    private String type;
    private String label;
    private String summary;
    private ProjectInfo project;
    private String externalStatusName;
    private String deliveredIn;

    @JsonView(RequirementView.Tree.class)
    private Set<RequirementInfo> children = new HashSet<>();

    @JsonView(RequirementView.Tree.class)
    private Integer testCaseCount;

    @JsonView(RequirementView.Detailed.class)
    private String releaseExternalId;

    @JsonView(RequirementView.Detailed.class)
    private String rootTitle;

    @JsonView(RequirementView.Detailed.class)
    private Set<TestCaseInfo> testCases;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public Set<RequirementInfo> getChildren() {
        return children;
    }

    public void setChildren(Set<RequirementInfo> children) {
        this.children = children;
    }

    public Integer getTestCaseCount() {
        return testCaseCount;
    }

    public void setTestCaseCount(Integer testCaseCount) {
        this.testCaseCount = testCaseCount;
    }

    public String getReleaseExternalId() {
        return releaseExternalId;
    }

    public void setReleaseExternalId(String releaseExternalId) {
        this.releaseExternalId = releaseExternalId;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public Set<TestCaseInfo> getTestCases() {
        return testCases;
    }

    public void setTestCases(Set<TestCaseInfo> testCases) {
        this.testCases = testCases;
    }

    public String getExternalStatusName() {
        return externalStatusName;
    }

    public void setExternalStatusName(String externalStatusName) {
        this.externalStatusName = externalStatusName;
    }

    public String getDeliveredIn() {
        return deliveredIn;
    }

    public void setDeliveredIn(String deliveredIn) {
        this.deliveredIn = deliveredIn;
    }
}
