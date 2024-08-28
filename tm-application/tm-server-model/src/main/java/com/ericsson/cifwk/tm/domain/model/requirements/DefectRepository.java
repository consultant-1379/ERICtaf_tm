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

import com.ericsson.cifwk.tm.domain.model.requirements.impl.DefectRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Set;

@ImplementedBy(DefectRepositoryImpl.class)
public interface DefectRepository extends BaseRepository<Defect, Long> {
    List<Defect> findMatchingExternalId(String search, int limit);

    List<Defect> findAllByExternalId(Set<String> externalIds);

    Defect findByExternalId(String externalId);
}
