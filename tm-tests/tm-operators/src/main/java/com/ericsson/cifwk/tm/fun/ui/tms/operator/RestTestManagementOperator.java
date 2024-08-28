package com.ericsson.cifwk.tm.fun.ui.tms.operator;

import com.beust.jcommander.internal.Sets;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.RequestBuilder;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.cifwk.tm.fun.ui.common.HttpToolHolder;
import com.ericsson.cifwk.tm.fun.ui.common.operator.RestCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.ReferenceGroup;
import com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references.TestCaseExecutions;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testplan.TestCampaignExecutionsInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Criterion;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Field;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.ExportResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.SearchResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestCampaignExecutionResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testexecutions.TestExecutionResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.CreateEditTestCampaignResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans.NavigationResult;
import com.ericsson.cifwk.tm.presentation.dto.CommentsThreadInfo;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.ericsson.cifwk.taf.tools.http.constants.ContentType.APPLICATION_JSON;
import static com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.JsonHelper.toTestCaseInfo;
import static com.google.common.net.HttpHeaders.ACCEPT;
import static java.util.stream.Collectors.toSet;

@Operator
public class RestTestManagementOperator extends RestCommonOperator {

    public static final String TEST_PLANS_URL = "/tm-server/api/test-campaigns/";
    // without "/", because called as resource "*.docx"
    public static final String TEST_CASES_URL = "/tm-server/api/test-cases";
    public static final String TEST_CASES_COMMENTS_URL = "/tm-server/api/test-cases/{objectId}/comments/";

    public static final Type TEST_CASE_PAGE = new TypeToken<Paginated<TestCaseInfo>>() {
    }.getType();

    public static final Type TEST_PLAN_PAGE = new TypeToken<Paginated<TestCampaignInfo>>() {
    }.getType();

    public static final Type TEST_EXECUTION_PAGE = new TypeToken<Paginated<TestExecutionInfo>>() {
    }.getType();

    public static final Type TEST_Plan_ITEM_PAGE = new TypeToken<Paginated<TestCampaignItemInfo>>() {
    }.getType();

    public void start(Host host) {
        HttpToolHolder httpToolHolder = httpToolHolderProvider.get();
        httpTool = httpToolHolder.getHttpTool();
    }

    @TestStep(id = "getTestCasesByFilter", description = "Gets Test Cases by Search criterions")
    public TestCasesResult getTestCasesByFilter(List<FilterInfo> filters) {
        String queryString = getQueryString(filters);

        Paginated<TestCaseInfo> testCases;
        try {
            testCases = getTestInfosBySearchQuery(queryString);
        } catch (Exception e) {
            return TestCasesResult.error(e.getMessage());
        }
        return TestCasesResult.success(testCases.getItems());
    }

    @TestStep(id = "showAssociatedTestPlans")
    public NavigationResult showAssociatedTestPlans(TestCaseInfo testCaseInfo) {
        try {
            getTestInfosBySearchQuery(testCaseInfo.getTestCaseId());

            List<TestCampaignInfo> testPlans = testCaseInfo.getAssociatedTestCampaigns();
            for (TestCampaignInfo testPlan : testPlans) {
                long testPlanId = testPlan.getId();
                TestCampaignInfo testCampaignInfo = getTestPlanInfoWithId(testPlanId);
                CreateEditTestCampaignResult testPlanResult = viewTestPlan(testCampaignInfo);
                if (!testPlanResult.isSuccess()) {
                    return NavigationResult.error("Could not show Test Plan " + testCampaignInfo.getId());
                }
            }
        } catch (Exception e) {
            return NavigationResult.error(e.getMessage());
        }
        return NavigationResult.success();
    }

    @TestStep(id = "getAssociatedComments")
    public Result<List<PostInfo>> getAssociatedComments(TestCaseInfo testCaseInfo) {
        String requestUrl = getTestCaseCommentsUrl(testCaseInfo.getId().toString());

        HttpResponse response = httpTool.request()
                .header(ACCEPT, JSON_MIME_TYPE)
                .contentType(APPLICATION_JSON)
                .queryParam("view", "detailed")
                .get(requestUrl);

        checkHttpStatus(requestUrl, response, HttpStatus.OK);
        checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);

