/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.requirements.Feature;
import com.ericsson.cifwk.tm.domain.model.requirements.FeatureRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;

@Repository
public class FeatureRepositoryImpl extends BaseRepositoryImpl<Feature, Long> implements FeatureRepository {
}
