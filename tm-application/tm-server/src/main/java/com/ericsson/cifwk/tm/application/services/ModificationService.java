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

import com.ericsson.cifwk.tm.application.services.impl.ModificationServiceImpl;
import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.Modification;
import com.ericsson.cifwk.tm.presentation.dto.VersionModification;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(ModificationServiceImpl.class)
public interface ModificationService {

    List<Modification> getModifications(Identifiable<?> entity);

    List<VersionModification> getVersionModifications(TestCaseVersion testCaseVersion);

}
