package com.ericsson.cifwk.tm.domain.model.shared;

import com.ericsson.cifwk.tm.domain.model.shared.impl.FeatureToggleRepositoryImpl;
import com.google.inject.ImplementedBy;

@ImplementedBy(FeatureToggleRepositoryImpl.class)
public interface FeatureToggleRepository extends BaseRepository<FeatureToggle, String> {
}
