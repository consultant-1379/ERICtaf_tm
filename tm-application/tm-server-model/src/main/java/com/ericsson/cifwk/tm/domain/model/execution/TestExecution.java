package com.ericsson.cifwk.tm.domain.model.execution;

import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.shared.ReferenceHelper;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.google.common.collect.Sets;
import org.hibernate.envers.Audited;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Audited
@Table(name = "TEST_EXECUTIONS")
public class TestExecution implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_plan_id")
    private TestCampaign testCampaign;

    @ManyToOne
    @JoinColumn(name = "test_case_id")
    private TestCaseVersion testCaseVersion;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "result")
    private Integer result;

    @Column(name = "comment")
    private String comment;

    @Column(name = "execution_time")
    private String executionTime;

    @Column(name = "kpi_measurement")
    private String kpiMeasurement;

    @ManyToMany
    @JoinTable(name = "DEFECTS_TEST_EXECUTIONS",
            joinColumns = @JoinColumn(name = "test_execution_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "defect_id", referencedColumnName = "id"))
    private Set<Defect> defects = Sets.newHashSet();

    @ManyToMany
    @JoinTable(name = "REQUIREMENTS_TEST_EXECUTIONS",
            joinColumns = @JoinColumn(name = "test_execution_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "requirement_id", referencedColumnName = "id"))
    private Set<Requirement> requirements = Sets.newHashSet();

    @OneToMany(mappedBy = "testExecution", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TestStepExecution> testStepExecutions = Sets.newLinkedHashSet();

    @OneToMany(mappedBy = "testExecution", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<VerifyStepExecution> verifyStepExecutions = Sets.newLinkedHashSet();

    @ManyToOne
    @JoinColumn(name = "iso_id")
    private ISO iso;

    @Column(name = "recorded_in_trs")
    private boolean recordedInTrs;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = new Date();
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCampaign getTestCampaign() {
        return testCampaign;
    }

    public void setTestCampaign(TestCampaign testCampaign) {
        this.testCampaign = testCampaign;
    }

    public TestCaseVersion getTestCaseVersion() {
        return testCaseVersion;
    }

    public void setTestCaseVersion(TestCaseVersion testCaseVersion) {
        this.testCaseVersion = testCaseVersion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public TestExecutionResult getResult() {
        return ReferenceHelper.parseEnumById(this.result, TestExecutionResult.class);
    }

    public void setResult(TestExecutionResult result) {
        this.result = result.getId();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Defect> getDefects() {
        return Collections.unmodifiableSet(defects);
    }

    public void addDefect(Defect defect) {
        if (!defects.contains(defect)) {
            defects.add(defect);
        }
    }

    public void addDefects(List<Defect> defects) {
        this.defects.addAll(defects);
    }

    public Set<TestStepExecution> getTestStepExecutions() {
        return testStepExecutions;
    }

    public void setTestStepExecutions(Set<TestStepExecution> testStepExecutions) {
        this.testStepExecutions = testStepExecutions;
    }

    public void addTestStepExecution(TestStepExecution testStepExecution) {
        testStepExecutions.add(testStepExecution);
        testStepExecution.setTestExecution(this);
    }

    public Set<VerifyStepExecution> getVerifyStepExecutions() {
        return verifyStepExecutions;
    }

    public void setVerifyStepExecutions(Set<VerifyStepExecution> verifyStepExecutions) {
        this.verifyStepExecutions = verifyStepExecutions;
    }

    public void addVerifyStepExecution(VerifyStepExecution verifyStepExecution) {
        verifyStepExecutions.add(verifyStepExecution);
        verifyStepExecution.setTestExecution(this);
    }

    public ISO getIso() {
        return iso;
    }

    public void setIso(ISO iso) {
        this.iso = iso;
    }

    public boolean isRecordedInTrs() {
        return recordedInTrs;
    }

    public void setRecordedInTrs(boolean recordedInTrs) {
        this.recordedInTrs = recordedInTrs;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public void addRequirement(Requirement requirement) {
        if (!requirements.contains(requirement)) {
            requirements.add(requirement);
        }
    }

    public void addRequirements(List<Requirement> defects) {
        this.requirements.addAll(defects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestExecution that = (TestExecution) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(testCampaign, that.testCampaign) &&
                Objects.equals(testCaseVersion, that.testCaseVersion) &&
                Objects.equals(iso, that.iso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, testCampaign, testCaseVersion, iso);
    }

    public String getKpiMeasurement() {
        return kpiMeasurement;
    }

    public void setKpiMeasurement(String kpiMeasurement) {
        this.kpiMeasurement = kpiMeasurement;
    }
}
