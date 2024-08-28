package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult;
import com.ericsson.cifwk.tm.domain.model.execution.TestStepExecution;
import com.ericsson.cifwk.tm.domain.model.execution.VerifyStepExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.users.User;

public class TestExecutionBuilder extends EntityBuilder<TestExecution> {

    public TestExecutionBuilder() {
        super(new TestExecution());
    }

    public TestExecutionBuilder withTestPlan(TestCampaign testCampaign) {
        entity.setTestCampaign(testCampaign);
        return this;
    }

    public TestExecutionBuilder withTestCaseVersion(TestCaseVersion testCaseVersion) {
        entity.setTestCaseVersion(testCaseVersion);
        return this;
    }

    public TestExecutionBuilder withAuthor(User user) {
        entity.setAuthor(user);
        return this;
    }

    public TestExecutionBuilder withResult(TestExecutionResult testExecutionResult) {
        entity.setResult(testExecutionResult);
        return this;
    }

    public TestExecutionBuilder withComment(String comment) {
        entity.setComment(comment);
        return this;
    }

    public TestExecutionBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }

    public TestExecutionBuilder addTestStepExecution(TestStepExecution testStepExecution) {
        entity.addTestStepExecution(testStepExecution);
        return this;
    }

    public TestExecutionBuilder addVerifyStepExecution(VerifyStepExecution verifyStepExecution) {
        entity.addVerifyStepExecution(verifyStepExecution);
        return this;
    }
}
