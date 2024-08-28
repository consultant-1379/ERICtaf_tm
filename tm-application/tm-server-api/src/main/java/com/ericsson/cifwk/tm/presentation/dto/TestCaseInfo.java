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

import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.validation.AnyNotEmptyField;
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AnyNotEmptyField({"technicalComponents", "component"})
public class TestCaseInfo extends SimpleTestCaseInfo {

    @Valid
    @NotNullField("type")
    private ReferenceDataItem type;

    @JsonView(TestCaseView.Detailed.class)
    //    @Contexts
    private Set<ReferenceDataItem> contexts = new LinkedHashSet<>();

    @JsonView(TestCaseView.Detailed.class)
    @NotEmptyField("technicalComponents")
    private Set<ReferenceDataItem> technicalComponents = Sets.newHashSet();

    @JsonView(TestCaseView.Detailed.class)
    @NotNullField("priority")
    private ReferenceDataItem priority;

    @JsonView(TestCaseView.Detailed.class)
    @Valid
    private Set<ReferenceDataItem> groups = new LinkedHashSet<>();

    @JsonView(TestCaseView.Detailed.class)
    @Valid
    private List<TestStepInfo> testSteps = new ArrayList<>();

    @JsonView(TestCaseView.Detailed.class)
    private List<RequirementInfo> requirements;

    @JsonView(TestCaseView.Detailed.class)
    private String precondition;

    @JsonView(TestCaseView.Detailed.class)
    private String packageName;

    @JsonView(TestCaseView.Detailed.class)
    private ReferenceDataItem executionType;

    @JsonView(TestCaseView.Detailed.class)
    private ReferenceDataItem automationCandidate;

    @JsonView(TestCaseView.Detailed.class)
    private ReferenceDataItem testCaseStatus;

    @JsonView(TestCaseView.Detailed.class)
    private List<VersionModification> modifications;

    @JsonView(TestCaseView.Detailed.class)
    @NotNullField("feature")
    private FeatureInfo feature;

    @JsonView(TestCaseView.Detailed.class)
    private List<TestCampaignInfo> associatedTestCampaigns;

    @JsonView(TestCaseView.Detailed.class)
    private boolean intrusive;

    @JsonView(TestCaseView.Detailed.class)
    private String intrusiveComment;

    @JsonView(TestCaseView.Detailed.class)
    private ReferenceDataItem testSuite;

    @JsonView(TestCaseView.Detailed.class)
    private ReferenceDataItem testTeam;

    @JsonView(TestCaseView.Detailed.class)
    private ReviewGroupInfo reviewGroup;

    private UserInfo reviewUser;

    @JsonView(TestCampaignItemView.Detailed.class)
    private Set<ReferenceDataItem> testCaseVersions = new LinkedHashSet<>();

    public ReferenceDataItem getType() {
        return type;
    }

    public void setType(ReferenceDataItem type) {
        this.type = type;
    }

    public Set<ReferenceDataItem> getContexts() {
        return Collections.unmodifiableSet(contexts);
    }

    public void addContexts(Collection<ReferenceDataItem> contexts) {
        this.contexts.addAll(contexts);
    }

    public void addContext(ReferenceDataItem context) {
        if (context != null) {
            this.contexts.add(context);
        }
    }

    public void clearContexts() {
        this.contexts.clear();
    }

    public Set<ReferenceDataItem> getTechnicalComponents() {
        return technicalComponents;
    }

    public void setTechnicalComponents(Set<ReferenceDataItem> technicalComponents) {
        this.technicalComponents = technicalComponents;
    }

    public void addTechnicalComponent(ReferenceDataItem component) {
        if (component != null) {
            technicalComponents.add(component);
        }
    }

    /**
     * Returns the title of valid component (if applicable),
     * or custom component otherwise.
     *
     * @return String title
     */
    public String getComponentTitle() {
        if (technicalComponents == null || technicalComponents.isEmpty()) {
            return "";
        }
        List<String> componentTitles = technicalComponents.stream()
                .map(c -> c.getTitle())
                .collect(Collectors.toList());

        return Joiner.on(", ").join(componentTitles);
    }

    public List<RequirementInfo> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<RequirementInfo> requirements) {
        this.requirements = requirements;
    }

    public ReferenceDataItem getPriority() {
        return priority;
    }

    public void setPriority(ReferenceDataItem priority) {
        this.priority = priority;
    }

    public List<TestStepInfo> getTestSteps() {
        return Collections.unmodifiableList(testSteps);
    }

    public void setTestSteps(List<TestStepInfo> testSteps) {
        this.testSteps = testSteps;
    }

    public void addTestStep(TestStepInfo testStep) {
        this.testSteps.add(testStep);
    }

    public void clearTestSteps() {
        this.testSteps.clear();
    }

    public void clearGroups() {
        this.groups.clear();
    }

    public Set<ReferenceDataItem> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public void setGroups(Set<ReferenceDataItem> group) {
        this.groups = group;
    }

    public void addGroup(ReferenceDataItem group) {
        if (group != null) {
            this.groups.add(group);
        }
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ReferenceDataItem getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ReferenceDataItem executionType) {
        this.executionType = executionType;
    }

    public ReferenceDataItem getAutomationCandidate() {
        return automationCandidate;
    }

    public void setAutomationCandidate(ReferenceDataItem automationCandidate) {
        this.automationCandidate = automationCandidate;
    }

    public List<VersionModification> getModifications() {
        return modifications;
    }

    public void setModifications(List<VersionModification> modifications) {
        this.modifications = modifications;
    }

    public ReferenceDataItem getTestCaseStatus() {
        return testCaseStatus;
    }

    public void setTestCaseStatus(ReferenceDataItem testCaseStatus) {
        this.testCaseStatus = testCaseStatus;
    }

    public List<TestCampaignInfo> getAssociatedTestCampaigns() {
        return associatedTestCampaigns;
    }

    public void setAssociatedTestCampaigns(List<TestCampaignInfo> associatedTestCampaigns) {
        this.associatedTestCampaigns = associatedTestCampaigns;
    }

    public void setContexts(Set<ReferenceDataItem> contexts) {
        this.contexts = contexts;
    }

    public FeatureInfo getFeature() {
        return feature;
    }

    public void setFeature(FeatureInfo feature) {
        this.feature = feature;
    }

    public boolean isIntrusive() {
        return intrusive;
    }

    public void setIntrusive(boolean intrusive) {
        this.intrusive = intrusive;
    }

    public String getIntrusiveComment() {
        return intrusiveComment;
    }

    public void setIntrusiveComment(String intrusiveComment) {
        this.intrusiveComment = intrusiveComment;
    }

    public ReferenceDataItem getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(ReferenceDataItem testSuite) {
        this.testSuite = testSuite;
    }

    public ReferenceDataItem getTestTeam() {
        return testTeam;
    }

    public void setTestTeam(ReferenceDataItem testTeam) {
        this.testTeam = testTeam;
    }

    public ReviewGroupInfo getReviewGroup() {
        return reviewGroup;
    }

    public void setReviewGroup(ReviewGroupInfo reviewGroupInfo) {
        this.reviewGroup = reviewGroupInfo;
    }

    public Set<ReferenceDataItem> getTestCaseVersions() {
        return testCaseVersions;
    }

    public void setTestCaseVersions(Set<ReferenceDataItem> testCaseVersions) {
        this.testCaseVersions = testCaseVersions;
    }

    public UserInfo getReviewUser() {
        return reviewUser;
    }

    public void setReviewUser(UserInfo reviewUser) {
        this.reviewUser = reviewUser;
    }
}
