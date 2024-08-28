package com.ericsson.cifwk.tm.test.fixture.builders;

import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggle;

/**
 * @author ebuzdmi
 */
public class FeatureToggleBuilder extends EntityBuilder<FeatureToggle> {

    public FeatureToggleBuilder() {
        super(new FeatureToggle());
    }

    public FeatureToggleBuilder withFeatureName(String featureName) {
        entity.setFeatureName(featureName);
        return this;
    }

    public FeatureToggleBuilder withFeatureEnabled(Boolean featureEnabled) {
        entity.setFeatureEnabled(featureEnabled);
        return this;
    }

    public FeatureToggleBuilder withStrategyId(String strategyId) {
        entity.setStrategyId(strategyId);
        return this;
    }

    public FeatureToggleBuilder withStrategyParams(String strategyParams) {
        entity.setStrategyParams(strategyParams);
        return this;
    }

}
