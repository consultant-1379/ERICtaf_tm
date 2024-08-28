package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;

import java.util.List;

public class DropBuilder extends EntityBuilder<Drop> {

    public DropBuilder() {
        super(new Drop());
    }

    public DropBuilder withProduct(Product product) {
        entity.setProduct(product);
        return this;
    }

    public DropBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public DropBuilder withIsos(List<ISO> isos) {
        entity.addIsos(isos);
        return this;
    }
}