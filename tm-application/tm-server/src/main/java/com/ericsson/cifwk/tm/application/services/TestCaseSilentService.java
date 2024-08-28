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

import com.ericsson.cifwk.tm.application.services.impl.TestCaseSilentServiceImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.responses.BadRequest;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(TestCaseSilentServiceImpl.class)
public interface TestCaseSilentService {

    List<BadRequest> populate(TestCaseVersion entity, TestCaseInfo testCaseInfo);

}
