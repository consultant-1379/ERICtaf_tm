/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.domain.model.shared.AuditedEntity;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.google.common.collect.Sets;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

import static org.hibernate.annotations.LazyCollectionOption.EXTRA;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "REQUIREMENTS")
@BatchSize(size = 20)
public class Requirement extends AuditedEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_id")
    private Requirement parent;

    @OneToMany(mappedBy = "parent")
    @BatchSize(size = 20)
    private Set<Requirement> children = Sets.newHashSet();

    @Column(name = "external_id", unique = true, nullable = false)
    private String externalId;

    @Column(name = "external_type")
    private String externalType;

    @Column(name = "external_deliveredIn")
    private String deliveredIn;

    @Column(name = "external_label")
    private String externalLabel;

    @Column(name = "external_summary")
    private String externalSummary;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "external_statusname")
    private String externalStatusName;

    @ManyToMany
    @JoinTable(name = "REQUIREMENT_FEATURES",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    private Set<Feature> features = Sets.newHashSet();

    @ManyToMany(mappedBy = "requirements")
    @LazyCollection(EXTRA)
    @Filter(name = "deletedEntityFilter")
    private Set<TestCaseVersion> testCaseVersions = Sets.newHashSet();

    public Requirement() {
    }

    public Requirement(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requirement getParent() {
        return parent;
    }

    void setParent(Requirement parent) {
        this.parent = parent;
    }

    public Requirement getRootParent() {
        Requirement rootParent = this.parent;
        if (rootParent == null) {
            return this;
        }
        while (rootParent.hasParent()) {
            rootParent = rootParent.getParent();
        }
        return rootParent;
    }

    public Set<Requirement> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public void addChild(Requirement child) {
        child.setParent(this);
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
    }

    public void removeChild(Requirement child) {
        child.setParent(null);
        this.children.remove(child);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public String getExternalType() {
        return externalType;
    }

    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }

    public String getExternalLabel() {
        return externalLabel;
    }

    public void setExternalLabel(String externalLabel) {
        this.externalLabel = externalLabel;
    }

    public String getExternalSummary() {
        return externalSummary;
    }

    public void setExternalSummary(String externalSummary) {
        this.externalSummary = externalSummary;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getExternalStatusName() {
        return externalStatusName;
    }

    public void setExternalStatusName(String externalStatusName) {
        this.externalStatusName = externalStatusName;
    }

    public Set<TestCaseVersion> getTestCaseVersions() {
        return Collections.unmodifiableSet(testCaseVersions);
    }

    public void addTestCase(TestCaseVersion testCaseVersion) {
        if (!testCaseVersions.contains(testCaseVersion)) {
            testCaseVersions.add(testCaseVersion);
        }
    }

    public void removeTestCase(TestCaseVersion testCaseVersion) {
        if (testCaseVersions.contains(testCaseVersion)) {
            testCaseVersions.remove(testCaseVersion);
            testCaseVersion.removeRequirement(this);
        }
    }

    private boolean hasParent() {
        return parent != null;
    }

    public String getDeliveredIn() {
        return deliveredIn;
    }

    public void setDeliveredIn(String deliveredIn) {
        this.deliveredIn = deliveredIn;
    }
}
