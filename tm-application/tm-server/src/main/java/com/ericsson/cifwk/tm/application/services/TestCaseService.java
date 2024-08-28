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

import com.ericsson.cifwk.tm.application.services.impl.TestCaseServiceImpl;
import com.ericsson.cifwk.tm.application.services.validation.ServiceValidationException;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.inject.ImplementedBy;

@ImplementedBy(TestCaseServiceImpl.class)
public interface TestCaseService {

    void populate(TestCaseVersion entity, TestCaseInfo testCaseInfo) throws ServiceValidationException;

    Paginated<TestCase> customSearch(Query query, int page, int perPage);
}
