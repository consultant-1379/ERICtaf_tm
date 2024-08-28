package com.ericsson.cifwk.tm.infrastructure.toggler;

import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggle;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggleRepository;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.inject.persist.Transactional;
import org.togglz.core.Feature;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.StateRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collections;
import java.util.Map;

/**
 * @author ebuzdmi
 */
public class HibernateStateRepository implements StateRepository {

    @Inject
    private Provider<FeatureToggleRepository> featureToggleRepositoryProvider;

    @Override
    public FeatureState getFeatureState(Feature feature) {
        String name = feature.name();
        FeatureToggleRepository featureToggleRepository = featureToggleRepositoryProvider.get();
        FeatureToggle featureToggle = featureToggleRepository.find(name);
        if (featureToggle == null) {
            return null;
        }
        FeatureState state = new FeatureState(feature);
        state.setEnabled(featureToggle.getFeatureEnabled());
        state.setStrategyId(featureToggle.getStrategyId());
        Map<String, String> map = deserialize(featureToggle.getStrategyParams());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            state.setParameter(entry.getKey(), entry.getValue());
        }
        return state;
    }

    @Override
    @Transactional
    public void setFeatureState(FeatureState featureState) {
        Feature feature = featureState.getFeature();
        FeatureToggle featureToggle = new FeatureToggle();
        featureToggle.setFeatureName(feature.name());
        featureToggle.setFeatureEnabled(featureState.isEnabled());
        featureToggle.setStrategyId(featureState.getStrategyId());
        featureToggle.setStrategyParams(serialize(featureState.getParameterMap()));
        FeatureToggleRepository featureToggleRepository = featureToggleRepositoryProvider.get();
        featureToggleRepository.merge(featureToggle);
    }

    private Map<String, String> deserialize(String strategyParams) {
        if (Strings.isNullOrEmpty(strategyParams)) {
            return Collections.emptyMap();
        }
        Splitter.MapSplitter splitter = Splitter.on('&').withKeyValueSeparator("=");
        return splitter.split(strategyParams);
    }

    private String serialize(Map<String, String> map) {
        if (map.isEmpty()) {
            return "";
        }
        Joiner.MapJoiner mapJoiner = Joiner.on('&').withKeyValueSeparator("=");
        return mapJoiner.join(map);
    }

}
