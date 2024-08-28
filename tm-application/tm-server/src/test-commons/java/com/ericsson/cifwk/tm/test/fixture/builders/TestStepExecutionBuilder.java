package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepResult;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;

public class TestStepExecutionBuilder extends EntityBuilder<TestStepExecution> {

    public TestStepExecutionBuilder() {
        super(new TestStepExecution());
    }

    public TestStepExecutionBuilder withTestStep(TestStep testStep) {
        entity.setTestStep(testStep);
        return this;
    }

    public TestStepExecutionBuilder withTestExecution(TestExecution testExecution) {
        entity.setTestExecution(testExecution);
        return this;
    }

    public TestStepExecutionBuilder withResult(TestStepResult testStepResult) {
        entity.setResult(testStepResult);
        return this;
    }

    public TestStepExecutionBuilder withData(String data) {
        entity.setData(data);
        return this;
    }

    public TestStepExecutionBuilder withId(long id) {
        entity.setId(id);
        return this;
    }
}
