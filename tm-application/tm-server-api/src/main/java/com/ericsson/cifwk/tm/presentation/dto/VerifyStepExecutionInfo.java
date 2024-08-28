package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;

import java.util.Date;

public class VerifyStepExecutionInfo implements Identifiable<Long> {

    private Long id;

    private Long testStep;

    private Long verifyStep;

    private Long testExecution;

    private Date createdAt;

    private String actualResult;

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

    public Long getVerifyStep() {
        return verifyStep;
    }

    public void setVerifyStep(Long verifyStep) {
        this.verifyStep = verifyStep;
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

    public String getActualResult() {
        return actualResult;
    }

    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }
}
