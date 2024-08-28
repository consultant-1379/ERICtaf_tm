/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements;

import com.ericsson.cifwk.tm.domain.model.requirements.impl.TechnicalComponentRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(TechnicalComponentRepositoryImpl.class)
public interface TechnicalComponentRepository extends BaseRepository<TechnicalComponent, Long> {
    int countForProduct(Long productId);

    TechnicalComponent findByName(String name, Long productId);

    List<TechnicalComponent> findByFeatureIds(List<Long> featureIds);

    TechnicalComponent findByFeatureAndName(Long featureId, String name);

    List<TechnicalComponent> findByIds(List<Long> ids);
}
