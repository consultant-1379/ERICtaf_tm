package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;

public class ProjectBuilder extends EntityBuilder<Project> {

    public ProjectBuilder() {
        super(new Project());
    }

    public ProjectBuilder withId(long id) {
        entity.setId(id);
        return this;
    }

    public ProjectBuilder withExternalId(String externalId) {
        entity.setExternalId(externalId);
        return this;
    }

    public ProjectBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public ProjectBuilder withProduct(Product product) {
        product.addProject(entity);
        return this;
    }
}
