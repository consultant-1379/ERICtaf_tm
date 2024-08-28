package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;

public class TestStepBuilder extends EntityBuilder<TestStep> {

    public TestStepBuilder() {
        super(new TestStep());
    }

    public TestStepBuilder withExecute(String execute) {
        entity.setTitle(execute);
        return this;
    }

    public TestStepBuilder withComment(String comment) {
        entity.setComment(comment);
        return this;
    }

    public TestStepBuilder withData(String data) {
        entity.setData(data);
        return this;
    }

    public TestStepBuilder addVerifyStep(VerifyStep verifyStep) {
        entity.addVerification(verifyStep);
        return this;
    }

    public TestStepBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }
}
