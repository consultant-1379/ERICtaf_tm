package com.ericsson.cifwk.tm.infrastructure.toggler;

import com.google.common.collect.Maps;
import org.togglz.core.Feature;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Set;

/**
 * @author ebuzdmi
 */
@Singleton
public class FeatureTogglerImpl implements FeatureToggler {

    @Override
    public Map<String, Boolean> retrieveFeatures() {
        FeatureManager featureManager = FeatureContext.getFeatureManager();
        Set<Feature> features = featureManager.getFeatures();
        Map<String, Boolean> featureMap = Maps.newHashMap();
        for (Feature feature : features) {
            boolean active = featureManager.isActive(feature);
            featureMap.put(feature.name(), active);
        }
        return featureMap;
    }

}
