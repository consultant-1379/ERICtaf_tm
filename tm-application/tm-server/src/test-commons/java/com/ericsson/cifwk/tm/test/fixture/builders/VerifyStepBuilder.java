package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;

public class VerifyStepBuilder extends EntityBuilder<VerifyStep> {

    public VerifyStepBuilder() {
        super(new VerifyStep());
    }

    public VerifyStepBuilder withVerifyStep(String verifyStep) {
        entity.setVerifyStep(verifyStep);
        return this;
    }

    public VerifyStepBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }

    public VerifyStepBuilder withTestStep(TestStep testStep) {
        testStep.addVerification(entity);
        return this;
    }
}
