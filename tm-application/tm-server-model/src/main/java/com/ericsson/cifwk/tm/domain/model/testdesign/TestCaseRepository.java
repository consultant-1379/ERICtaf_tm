package com.ericsson.cifwk.tm.domain.model.testdesign;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestCaseRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.Collection;
import java.util.List;

/**
 *
 */
@ImplementedBy(TestCaseRepositoryImpl.class)
public interface TestCaseRepository extends BaseRepository<TestCase, Long> {

    TestCase findByAnyId(String testId);

    TestCase findByExternalId(String externalId);

    List<TestCase> findMatchingTestCaseId(Long productId, List<Long> featureId, List<Long> componentIds, String testCaseId, int limit);

    List<TestCase> findByRequirementId(String requirementId);

    Collection<String> findTestCaseIds(Collection<String> externalIds);

    int findTotalByProduct(Long productId);
}
