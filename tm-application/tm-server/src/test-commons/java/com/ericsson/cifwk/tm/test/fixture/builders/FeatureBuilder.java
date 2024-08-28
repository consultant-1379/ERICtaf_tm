package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;

import java.util.Set;

public class FeatureBuilder extends EntityBuilder<ProductFeature> {

    public FeatureBuilder() {
        super(new ProductFeature());
    }

    public FeatureBuilder withId(long id) {
        entity.setId(id);
        return this;
    }

    public FeatureBuilder withProduct(Product product) {
        entity.setProduct(product);
        return this;
    }

    public FeatureBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public FeatureBuilder withComponents(Set<TechnicalComponent> components) {
        entity.setComponents(components);
        return this;
    }
}