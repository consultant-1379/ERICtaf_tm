package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Repository
public class TestCaseRepositoryImpl extends BaseRepositoryImpl<TestCase, Long> implements TestCaseRepository {

    public static final String TEST_CASE_ID = "testCaseId";
    public static final String PRODUCT = "currentVersion.requirements.project.product.id";

    @Override
    public TestCase findByAnyId(String testId) {
        try {
            return find(Long.valueOf(testId));
        } catch (NumberFormatException e) {
            return findByExternalId(testId);
        }
    }

    @Override
    public TestCase findByExternalId(String externalId) {
        Search search = new Search(TestCase.class);
        search.addFilterEqual(TEST_CASE_ID, externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Collection<String> findTestCaseIds(Collection<String> testCaseIds) {
        Search search = new Search(TestCase.class);
        search.addField(TEST_CASE_ID);
        search.addFilterIn(TEST_CASE_ID, testCaseIds);
        search.setResultMode(ISearch.RESULT_SINGLE);
        return search(search);
    }

    @Override
    public int findTotalByProduct(Long productId) {
        Search search = new Search(TestCase.class);
        search.addFilterEqual(PRODUCT, productId);
        search.setMaxResults(1);
        search.setDistinct(true);
        SearchResult<Object> objectSearchResult = searchAndCount(search);
        return objectSearchResult.getTotalCount();
    }

    @Override
    public List<TestCase> findMatchingTestCaseId(Long productId, List<Long> featureIds, List<Long> componentIds, String testCaseId, int limit) {
        Search search = new Search(TestCase.class);
        search.addFilterEqual("currentVersion.productFeature.product.id", productId);
        if (featureIds != null) {
            search.addFilterIn("currentVersion.productFeature.id", featureIds);
        }
        if (!componentIds.isEmpty()) {
            search.addFilterSome("currentVersion.technicalComponents", Filter.in("id", componentIds));
        }
        search.addFilterILike(TEST_CASE_ID, SearchHelpers.toLikePattern(testCaseId));
        search.addSortAsc(TEST_CASE_ID);
        search.setMaxResults(limit);
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<TestCase> findByRequirementId(String requirementId) {
        Search search = searchByRequirementId(requirementId);
        return this.search(search);
    }

    private Search searchByRequirementId(String requirementId) {
        Search search = new Search(TestCase.class);
        search.addFilterEqual("currentVersion.requirements.externalId", requirementId);
        return search;
    }

}
