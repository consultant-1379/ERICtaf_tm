package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.dto.view.TestExecutionView;
import com.ericsson.cifwk.tm.presentation.validation.Defects;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;
import com.ericsson.cifwk.tm.presentation.validation.RequirementId;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestExecutionInfo implements Identifiable<Long> {

    private Long id;

    private String product;

    private Long testPlan;

    private Long testCase;

    private Date createdAt;

    private String author;

    @Size(max=255)
    private String kpiMeasurement;

    @Valid
    @NotNullField("result")
    private ReferenceDataItem result;

    private String comment;

    private String executionTime;

    @Defects
    private Set<String> defectIds = new HashSet<>();

    @RequirementId
    private Set<String> requirementIds = new HashSet<>();

    @JsonView(TestExecutionView.Detailed.class)
    private List<TestStepExecutionInfo> testStepExecutions = new ArrayList<>();

    @JsonView(TestExecutionView.Detailed.class)
    private List<VerifyStepExecutionInfo> verifyStepExecutions = new ArrayList<>();

    private IsoInfo iso;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getTestPlan() {
        return testPlan;
    }

    public void setTestPlan(Long testPlan) {
        this.testPlan = testPlan;
    }

    public Long getTestCase() {
        return testCase;
    }

    public void setTestCase(Long testCase) {
        this.testCase = testCase;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ReferenceDataItem getResult() {
        return result;
    }

    public void setResult(ReferenceDataItem result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<String> getDefectIds() {
        return defectIds;
    }

    public void setDefectIds(Set<String> defectIds) {
        this.defectIds = defectIds;
    }

    public List<TestStepExecutionInfo> getTestStepExecutions() {
        return testStepExecutions;
    }

    public void addTestStepExecution(TestStepExecutionInfo testStepExecutionInfo) {
        this.testStepExecutions.add(testStepExecutionInfo);
    }

    public List<VerifyStepExecutionInfo> getVerifyStepExecutions() {
        return verifyStepExecutions;
    }

    public void addVerifyStepExecution(VerifyStepExecutionInfo verifyStepExecutionInfo) {
        this.verifyStepExecutions.add(verifyStepExecutionInfo);
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public Set<String> getRequirementIds() {
        return requirementIds;
    }

    public void setRequirementIds(Set<String> requirementIds) {
        this.requirementIds = requirementIds;
    }

    public IsoInfo getIso() {
        return iso;
    }

    public void setIso(IsoInfo iso) {
        this.iso = iso;
    }

    public String getKpiMeasurement() {
        return kpiMeasurement;
    }

    public void setKpiMeasurement(String kpiMeasurement) {
        this.kpiMeasurement = kpiMeasurement;
    }
}
