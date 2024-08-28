package com.ericsson.cifwk.tm.domain.model.shared.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggle;
import com.ericsson.cifwk.tm.domain.model.shared.FeatureToggleRepository;

@Repository
public class FeatureToggleRepositoryImpl
        extends BaseRepositoryImpl<FeatureToggle, String> implements FeatureToggleRepository {

}