        CommentsThreadInfo commentsThreadInfo = json.fromJson(response.getBody(), CommentsThreadInfo.class);
        return Result.success(commentsThreadInfo.getPosts());
    }

    @TestStep(id = "hideAssociatedComments")
    public NavigationResult hideAssociatedComments() {
        return NavigationResult.success();
    }

    @TestStep(id = "createAssociatedComment")
    public Result<PostInfo> createAssociatedComment(TestCaseInfo testCaseInfo, PostInfo newComment) {
        String requestUrl = getTestCaseCommentsUrl(testCaseInfo.getId().toString());
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(json.toJson(newComment))
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        PostInfo createdPost = json.fromJson(response.getBody(), PostInfo.class);
        return Result.success(createdPost);

    }

    @TestStep(id = "deleteAssociatedComment")
    public Result<PostInfo> deleteAssociatedComment(TestCaseInfo testCaseInfo, PostInfo comment) {
        String requestUrl = getTestCaseCommentsUrl(testCaseInfo.getId().toString()) + comment.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .delete(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success(null);
    }

    @TestStep(id = "checkAssociatedCommentDeleteIsRestricted")
    public Result<PostInfo> checkAssociatedCommentDeleteIsRestricted(TestCaseInfo testCaseInfo, PostInfo comment) {
        String requestUrl = getTestCaseCommentsUrl(testCaseInfo.getId().toString()) + comment.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .delete(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success(null);
    }

    @TestStep(id = "checkCommentsCount")
    public Result<String> checkCommentsCount(TestCaseInfo testCaseInfo) {
        String requestUrl = getTestCaseCommentsUrl(testCaseInfo.getId().toString());

        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .get(requestUrl);

        checkHttpStatus(requestUrl, response, HttpStatus.OK);
        checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);

        CommentsThreadInfo commentsThreadInfo = json.fromJson(response.getBody(), CommentsThreadInfo.class);
        return Result.success(commentsThreadInfo.getTotalCommentsCount().toString());

    }

    @TestStep(id = "getTestCasesByRequirement", description = "Get Test Cases by Requirement {0}")
    public TestCasesResult getTestCasesByRequirement(String requirementId) {
        List<TestCaseInfo> testCases;
        String validRequirementId = requirementId.toUpperCase();

        try {
            testCases = getTestInfosByRequirementId(validRequirementId);
        } catch (Exception e) {
            return TestCasesResult.error(e.getMessage());
        }
        return TestCasesResult.success(validRequirementId, testCases);
    }

    @TestStep(id = "createTestCase", description = "Creates Test Case with JSON InputStream and ProductInfo")
    public CreateEditTestCaseResult createTestCase(InputStream testCaseJson, ProductInfo productInfo) {
        if (productInfo != null) {
            selectUserProfileProduct(productInfo);
        }

        String requestUrl = TEST_CASES_URL;
        TestCaseInfo testCaseToCreate = setTestCaseId(testCaseJson, UUID.randomUUID().toString());
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .body(JsonHelper.fromTestCaseInfo(testCaseToCreate))
                .contentType(ContentType.APPLICATION_JSON)
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return CreateEditTestCaseResult.error(e.getMessage());
        }

        TestCaseInfo testCaseInfo = json.fromJson(response.getBody(), TestCaseInfo.class);
        return CreateEditTestCaseResult.success(testCaseInfo);
    }

    @TestStep(id = "createTestCaseFromTestCaseView", description = "Creates Test Case with JSON InputStream and ProductInfo")
    public CreateEditTestCaseResult createTestCaseFromTestCaseView(InputStream testCaseJson, ProductInfo productInfo) {
        return createTestCase(testCaseJson, productInfo);
    }

    @TestStep(id = "editTestCase", description = "Makes edit for Test Case")
    public CreateEditTestCaseResult editTestCase(TestCaseInfo existingTestCaseInfo, InputStream testCaseJson) {
        TestCaseInfo testCaseInfoToSave = setTestCaseId(testCaseJson, UUID.randomUUID().toString());
        updateWithIds(existingTestCaseInfo, testCaseInfoToSave);
        String testCaseInfoString = JsonHelper.fromTestCaseInfo(testCaseInfoToSave);

        String requestUrl = TEST_CASES_URL + "/" + testCaseInfoToSave.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .body(testCaseInfoString)
                .contentType(ContentType.APPLICATION_JSON)
                .put(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            return CreateEditTestCaseResult.error(e.getMessage());
        }

        return CreateEditTestCaseResult.success(null);
    }

    @TestStep(id = "copyTestCase", description = "Copies the Test Case")
    public CreateEditTestCaseResult copyTestCase(TestCaseInfo testCaseInfo, InputStream testCaseJson) {
        String requestUrl = TEST_CASES_URL;

        TestCaseInfo copy = setTestCaseId(testCaseJson, UUID.randomUUID().toString());
        copy.setTestCaseId("[ Random UUID for testing ] Copy of - " + UUID.randomUUID().toString());
        copy.setTitle("Copy of - " + testCaseInfo.getTitle());
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .body(JsonHelper.fromTestCaseInfo(copy))
                .contentType(ContentType.APPLICATION_JSON)
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return CreateEditTestCaseResult.error(e.getMessage());
        }
        TestCaseInfo copiedTestCase = json.fromJson(response.getBody(), TestCaseInfo.class);

        return CreateEditTestCaseResult.success(copiedTestCase);
    }

    @TestStep(id = "viewTestCase", description = "Select Test Case by the given {0} ID")
    public Result<TestCaseInfo> viewTestCase(String testCaseId) {
        String requestUrl = new StringBuilder(TEST_CASES_URL).append("/").append(testCaseId).toString();
        HttpResponse response = httpTool.request()
                .header(ACCEPT, JSON_MIME_TYPE)
                .contentType(APPLICATION_JSON)
                .queryParam("view", "detailed")
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
            checkContentType(requestUrl, response, APPLICATION_JSON);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        TestCaseInfo testCaseInfo = json.fromJson(response.getBody(), TestCaseInfo.class);
        return Result.success(testCaseInfo);
    }

    @TestStep(id = "selectUserProfileProduct", description = "Selects User Profile Product")
    public Result<ProductInfo> selectUserProfileProduct(ProductInfo productInfo) {
        String requestUrl = "/tm-server/api/users/me";

        UserProfileInfo userProfileFromGet;
        try {
            userProfileFromGet = getUserProfileInfo();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        userProfileFromGet.setProduct(productInfo);

        String userProfileJson = json.toJson(userProfileFromGet, UserProfileInfo.class);

        // save UserProfile
        HttpResponse responseFromPut = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(userProfileJson)
                .put(requestUrl);
        try {
            checkHttpStatus(requestUrl, responseFromPut, HttpStatus.OK);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        // get updated UserProfile from server
        try {
            userProfileFromGet = getUserProfileInfo();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success(userProfileFromGet.getProduct());
    }

    @TestStep(id = "getSelectedProject", description = "Gets Selected Project")
    public Result<ProductInfo> getSelectedProject() {
        UserProfileInfo userProfileInfo = getUserProfileInfo();
        Long productId = userProfileInfo.getProduct().getId();

        ProductInfo productInfo;
        if (productId == null) {
            productInfo = ProductType.ENM.getProductInfo();
            return Result.success(productInfo);
        }

        try {
            productInfo = getProductById(productId);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
        return Result.success(productInfo);
    }

    @TestStep(id = "executeQuickSearch", description = "Executes Quick Search {0}")
    public SearchResult executeQuickSearch(String searchValue) {
        List<Criterion> criterions = Lists.newArrayList();
        criterions.add(new Criterion(Field.ANY, Condition.CONTAINS, searchValue));

        List<FilterInfo> filters = Lists.newArrayList();
        return executeAdvancedSearch(criterions, filters);
    }

    @TestStep(id = "executeAdvancedSearch", description = "Executes Advanced Search with criterions")
    public SearchResult executeAdvancedSearch(List<Criterion> criterions, List<FilterInfo> filters) {
        String queryString = getQueryString(criterions, filters);
        Paginated<TestCaseInfo> testCases = getTestInfosBySearchQuery(queryString);

        return SearchResult.success(testCases);
    }

    @TestStep(id = "executeExportAction", description = "Executes Export Action")
    public ExportResult executeExportAction(List<Criterion> criterions) {
        String queryString = getQueryString(criterions);

        getExportedTestCasesPath(queryString);

        return ExportResult.success();
    }

    @TestStep(id = "navigateToTestPlans", description = "Gets Test Plans and checks http status")
    public NavigationResult navigateToTestPlans() {
        String requestUrl = TEST_PLANS_URL;
        HttpResponse response = httpTool.get(requestUrl);
        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            return NavigationResult.error(e.getMessage());
        }
        return NavigationResult.success();
    }

    @TestStep(id = "filterTestPlans", description = "Filters Test Plans")
    public Result<Paginated<TestCampaignInfo>> filterTestPlans(TestCampaignInfo filterSelection, List<FilterInfo> tableFilters) {
        String requestUrl = TEST_PLANS_URL;
        String queryString = getQueryString(tableFilters);

        RequestBuilder builder = httpTool.request()
                .queryParam("q", queryString)
                .queryParam("product", filterSelection.getProduct().getId().toString())
                .queryParam("feature", filterSelection.getFeatures().stream().findFirst().get().getId().toString());

        if (filterSelection.getDrop() != null) {
            builder = builder.queryParam("drop", filterSelection.getDrop().getId().toString());
        }

        Set<TechnicalComponentInfo> components = filterSelection.getComponents();
        if (!components.isEmpty()) {
            Set<String> componentIds = components.stream()
                    .map(c -> c.getId().toString())
                    .collect(toSet());

            String[] idsArray = componentIds.toArray(new String[componentIds.size()]);
            builder = builder.queryParam("component", idsArray);
        }

        HttpResponse response = builder.get(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Paginated<TestCampaignInfo> testPlans = json.fromJson(response.getBody(), TEST_PLAN_PAGE);
        return Result.success(testPlans);
    }

    @TestStep(id = "createTestPlan", description = "Creates Test Plan with default project")
    public CreateEditTestCampaignResult createTestPlan(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL;
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(json.toJson(testCampaignInfo))
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return CreateEditTestCampaignResult.error(e.getMessage());
        }

        TestCampaignInfo createdTestCampaignInfo = json.fromJson(response.getBody(), TestCampaignInfo.class);
        return CreateEditTestCampaignResult.success(createdTestCampaignInfo);
    }

    @TestStep(id = "createTestPlanWithMultipleTestCases", description = "Creates Test Plan with searched testcases")
    public CreateEditTestCampaignResult createTestPlanWithMultipleTestCases(TestCampaignInfo testCampaignInfo, List<Criterion> criterions) {
        String queryString = getQueryString(criterions);
        Set<TestCampaignItemInfo> assignments = Sets.newHashSet();
        Paginated<TestCaseInfo> testCases = getTestInfosBySearchQuery(queryString);

        for (TestCaseInfo testcase : testCases.getItems()) {
            TestCaseInfo testCaseInfo = new TestCaseInfo();
            testCaseInfo.setId(testcase.getId());
            testCaseInfo.setTestCaseId(testcase.getTitle());
            testCaseInfo.setVersion(testcase.getVersion());

            TestCampaignItemInfo assignmentInfo = new TestCampaignItemInfo();
            assignmentInfo.setTestCase(testCaseInfo);
            assignments.add(assignmentInfo);
        }
        testCampaignInfo.setTestCampaignItems(assignments);

        return createTestPlan(testCampaignInfo);
    }

    @TestStep(id = "createTestPlan", description = "Creates Test Plan with defined project")
    public CreateEditTestCampaignResult createTestPlan(TestCampaignInfo testCampaignInfo, ProjectInfo projectInfo) {
        testCampaignInfo.setProject(projectInfo);
        CreateEditTestCampaignResult testPlanResult = createTestPlan(testCampaignInfo);
        if (projectInfo == null) {
            return testPlanResult;
        }
        if (!testPlanResult.isSuccess()) {
            return testPlanResult;
        }
        ProjectInfo project = testPlanResult.getTestCampaignInfo().getProject();

        if (project != null && projectInfo.getExternalId().equals(project.getExternalId())) {
            return testPlanResult;
        } else {
            return CreateEditTestCampaignResult.error("Test Plan has invalid project.");
        }
    }

    @TestStep(id = "viewTestPlan", description = "View Test Plan")
    public CreateEditTestCampaignResult viewTestPlan(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .queryParam("view", "detailed")
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return CreateEditTestCampaignResult.error(e.getMessage());
        }
        TestCampaignInfo viewedTestCampaignInfo = json.fromJson(response.getBody(), TestCampaignInfo.class);
        return CreateEditTestCampaignResult.success(viewedTestCampaignInfo);
    }

    @TestStep(id = "editTestPlan", description = "Edit Test Plan")
    public CreateEditTestCampaignResult editTestPlan(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(json.toJson(testCampaignInfo))
                .put(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            return CreateEditTestCampaignResult.error(e.getMessage());
        }

        // return updated data from server
        return viewTestPlan(testCampaignInfo);
    }

    @TestStep(id = "copyTestPlan", description = "Makes copy of Test Plan")
    public CreateEditTestCampaignResult copyTestPlan(TestCampaignInfo testCampaignInfo) {
        TestCampaignInfo copy = new TestCampaignInfo();
        copy.setName(testCampaignInfo.getName() + " - COPY");
        copy.setProduct(testCampaignInfo.getProduct());
        copy.setFeatures(testCampaignInfo.getFeatures());
        copy.setDrop(testCampaignInfo.getDrop());
        copy.setComponents(testCampaignInfo.getComponents());
        copy.setTestCampaignItems(copyTestCampaignItems(testCampaignInfo.getTestCampaignItems()));
        return createTestPlan(copy);
    }

    @TestStep(id = "deleteTestPlan", description = "Deletes Test Plan")
    public CreateEditTestCampaignResult deleteTestPlan(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId();
        HttpResponse response = httpTool.delete(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return CreateEditTestCampaignResult.error(e.getMessage());
        }

        return CreateEditTestCampaignResult.success(null);
    }

    @TestStep(id = "navigateToTestPlanExecution", description = "Gets Test Plan Execution data and checks http status")
    public NavigationResult navigateToTestPlanExecution(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .queryParam("view", "detailed")
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl + "?view=detailed", response, HttpStatus.OK);
        } catch (Exception e) {
            return NavigationResult.error(e.getMessage());
        }
        return NavigationResult.success();
    }

    @TestStep(id = "viewTestPlanExecution", description = "View Test Plan Execution")
    public TestCampaignExecutionResult viewTestPlanExecution(TestCampaignInfo testCampaignInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId();
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .queryParam("view", "detailed")
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl + "?view=detailed", response, HttpStatus.OK);
            checkContentType(requestUrl + "?view=detailed", response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return TestCampaignExecutionResult.error(e.getMessage());
        }
        TestCampaignInfo viewedTestCampaignInfo = json.fromJson(response.getBody(), TestCampaignInfo.class);

        String executionsUrl;
        List<TestCaseExecutions> testCaseExecutions = new ArrayList();
        for (TestCampaignItemInfo testCaseInfo : viewedTestCampaignInfo.getTestCampaignItems()) {
            executionsUrl = new StringBuffer(requestUrl)
                    .append("/test-cases/")
                    .append(testCaseInfo.getTestCase().getId())
                    .append("/executions/")
                    .toString();
            response = httpTool.request()
                    .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                    .contentType(ContentType.APPLICATION_JSON)
                    .get(executionsUrl);

            try {
                checkHttpStatus(executionsUrl, response, HttpStatus.OK);
                checkContentType(executionsUrl, response, ContentType.APPLICATION_JSON);
            } catch (Exception e) {
                return TestCampaignExecutionResult.error(e.getMessage());
            }

            Paginated<TestExecutionInfo> testExecutionPage = json.fromJson(response.getBody(), TEST_EXECUTION_PAGE);
            TestCaseExecutions currentTestCaseExecutions = new TestCaseExecutions(
                    testCaseInfo.getTestCase().getTestCaseId(),
                    testExecutionPage.getItems());

            testCaseExecutions.add(currentTestCaseExecutions);
        }

        return TestCampaignExecutionResult.success(new TestCampaignExecutionsInfo(viewedTestCampaignInfo, testCaseExecutions));
    }

    @TestStep(id = "searchForTestExecutions", description = "Search for Test Executions")
    public Result<List<TestCampaignItemInfo>> searchForTestExecutions(TestCampaignInfo testCampaignInfo, List<Criterion> criteria) {
        String assignmentsUrl = TEST_PLANS_URL + testCampaignInfo.getId() + "/test-cases";

        List<TestCampaignItemInfo> assignments;
        String query = getQueryString(criteria);

        try {
            HttpResponse response = httpTool.request()
                    .queryParam("q", query)
                    .get(assignmentsUrl);

            checkHttpStatus(assignmentsUrl, response, HttpStatus.OK);
            checkContentType(assignmentsUrl, response, ContentType.APPLICATION_JSON);
            assignments = json.fromJson(response.getBody(), new TypeToken<List<TestCampaignItemInfo>>() {
            }.getType());
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        return Result.success(assignments);
    }

    @TestStep(id = "createTestExecution", description = "Creates Test Execution")
    public TestExecutionResult createTestExecution(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo, TestExecutionInfo testExecutionInfo) {
        String requestUrl = TEST_PLANS_URL + testCampaignInfo.getId() + "/test-cases/" + assignmentInfo.getTestCase().getId() + "/executions/";
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(json.toJson(testExecutionInfo))
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return TestExecutionResult.error(e.getMessage());
        }

        TestExecutionInfo createdTestExecutionInfo = json.fromJson(response.getBody(), TestExecutionInfo.class);
        return TestExecutionResult.success(createdTestExecutionInfo);
    }

    @TestStep(id = "createPassedTestExecution", description = "Creates passed Test Execution")
    public TestExecutionResult createPassedTestExecution(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo, TestExecutionInfo testExecutionInfo) {
        return createTestExecution(testCampaignInfo, assignmentInfo, testExecutionInfo);
    }

    @TestStep(id = "updateTestExecutionInfo", description = "Updates Test Execution Info")
    public TestExecutionResult updateTestExecutionInfo(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo, TestExecutionInfo testExecutionInfo) {
        long testCasePk = assignmentInfo.getTestCase().getId();
        String requestUrl = TEST_CASES_URL + "/" + testCasePk;
        HttpResponse testCaseResponse = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .queryParam("view", "detailed")
                .contentType(ContentType.APPLICATION_JSON)
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl + "?view=detailed", testCaseResponse, HttpStatus.OK);
            checkContentType(requestUrl + "?view=detailed", testCaseResponse, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return TestExecutionResult.error(e.getMessage());
        }
        TestCaseInfo testCaseInfo = json.fromJson(testCaseResponse.getBody(), TestCaseInfo.class);

        applyTestExecutionInfoWithIds(testExecutionInfo, testCampaignInfo, testCaseInfo);

        requestUrl = TEST_PLANS_URL + testCampaignInfo.getId() + "/test-cases/" + testCasePk + "/executions/";
        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .body(json.toJson(testExecutionInfo))
                .post(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.CREATED);
            checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return TestExecutionResult.error(e.getMessage());
        }

        TestExecutionInfo createdTestExecutionInfo = json.fromJson(response.getBody(), TestExecutionInfo.class);
        return TestExecutionResult.success(createdTestExecutionInfo);
    }

    @TestStep(id = "getPrepopulatedTestExecutionInfo", description = "REST is not supported yet")
    public TestExecutionResult getPrepopulatedTestExecutionInfo(TestCampaignInfo testCampaignInfo, TestCampaignItemInfo assignmentInfo) {
        String executionsUrl = TEST_PLANS_URL
                + testCampaignInfo.getId()
                + "/test-cases/"
                + assignmentInfo.getTestCase().getId()
                + "/executions";

        HttpResponse response = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .queryParam("perPage", "1")
                .queryParam("page", "1")
                .queryParam("view", "detailed")
                .get(executionsUrl);

        String queryStr = "?perPage=1&page=1&view=detailed";
        try {
            checkHttpStatus(executionsUrl + queryStr, response, HttpStatus.OK);
            checkContentType(executionsUrl + queryStr, response, ContentType.APPLICATION_JSON);
        } catch (Exception e) {
            return TestExecutionResult.error(e.getMessage());
        }

        Paginated<TestExecutionInfo> testExecutionPage = json.fromJson(response.getBody(), TEST_EXECUTION_PAGE);
        List<TestExecutionInfo> items = testExecutionPage.getItems();
        if (items.size() != 1) {
            return TestExecutionResult.error("There were not returned any testExecutionInfo. Actual count: " + items.size());
        }
        TestExecutionInfo testExecutionInfo = items.iterator().next();

        return TestExecutionResult.success(testExecutionInfo);
    }

    @TestStep(id = "getCurrentUserAssignments", description = "Get current logged in user assignments")
    public Result<Paginated<TestCampaignItemInfo>> getCurrentUserAssignments(List<FilterInfo> filters) {
        String requestUrl = "/tm-server/api/assignments";
        String queryString = getQueryString(filters);

        HttpResponse response = httpTool.request()
                .queryParam("q", queryString)
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        Paginated<TestCampaignItemInfo> assignmentPage = json.fromJson(response.getBody(), TEST_Plan_ITEM_PAGE);

        return Result.success(assignmentPage);
    }

    @TestStep(id = "getNewTestCaseGroups", description = "Get groups for current user selected project")
    public Result<List<ReferenceGroup>> getNewTestCaseGroups(ProductInfo productInfo) {
        String requestUrl = "/tm-server/api/references";

        HttpResponse response = httpTool.request()
                .queryParam("referenceId", "group")
                .queryParam("productId", productInfo.getName())
                .get(requestUrl);

        try {
            checkHttpStatus(requestUrl, response, HttpStatus.OK);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

        ReferenceGroup[] referenceGroups = json.fromJson(response.getBody(), ReferenceGroup[].class);
        return Result.success(Arrays.asList(referenceGroups));
    }

    @TestStep(id = "getExpectedResult", description = "Get the expected Test Case data matching the copied Test Case")
    public TestCaseInfo getExpectedResult(CreateEditTestCaseResult copyResult) {
        return toTestCaseInfo(getCopyTestCaseJson());
    }

    private InputStream getCopyTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/copyTestCaseRESTData.json");
    }

    private TestCaseInfo setTestCaseId(InputStream json, String testCaseId) {
        TestCaseInfo info = JsonHelper.toTestCaseInfo(json);
        if (info.getTestCaseId() != null) {
            info.setTestCaseId(testCaseId);
        }
        return info;
    }

    private void applyTestExecutionInfoWithIds(TestExecutionInfo testExecutionInfo, TestCampaignInfo testCampaignInfo, TestCaseInfo testCaseInfo) {
        testExecutionInfo.setTestPlan(testCampaignInfo.getId());
        testExecutionInfo.setTestCase(testCaseInfo.getId());

        for (int i = 0; i < testCaseInfo.getTestSteps().size(); i++) {
            TestStepInfo testStepInfo = testCaseInfo.getTestSteps().get(i);
            final int testStepOrderNr = i + 1;

            Optional<TestStepExecutionInfo> testStepExecutionInfoOptional = getTestStepExecutionOptional(testExecutionInfo, testStepOrderNr);
            applyTestStepResult(testStepInfo, testStepExecutionInfoOptional);

            applyVerifyStepsActualResults(testStepOrderNr, testStepInfo, testStepInfo.getVerifies(), testExecutionInfo.getVerifyStepExecutions());
        }
    }

    private Optional<TestStepExecutionInfo> getTestStepExecutionOptional(
            TestExecutionInfo testExecutionInfo,
            final int testStepOrderNr) {

        return Iterables.tryFind(testExecutionInfo.getTestStepExecutions(), new Predicate<TestStepExecutionInfo>() {
            @Override
            public boolean apply(TestStepExecutionInfo testStepExecutionInfo) {
                return testStepExecutionInfo.getTestStepOrderNr() == testStepOrderNr;
            }
        });
    }

    private void applyTestStepResult(TestStepInfo testStepInfo, Optional<TestStepExecutionInfo> testStepExecutionInfoOptional) {
        if (!testStepExecutionInfoOptional.isPresent()) {
            return;
        }
        TestStepExecutionInfo testStepExecution = testStepExecutionInfoOptional.get();
        testStepExecution.setTestStep(testStepInfo.getId());
    }

    private void applyVerifyStepsActualResults(
            int testStepOrderNr,
            TestStepInfo testStepInfo,
            List<VerifyStepInfo> verifySteps,
            List<VerifyStepExecutionInfo> verifyStepExecutions) {

        if (verifyStepExecutions.isEmpty()) {
            return;
        }

        for (int i = 0; i < verifySteps.size(); i++) {
            VerifyStepInfo verifyStep = verifySteps.get(i);
            final int verifyStepOrderNr = i + 1;

            Optional<VerifyStepExecutionInfo> verifyStepExecutionOptional = getVerifyStepExecutionOptional(verifyStepExecutions, testStepOrderNr, verifyStepOrderNr);
            applyVerifyStepActualResult(testStepInfo, verifyStep, verifyStepExecutionOptional);
        }
    }

    private void applyVerifyStepActualResult(TestStepInfo testStepInfo, VerifyStepInfo verifyStep, Optional<VerifyStepExecutionInfo> verifyStepExecutionOptional) {
        if (!verifyStepExecutionOptional.isPresent()) {
            return;
        }
        VerifyStepExecutionInfo verifyStepExecution = verifyStepExecutionOptional.get();
        verifyStepExecution.setTestStep(testStepInfo.getId());
        verifyStepExecution.setVerifyStep(verifyStep.getId());
    }

    private Optional<VerifyStepExecutionInfo> getVerifyStepExecutionOptional(
            List<VerifyStepExecutionInfo> verifyStepExecutions,
            final int testStepOrderNr,
            final int verifyStepOrderNr) {

        return Iterables.tryFind(verifyStepExecutions, new Predicate<VerifyStepExecutionInfo>() {
            @Override
            public boolean apply(VerifyStepExecutionInfo verifyStepExecutionInfo) {
                return verifyStepExecutionInfo.getTestStep() == testStepOrderNr && verifyStepExecutionInfo.getVerifyStep() == verifyStepOrderNr;
            }
        });
    }

    private String getQueryString(List... criterions) {
        List<String> data = Lists.newArrayList();
        for (List criterion : criterions) {
            String criterionString = Joiner.on("&").join(criterion);
            if (!criterionString.isEmpty()) {
                data.add(criterionString);
            }
        }
        return Joiner.on("&").join(data);
    }

    private ProductInfo getProductById(Long id) {
        String requestUrl = "/tm-server/api/products/" + id;

        HttpResponse responseFromGet = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .get(requestUrl);

        checkHttpStatus(requestUrl, responseFromGet, HttpStatus.OK);
        checkContentType(requestUrl, responseFromGet, ContentType.APPLICATION_JSON);

        return json.fromJson(responseFromGet.getBody(), ProductInfo.class);
    }

    private UserProfileInfo getUserProfileInfo() {
        String requestUrl = "/tm-server/api/users/me";

        HttpResponse responseFromGet = httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON)
                .get(requestUrl);

        checkHttpStatus(requestUrl, responseFromGet, HttpStatus.OK);
        checkContentType(requestUrl, responseFromGet, ContentType.APPLICATION_JSON);

        return json.fromJson(responseFromGet.getBody(), UserProfileInfo.class);
    }

    private Path getExportedTestCasesPath(String searchQuery) {
        String requestUrl = TEST_CASES_URL + ".docx";
        HttpResponse response = httpTool.request()
                .queryParam("q", searchQuery)
                .get(requestUrl);

        checkHttpStatus(requestUrl, response, HttpStatus.OK);
        checkContentType(requestUrl, response, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        try {
            Path file = Files.createTempFile("export", ".docx");
            // TODO: VOV: Should be changed to response.getFile() or something similar. Wait for improvement.
            byte[] bytes = response.getBody().getBytes("UTF8");
            return Files.write(file, bytes);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private Paginated<TestCaseInfo> getTestInfosBySearchQuery(String searchQuery) {
        String requestUrl = TEST_CASES_URL;

        HttpResponse response = httpTool.request()
                .queryParam("q", searchQuery)
                .queryParam("view", "simple-requirements")
                .get(requestUrl);

        checkHttpStatus(requestUrl, response, HttpStatus.OK);
        checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);
        return json.fromJson(response.getBody(), TEST_CASE_PAGE);
    }

    private List<TestCaseInfo> getTestInfosByRequirementId(String requirementId) {
        String requestUrl = "/tm-server/api/requirements/" + requirementId;

        HttpResponse response = httpTool.request()
                .queryParam("view", "detailed")
                .get(requestUrl);

        checkHttpStatus(requestUrl, response, HttpStatus.OK);
        checkContentType(requestUrl, response, ContentType.APPLICATION_JSON);

        RequirementInfo requirementInfo = json.fromJson(response.getBody(), RequirementInfo.class);
        List testCases = new ArrayList();
        testCases.addAll(requirementInfo.getTestCases());
        return testCases;
    }

    private void updateWithIds(TestCaseInfo infoWithIds, TestCaseInfo infoWithoutIds) {
        infoWithoutIds.setId(infoWithIds.getId());

        List<TestStepInfo> stepsWithIds = infoWithIds.getTestSteps();
        List<TestStepInfo> stepsWithoutIds = infoWithoutIds.getTestSteps();
        for (int i = 0; i < stepsWithIds.size(); i++) {
            TestStepInfo stepWithoutId = stepsWithoutIds.get(i);
            TestStepInfo stepWithId = stepsWithIds.get(i);
            stepWithoutId.setId(stepWithId.getId());

            List<VerifyStepInfo> verifiesWithIds = stepWithId.getVerifies();
            List<VerifyStepInfo> verifiesWithoutIds = stepWithoutId.getVerifies();

            for (int j = 0; j < verifiesWithIds.size(); j++) {
                verifiesWithoutIds.get(j).setId(verifiesWithIds.get(j).getId());
            }
        }
    }

    private TestCampaignInfo getTestPlanInfoWithId(long id) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setId(id);
        return testCampaignInfo;
    }

    private String getTestCaseCommentsUrl(String testCaseId) {
        return TEST_CASES_COMMENTS_URL.replace("{objectId}", testCaseId);
    }

    private Set<TestCampaignItemInfo> copyTestCampaignItems(Set<TestCampaignItemInfo> items) {
        items.forEach(i -> i.setId(null));
        return items;
    }
}
