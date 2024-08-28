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

import com.ericsson.cifwk.tm.domain.model.requirements.impl.ProjectRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.Map;

@ImplementedBy(ProjectRepositoryImpl.class)
public interface ProjectRepository extends BaseRepository<Project, Long> {

    Project findByExternalId(String externalId);

    Project findByExternalIdAndName(String externalId, String name);

    Map<Long, String> findAllIdsToExternalIds();
}
