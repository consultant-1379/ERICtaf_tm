package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;

import java.util.Date;

public class TestStepExecutionInfo implements Identifiable<Long> {

    private Long id;

    private Long testStep;

    private Long testExecution;

    private Date createdAt;

    private ReferenceDataItem result;

    private String data;

    private int testStepOrderNr;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestStep() {
        return testStep;
    }

    public void setTestStep(Long testStep) {
        this.testStep = testStep;
    }

    public Long getTestExecution() {
        return testExecution;
    }

    public void setTestExecution(Long testExecution) {
        this.testExecution = testExecution;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ReferenceDataItem getResult() {
        return result;
    }

    public void setResult(ReferenceDataItem result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public int getTestStepOrderNr() {
        return testStepOrderNr;
    }

    public void setTestStepOrderNr(int testStepOrderNr) {
        this.testStepOrderNr = testStepOrderNr;
    }
}
