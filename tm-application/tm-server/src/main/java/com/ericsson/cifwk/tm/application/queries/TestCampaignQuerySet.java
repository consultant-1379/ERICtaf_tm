/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.params.TestCampaignCriteria;
import com.ericsson.cifwk.tm.application.queries.csv.TestCaseCSVCreator;
import com.ericsson.cifwk.tm.application.queries.helper.TestCampaignQuerySetHelper;
import com.ericsson.cifwk.tm.application.services.TestCampaignService;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignItemMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestExecutionMapper;
import com.ericsson.cifwk.tm.integration.fileStorage.FileService;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.responses.CompletionHelper;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@QuerySet
public class TestCampaignQuerySet {

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Inject
    private TestCampaignItemRepository testCampaignItemRepository;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private TestCampaignService testCampaignService;

    @Inject
    private TestCampaignMapper testCampaignMapper;

    @Inject
    private TestCampaignItemMapper testCampaignItemMapper;

    @Inject
    private TestExecutionRepository testExecutionRepository;

    @Inject
    private TestExecutionMapper testExecutionMapper;

    @Inject
    private FileService fileService;

    @Inject
    private TestCampaignQuerySetHelper testCampaignQuerySetHelper;

    public static final String SEPARATOR = ",";

    public Response getTestPlan(
            Long testPlanId,
            Class<? extends DtoView<TestCampaignInfo>> view) {
        TestCampaign testCampaign = testCampaignRepository.find(testPlanId);
        if (testCampaign != null && testCampaign.isDeleted()) {
            return Responses.notFound();
        }
        TestCampaignInfo testCampaignInfo = testCampaignMapper.mapEntity(testCampaign, TestCampaignInfo.class, view);
        return Responses.nullable(testCampaignInfo);
    }

