/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.management;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.trs.TrsJobRecord;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.Sets;
import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Audited
@Table(name = "TEST_PLANS")
public class TestCampaign extends AuditedEntity implements NamedWithId<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "environment")
    private String environment;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "system_version")
    private String systemVersion;

    @Column(name = "auto_create")
    private boolean autoCreate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "testCampaign", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Filter(name = "deletedEntityFilter")
    private Set<TestCampaignItem> testCampaignItems = Sets.newHashSet();

    @OneToMany(mappedBy = "testCampaign", fetch = FetchType.LAZY)
    private Set<TestExecution> executions = Sets.newHashSet();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project; //TODO: determine what to do with this

    @ManyToOne
    @JoinColumn(name = "drop_id")
    private Drop drop;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "TEST_CAMPAIGN_FEATURES",
            joinColumns = {@JoinColumn(name = "test_campaign_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "feature_id", referencedColumnName = "id")})
    private Set<ProductFeature> features = Sets.newHashSet();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "TEST_CAMPAIGNS_TECHNICAL_COMPONENTS",
            joinColumns = {@JoinColumn(name = "test_campaign_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "technical_component_id", referencedColumnName = "id")})
    private Set<TechnicalComponent> components = Sets.newHashSet();

    @Column(name = "locked")
    private boolean locked;

    @ManyToOne
    @JoinColumn(name = "trs_record_id")
    private TrsJobRecord trsJobRecord;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEST_CAMPAIGN_GROUP_TEST_CAMPAIGNS",
            joinColumns = @JoinColumn(name = "test_campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "test_campaign_group_id"))
    @Filter(name = "deletedEntityFilter")
    private Set<TestCampaignGroup> groups = Sets.newHashSet();

    @Column(name = "ps_from")
    private String psFrom;

    @Column(name = "ps_to")
    private String psTo;

    @Column(name = "guide_revision")
    private String guideRevision;

    @Column(name = "sed_revision")
    private String sedRevision;

    @Column(name = "other_dependent_sw")
    private String otherDependentSW;

    @Column(name = "node_type_version")
    private String nodeTypeVersion;

    @Column(name = "sov_status")
    private String sovStatus;

    @Column(name = "comment")
    private String comment;


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

    @Override
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

    public Set<TestExecution> getExecutions() {
        return executions;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Drop getDrop() {
        return drop;
    }

    public void setDrop(Drop drop) {
        this.drop = drop;
    }

    public Set<ProductFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<ProductFeature> features) {
        this.features = features;
    }

    public Set<TechnicalComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<TechnicalComponent> components) {
        this.components = components;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
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

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public Set<TestCampaignItem> getTestCampaignItems() {
        return Collections.unmodifiableSet(testCampaignItems);
    }

    public void addTestCampaignItem(TestCampaignItem testCampaignItem) {
        testCampaignItem.setTestCampaign(this);
        this.testCampaignItems.add(testCampaignItem);
    }

    public void clearTestCampaignItems() {
        this.testCampaignItems.clear();
    }

    public void removeTestCampaignItem(TestCampaignItem testCampaignItem) {
        testCampaignItems.remove(testCampaignItem);
    }

    public void lock() {
        setLocked(true);
    }

    public void unlock() {
        setLocked(false);
    }

    public TrsJobRecord getTrsJobRecord() {
        return trsJobRecord;
    }

    public void setTrsJobRecord(TrsJobRecord trsJobRecord) {
        this.trsJobRecord = trsJobRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestCampaign that = (TestCampaign) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name);
    }


    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public Set<TestCampaignGroup> getGroups() {
        return groups;
    }

    public void setGroup(Set<TestCampaignGroup> groups) {
        this.groups = groups;
    }

    public void clearGroups() {
        groups.clear();
    }

    public void addGroup(List<TestCampaignGroup> groups) {
        this.groups.addAll(groups);
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


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

}
