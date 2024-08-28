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

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;

public class ScopeBuilder extends EntityBuilder<Scope> {

    public ScopeBuilder() {
        super(new Scope());
    }

    public ScopeBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public ScopeBuilder withProduct(Product product) {
        entity.setProduct(product);
        return this;
    }

    public ScopeBuilder withEnabled(boolean enabled) {
        entity.setEnabled(enabled);
        return this;
    }
}
