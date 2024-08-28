package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;

public class ProductBuilder extends EntityBuilder<Product> {

    public ProductBuilder() {
        super(new Product());
    }

    public ProductBuilder withId(long id) {
        entity.setId(id);
        return this;
    }

    public ProductBuilder withExternalId(String externalId) {
        entity.setExternalId(externalId);
        return this;
    }

    public ProductBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public ProductBuilder addProject(Project project) {
        entity.addProject(project);
        return this;
    }

}
