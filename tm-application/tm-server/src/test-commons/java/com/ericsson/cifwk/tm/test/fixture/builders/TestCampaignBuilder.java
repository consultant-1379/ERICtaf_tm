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

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.google.common.collect.Sets;

import java.util.Set;

public class TestCampaignBuilder extends EntityBuilder<TestCampaign> {

    public TestCampaignBuilder() {
        super(new TestCampaign());
    }

    public TestCampaignBuilder withName(String name) {
        entity.setName(name);
        return this;
    }

    public TestCampaignBuilder withDescription(String description) {
        entity.setDescription(description);
        return this;
    }

    public TestCampaignBuilder withEnvironment(String environment) {
        entity.setEnvironment(environment);
        return this;
    }

    public TestCampaignBuilder withId(Long id) {
        entity.setId(id);
        return this;
    }

    public TestCampaignBuilder addTestPlanItem(TestCampaignItem testCampaignItem) {
        entity.addTestCampaignItem(testCampaignItem);
        return this;
    }

    public TestCampaignBuilder withProject(Project project) {
        entity.setProject(project);
        return this;
    }

    public TestCampaignBuilder withDrop(Drop drop) {
        entity.setDrop(drop);
        return this;
    }

    public TestCampaignBuilder withFeature(ProductFeature feature) {
        Set<ProductFeature> features = Sets.newHashSet(feature);
        entity.setFeatures(features);
        return this;
    }

    public TestCampaignBuilder withFeatures(Set<ProductFeature> features) {
        entity.setFeatures(features);
        return this;
    }

    public TestCampaignBuilder withComponents(Set<TechnicalComponent> components) {
        entity.setComponents(components);
        return this;
    }
}
