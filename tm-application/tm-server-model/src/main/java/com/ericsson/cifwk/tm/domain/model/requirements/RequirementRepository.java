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

import com.ericsson.cifwk.tm.domain.model.requirements.impl.RequirementRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Set;

@ImplementedBy(RequirementRepositoryImpl.class)
public interface RequirementRepository extends BaseRepository<Requirement, Long> {

    Requirement findByExternalId(String externalId);

    Requirement findByExternalIdAndProjectId(String externalId, String projectId);

    List<Requirement> findAllByExternalId(Set<String> externalIds);

    List<Requirement> findTopLevel(String projectId);

    List<String> getAllJiraExternalIds();

    List<Requirement> findMatchingExternalId(String externalId, List<String> filterType, int limit);

}
