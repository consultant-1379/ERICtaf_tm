package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;

public class TestTypeBuilder extends EntityBuilder<TestType> {

    public TestTypeBuilder() {
        super(new TestType());
    }

    public TestTypeBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public TestTypeBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }

    public TestTypeBuilder withProduct(Product product) {
        entity.setProduct(product);
        return this;
    }
}
