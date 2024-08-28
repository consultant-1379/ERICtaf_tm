/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.validation.ExistingProject;
import com.ericsson.cifwk.tm.presentation.validation.FixedSizeField;
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;
import com.ericsson.cifwk.tm.presentation.validation.RangeFields;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.Valid;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@RangeFields(from = "startDate", to = "endDate")
public class TestCampaignInfo implements Identifiable<Long> {

    private Long id;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Long parentId;

    @NotEmptyField("name")
    @FixedSizeField("name")
    private String name;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String description;

    @FixedSizeField("environment")
    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String environment;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private ProductInfo product;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private DropInfo drop;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Set<FeatureInfo> features;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Set<TechnicalComponentInfo> components = new LinkedHashSet<>();

    @FixedSizeField("systemVersion")
    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String systemVersion;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Date startDate;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Date endDate;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String hostname;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private boolean locked;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private boolean autoCreate;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private UserInfo author;

    @Valid
    @JsonView(TestCampaignView.Detailed.class)
    private Set<TestCampaignItemInfo> testCampaignItems = new LinkedHashSet<>();

    @Valid
    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private Set<ReferenceDataItem> groups = new LinkedHashSet<>();

    @ExistingProject
    @JsonView(TestCampaignView.Detailed.class)
    private ProjectInfo project;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String psFrom;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String psTo;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String guideRevision;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String sedRevision;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String otherDependentSW;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String nodeTypeVersion;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String sovStatus;

    @JsonView({TestCampaignView.Simple.class, TestCampaignView.Detailed.class})
    private String comment;

    public String getPsFrom() {
        return psFrom;
    }

    public void setPsFrom(String psFrom) {
        this.psFrom = psFrom;
    }

    public String getPsTo() {
        return psTo;
    }

    public void setPsTo(String psTo) {
        this.psTo = psTo;
    }

    public String getGuideRevision() {
        return guideRevision;
    }

    public void setGuideRevision(String guideRevision) {
        this.guideRevision = guideRevision;
    }

    public String getSedRevision() {
        return sedRevision;
    }

    public void setSedRevision(String sedRevision) {
        this.sedRevision = sedRevision;
    }

    public String getOtherDependentSW() {
        return otherDependentSW;
    }

    public void setOtherDependentSW(String otherDependentSW) {
        this.otherDependentSW = otherDependentSW;
    }

    public String getNodeTypeVersion() {
        return nodeTypeVersion;
    }

    public void setNodeTypeVersion(String nodeTypeVersion) {
        this.nodeTypeVersion = nodeTypeVersion;
    }

    public String getSovStatus() { return sovStatus; }

    public void setSovStatus(String sovStatus) { this.sovStatus = sovStatus; }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public DropInfo getDrop() {
        return drop;
    }

    public void setDrop(DropInfo drop) {
        this.drop = drop;
    }

    public Set<FeatureInfo> getFeatures() {
        return features;
    }

    public void setFeatures(Set<FeatureInfo> features) {
        this.features = features;
    }

    public Set<TechnicalComponentInfo> getComponents() {
        return components;
    }

    public void setComponents(Set<TechnicalComponentInfo> components) {
        this.components = components;
    }

    public Set<TestCampaignItemInfo> getTestCampaignItems() {
        return testCampaignItems;
    }

    public void setTestCampaignItems(Set<TestCampaignItemInfo> testCampaignItems) {
        this.testCampaignItems = testCampaignItems;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestCampaignInfo that = (TestCampaignInfo) o;

        if (environment != null ? !environment.equals(that.environment) : that.environment != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (systemVersion != null ? !systemVersion.equals(that.systemVersion) : that.systemVersion != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (systemVersion != null ? systemVersion.hashCode() : 0);
        return result;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public Set<ReferenceDataItem> getGroups() {
        return groups;
    }

    public void setGroups(Set<ReferenceDataItem> groups) {
        this.groups = groups;
    }

    public void addGroup(ReferenceDataItem referenceDataItem) {
        if (referenceDataItem != null) {
            this.groups.add(referenceDataItem);
        }
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }
}
