package com.ericsson.cifwk.tm.domain.shared.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggle;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggleRepository;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author ebuzdmi
 */
public class FeatureToggleRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    FeatureToggleRepository repository;

    @Test
    public void shouldStore() {
        FeatureToggle toggle = TestEntityFactory.buildFeatureToggle()
                .withFeatureName("NAME")
                .withFeatureEnabled(Boolean.TRUE)
                .withStrategyId("A")
                .withStrategyParams("B")
                .build();

        persistence.persistInTransaction(toggle);
    }

}
