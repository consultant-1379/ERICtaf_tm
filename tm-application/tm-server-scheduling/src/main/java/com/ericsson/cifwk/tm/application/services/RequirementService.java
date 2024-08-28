/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.RequirementServiceImpl;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Set;

@ImplementedBy(RequirementServiceImpl.class)
public interface RequirementService {

    Requirement save(Requirement requirementEntity);

    Requirement findOrImport(String externalId);

    List<Requirement> findAllOrImport(Set<String> externalIds);

    void fetchAndSaveUpdatedRequirements();

    void fetchAndSaveUpdatedRequirements(String jql);

}
