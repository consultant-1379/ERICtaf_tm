package com.ericsson.cifwk.tm.domain.model.execution.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import java.util.List;

@Repository
public class TestExecutionRepositoryImpl extends BaseRepositoryImpl<TestExecution, Long> implements TestExecutionRepository {

    @Override
    public List<TestExecution> findByTestPlan(long testPlanId) {
        Search search = new Search(TestExecution.class);
        search.addFilterEqual("testCampaign.id", testPlanId);
        search.addSortDesc("createdAt");
        return this.search(search);
    }

    @Override
    public TestExecution findLatestByTestPlanAndTestCase(long testPlanId, String testCaseId) {
        Search search = searchByTestPlanAndTestCase(testPlanId, testCaseId);
        search.setMaxResults(1);
        return this.searchUnique(search);
    }

    @Override
    public Paginated<TestExecution> findByTestPlanAndTestCase(long testPlanId,
                                                              String testCaseId,
                                                              int page,
                                                              int perPage) {
        Search search = searchByTestPlanAndTestCase(testPlanId, testCaseId);
        return searchPaginated(search, page, perPage);
    }

    @Override
    public List<TestExecution> findUnrecordedExecutions() {
        Search search = new Search(TestExecution.class);
        search.addFilterEqual("recordedInTrs", false);
        return this.search(search);
    }

    @Override
    public void save(List<TestExecution> testExecutions) {
        TestExecution[] testExecutionsArray = new TestExecution[testExecutions.size()];
        this.save(testExecutions.toArray(testExecutionsArray));
    }

    @Override
    public boolean isLatestForTestCaseAndIso(TestExecution testExecution) {
        Search search = new Search(TestExecution.class);
        search.addFilterEqual("testCaseVersion.testCase", testExecution.getTestCaseVersion().getTestCase());
        search.addFilterEqual("iso", testExecution.getIso());
        search.addFilterGreaterThan("createdAt", testExecution.getCreatedAt());
        List<TestExecution> laterExecutions = this.search(search);
        return laterExecutions.isEmpty();
    }

    private Search searchByTestPlanAndTestCase(long testPlanId, String testCaseId) {
        Search search = new Search(TestExecution.class);
        search.addFilterEqual("testCampaign.id", testPlanId);
        try {
            long id = Long.parseLong(testCaseId);
            search.addFilterEqual("testCaseVersion.testCase.id", id);
        } catch (NumberFormatException e) {
            search.addFilterEqual("testCaseVersion.testCase.testCaseId", testCaseId);
        }
        search.addSortDesc("createdAt");
        return search;
    }

}
