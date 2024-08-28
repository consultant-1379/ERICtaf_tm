package com.ericsson.cifwk.tm.infrastructure.toggler;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import org.junit.Test;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.util.NamedFeature;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author ebuzdmi
 */
public class HibernateStateRepositoryTest extends BaseServiceLayerTest {

    @Inject
    HibernateStateRepository repository;

    @Test
    public void getNonExisting() {
        FeatureState state = repository.getFeatureState(new NamedFeature("none"));

        assertThat(state, nullValue());
    }

    @Test
    public void store() {
        FeatureState featureState = new FeatureState(new NamedFeature("name"), true);
        featureState.setStrategyId("strategy");
        featureState.setParameter("x", "1");
        repository.setFeatureState(featureState);

        FeatureState result = repository.getFeatureState(new NamedFeature("name"));

        assertThat(result.getFeature().name(), equalTo("name"));
        assertThat(result.getStrategyId(), equalTo("strategy"));
        assertThat(result.getParameter("x"), equalTo("1"));
        assertThat(result.isEnabled(), is(true));
    }

}
