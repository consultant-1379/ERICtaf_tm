package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.VerifyStepExecution;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;

public class VerifyStepExecutionBuilder extends EntityBuilder<VerifyStepExecution> {

    public VerifyStepExecutionBuilder() {
        super(new VerifyStepExecution());
    }

    public VerifyStepExecutionBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }

    public VerifyStepExecutionBuilder withVerifyStep(VerifyStep verifyStep) {
        entity.setVerifyStep(verifyStep);
        return this;
    }

    public VerifyStepExecutionBuilder withTestExecution(TestExecution testExecution) {
        entity.setTestExecution(testExecution);
        return this;
    }

    public VerifyStepExecutionBuilder withActualResult(String result) {
        entity.setActualResult(result);
        return this;
    }
}
