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

import com.ericsson.cifwk.tm.application.services.impl.DefectServiceImpl;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Set;

@ImplementedBy(DefectServiceImpl.class)
public interface DefectService {

    List<Defect> findAll(Set<String> defectIds);

    void fetchAndSaveUpdatedDefects();

    void fetchAndSaveUpdatedDefects(String jql);
}
