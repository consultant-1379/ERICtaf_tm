package com.ericsson.cifwk.tm.infrastructure.toggler;

import com.google.inject.ImplementedBy;

import java.util.Map;

/**
 * @author ebuzdmi
 */
@ImplementedBy(FeatureTogglerImpl.class)
public interface FeatureToggler {

    Map<String, Boolean> retrieveFeatures();

}
