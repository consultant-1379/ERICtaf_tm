/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;

public class TechnicalComponentBuilder extends EntityBuilder<TechnicalComponent> {

    public TechnicalComponentBuilder() {
        super(new TechnicalComponent());
    }

    public TechnicalComponentBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public TechnicalComponentBuilder withFeature(ProductFeature productFeature) {
        entity.setFeature(productFeature);
        return this;
    }
}
