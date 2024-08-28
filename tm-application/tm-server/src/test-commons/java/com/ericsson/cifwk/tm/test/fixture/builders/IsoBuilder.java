package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;

public class IsoBuilder extends EntityBuilder<ISO> {

    public IsoBuilder() {
        super(new ISO());
    }

    public IsoBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public IsoBuilder withVersion(String version) {
        entity.setVersion(version);
        return this;
    }

    public IsoBuilder withDrop(Drop drop) {
        entity.addDrop(drop);
        return this;
    }
}
