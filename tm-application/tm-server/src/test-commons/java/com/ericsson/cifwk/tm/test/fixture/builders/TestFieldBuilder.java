package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.testdesign.FieldType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;

public class TestFieldBuilder extends EntityBuilder<TestField> {

    public TestFieldBuilder() {
        super(new TestField());
    }

    public TestFieldBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public TestFieldBuilder withFieldType(FieldType fieldType) {
        entity.setField(fieldType);
        return this;
    }

    public TestFieldBuilder withValue(String value) {
        entity.setValue(value);
        return this;
    }
}