    //TODO: remove
    public Response getTestPlansByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo,
            final Class<? extends DtoView<TestCampaignInfo>> view) {
        Paginated<TestCampaign> paginated = testCampaignService.customSearch(query, page, perPage);

        Map<String, QueryField> testPlanFields = searchMapping.getTestPlanFields();
        QueryInfo queryInfo = query.convertToQueryInfo(testPlanFields);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestCampaign, TestCampaignInfo>() {
                    @Override
                    public TestCampaignInfo apply(TestCampaign testCampaign) {
                        return testCampaignMapper.mapEntity(testCampaign, TestCampaignInfo.class, view);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    public Response getTestCampaigns(TestCampaignCriteria criteria,
                                     int page, int perPage,
                                     UriInfo uriInfo,
                                     final Class<? extends DtoView<TestCampaignInfo>> view) {

        Paginated<TestCampaign> paginated =
                testCampaignService.customSearch(criteria, page, perPage);

        Map<String, QueryField> testPlanFields = searchMapping.getTestPlanFields();
        Query query = criteria.getQuery();
        QueryInfo queryInfo = query.convertToQueryInfo(testPlanFields);

        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestCampaign, TestCampaignInfo>() {
                    @Override
                    public TestCampaignInfo apply(TestCampaign testCampaign) {
                        return testCampaignMapper.mapEntity(testCampaign, TestCampaignInfo.class, view);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    public Response getCompletion(String search, int page, int perPage) {
        Query query = Query.fromQueryString(search);
        Paginated<TestCampaign> paginated = testCampaignService.customSearch(query, page, perPage);
        List<TestCampaign> results = paginated.getResults();
        CompletionInfo completion = CompletionHelper.completion(search, results);
        return Responses.ok(completion);
    }

    public Response getTestCases(long testPlanId, String q) {
        TestCampaign testCampaign = testCampaignRepository.find(testPlanId);
        if (testCampaign == null) {
            return Responses.notFound();
        }

        TestCampaignInfo testCampaignInfo =
                testCampaignMapper.mapEntity(testCampaign, new TestCampaignInfo(), TestCampaignView.Detailed.class);

        if (q == null) {
            return Responses.ok(testCampaignInfo.getTestCampaignItems());
        } else {
            Search search = getSearch(testPlanId, q);
            List<TestCampaignItem> filteredEntities = testCampaignItemRepository.search(search);
            Set<TestCampaignItemInfo> matches = getMatchingDtos(filteredEntities,
                    testCampaignInfo.getTestCampaignItems());
            return Responses.ok(matches);
        }
    }

    private Search getSearch(long testPlanId, String q) {
        Map<String, QueryField> testPlanItemFields = searchMapping.getDetailedTestPlanItemFields();
        Query query = Query.fromQueryString(q);
        Search search = query.convertToSearch(TestCampaignItem.class, testPlanItemFields);
        search.addFilterEqual("testCampaign.id", testPlanId);

        return search;
    }

    private Set<TestCampaignItemInfo> getMatchingDtos(List<TestCampaignItem> entities, Set<TestCampaignItemInfo> dtos) {
        Set<TestCampaignItemInfo> matches = Sets.newHashSet();
        Set<Long> ids = Mapping.ids(entities);

        for (TestCampaignItemInfo testCampaignItemInfo : dtos) {
            if (ids.contains(testCampaignItemInfo.getId())) {
                matches.add(testCampaignItemInfo);

            }
        }

        return matches;
    }

    public Response getTestCase(long testPlanId, String testCaseId,
                                Class<? extends DtoView<TestCampaignItemInfo>> view) {

        TestCampaignItem testCampaignItem = testCampaignItemRepository.findByTestPlanAndTestCase(testPlanId,
                testCaseId);
        if (testCampaignItem != null) {
            TestCampaignItemInfo testCampaignItemInfo =
                    testCampaignItemMapper.mapEntity(testCampaignItem, TestCampaignItemInfo.class, view);
            return Responses.ok(testCampaignItemInfo.getTestCase());
        }
        return Responses.notFound();
    }

    public Response getTestExecutions(
            long testPlanId,
            String testCaseId,
            int page,
            int perPage,
            UriInfo uriInfo,
            final Class<? extends DtoView<TestExecutionInfo>> view) {

        Paginated<TestExecution> paginated =
                testExecutionRepository.findByTestPlanAndTestCase(
                        testPlanId,
                        testCaseId,
                        page,
                        perPage);

        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestExecution, TestExecutionInfo>() {
                    @Override
                    public TestExecutionInfo apply(TestExecution execution) {
                        return testExecutionMapper.mapEntity(execution, TestExecutionInfo.class, view);
                    }
                }

        );
    }

    public Response getTestCaseCSV(long testPlanId, String filter) {
        TestCampaign testCampaign = testCampaignRepository.find(testPlanId);
        if (testCampaign == null) {
            return Responses.notFound();
        }
        String fileName = "TestCampaign#" + testPlanId;
        String fileExtension = ".csv";

        TestCampaignInfo testCampaignInfo =
                testCampaignMapper.mapEntity(testCampaign, new TestCampaignInfo(),
                        TestCampaignView.DetailedItems.class);

        StreamingOutput stream;
        TestCaseCSVCreator testCaseCSVFileCreator = new TestCaseCSVCreator();

        if (filter != null) {
            List<String> filteredList = Arrays.asList(filter.split(SEPARATOR));
            stream = testCaseCSVFileCreator.createCSV(testCampaignInfo, filteredList);
        } else {
            testCampaignQuerySetHelper.updateFileInfo(testPlanId, testCampaignInfo.getTestCampaignItems());
            stream = testCaseCSVFileCreator.createCSV(testCampaignInfo);
        }
        return Responses.file(stream, fileName, fileExtension);
    }


    public Response getTestExecutions(long testPlanId, Class<? extends DtoView<TestExecutionInfo>> dtoView) {
        TestCampaign testCampaign = testCampaignRepository.find(testPlanId);
        if (testCampaign == null) {
            return Responses.badRequest("Could not find test plan");
        }

        TestCampaignInfo testCampaignInfo =
                testCampaignMapper.mapEntity(testCampaign, new TestCampaignInfo(), TestCampaignView.Detailed.class);

        Set<TestCampaignItemInfo> testCaseInfos = testCampaignInfo.getTestCampaignItems();
        List<TestExecutionInfo> testExecutionInfos = Lists.newArrayList();

        for (TestCampaignItemInfo testCaseInfo : testCaseInfos) {
            TestExecution testExecution = testExecutionRepository.findLatestByTestPlanAndTestCase(testPlanId,
                    testCaseInfo.getTestCase().getTestCaseId());

            testExecutionInfos.add(testExecutionMapper.mapEntity(testExecution,
                    TestExecutionInfo.class,
                    dtoView));

        }

        return Responses.ok(testExecutionInfos);
    }
}
