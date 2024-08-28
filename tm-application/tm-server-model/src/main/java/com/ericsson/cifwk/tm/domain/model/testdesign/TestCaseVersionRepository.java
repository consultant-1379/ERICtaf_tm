/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestCaseVersionRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(TestCaseVersionRepositoryImpl.class)
public interface TestCaseVersionRepository extends BaseRepository<TestCaseVersion, Long> {

    List<TestCaseVersion> findByTestCase(String testCaseId);

    TestCaseVersion findByTestCaseAndSequence(long id, long majorVersion, long minorVersion);

    TestCaseVersion findByTestCaseAndSequence(String testCaseId, long majorVersion, long minorVersion);

}
