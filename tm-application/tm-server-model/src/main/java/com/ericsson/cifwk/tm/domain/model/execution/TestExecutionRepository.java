/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.execution;

import com.ericsson.cifwk.tm.domain.model.execution.impl.TestExecutionRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(TestExecutionRepositoryImpl.class)
public interface TestExecutionRepository extends BaseRepository<TestExecution, Long> {

    List<TestExecution> findByTestPlan(long testPlanId);

    TestExecution findLatestByTestPlanAndTestCase(long testPlanId, String testCaseId);

    Paginated<TestExecution> findByTestPlanAndTestCase(long testPlanId, String testCaseId, int page, int perPage);

    List<TestExecution> findUnrecordedExecutions();

    void save(List<TestExecution> testExecutions);

    boolean isLatestForTestCaseAndIso(TestExecution testExecution);
}
