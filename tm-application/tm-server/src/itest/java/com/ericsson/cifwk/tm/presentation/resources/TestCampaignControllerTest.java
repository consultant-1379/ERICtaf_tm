/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.application.services.validation.Nullable;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.management.impl.TestCampaignRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.impl.TestCaseVersionRepositoryImpl;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.infrastructure.mapping.DefectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.EnumReferenceMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProductMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProjectMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestExecutionResultMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestStepMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.VerifyStepMapper;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.SimpleTestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestInfoList;
import com.ericsson.cifwk.tm.presentation.dto.ValidationError;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignView;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseViewFactory;
import com.ericsson.cifwk.tm.test.fixture.Faker;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Chars;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult.FAIL;
import static com.ericsson.cifwk.tm.domain.model.execution.TestExecutionResult.PASS;
import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildDrop;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildFeature;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildProduct;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildProject;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestPlan;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class TestCampaignControllerTest extends BaseControllerLevelTest {

    public static final String TEST_PLANS_URL = "/tm-server/api/test-campaigns/";
    public static final String TEST_CAMPAIGNS_URL = "/tm-server/api/test-campaigns/";
    private static final int STRING_LENGTH = 300;

    private static final MediaType[] ACCEPTED = {MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.WILDCARD_TYPE};

    private static final GenericType<PageWrapper<TestCampaignInfo>> TEST_PLAN_PAGE = new GenericType<PageWrapper<TestCampaignInfo>>() {
    };

    private static final GenericType<List<TestCampaignInfo>> TEST_CAMPAIGN_LIST = new GenericType<List<TestCampaignInfo>>() {
    };

    private static final GenericType<List<TestCaseInfo>> TEST_CASE_LIST = new GenericType<List<TestCaseInfo>>() {
    };

    private static final GenericType<PageWrapper<TestExecutionInfo>> TEST_EXECUTION_LIST = new GenericType<PageWrapper<TestExecutionInfo>>() {
    };

    private static final GenericType<List<TestExecutionInfo>> TEST_EXECUTION_LIST_NO_PAGINATION = new GenericType<List<TestExecutionInfo>>() {
    };

    private static final GenericType<List<TestCampaignItemInfo>> TEST_CASE_ITEM_LIST = new GenericType<List<TestCampaignItemInfo>>() {
    };

    private final String headers = "Test Case ID,Title,Assigned,Result,Comments,Defect ID's,Priority,Type,Group,Feature,Component," +
            "Execution Type,Context,Execution Time,Improvement Story ID(s),Team,Suite,Execution Author,KPI Measurement,Requirement ID(s),File Attached";

    private TestCampaignMapper testCampaignMapper;
    private TestCaseMapper testCaseMapper;
    private ProductMapper productMapper;
    private FeatureMapper featureMapper;

    private final String DEFECT_ID = "CIP-234";

    @Mock
    private TestCampaignRepository testCampaignRepository;

    @Before
    public void setUp() {
        testCampaignMapper = new TestCampaignMapper(
                new ProjectMapper(new ProductMapper()),
                new TestCampaignViewFactory(),
                new TestExecutionResultMapper(),
                new DefectMapper(new ProjectMapper(new ProductMapper())),
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                null,
                new TestCaseVersionRepositoryImpl(),
                new DropMapper(new ProductMapper()),
                new ProductMapper(),
                new FeatureMapper(new ProductMapper()),
                new UserMapper(),
                new ReviewGroupMapper()
        );

        TestCaseVersionMapper testCaseVersionMapper = new TestCaseVersionMapper(
                new EnumReferenceMapper(),
                new TestStepMapper(new VerifyStepMapper()),
                new TestCaseViewFactory(),
                new TestCampaignRepositoryImpl(),
                testCampaignMapper,
                new ProjectMapper(new ProductMapper()),
                new FeatureMapper(new ProductMapper()),
                new ReviewGroupMapper(),
                new UserMapper(),
                new TestCaseVersionRepositoryImpl()
        );

        testCaseMapper = new TestCaseMapper(testCaseVersionMapper, new TestCaseViewFactory());
        featureMapper = new FeatureMapper(new ProductMapper());
        productMapper = new ProductMapper();

        Defect defect = new Defect();
        defect.setExternalId(DEFECT_ID);
        defect.setExternalTitle(DEFECT_ID);
        defect.setExternalSummary(DEFECT_ID);
        defect.setExternalStatusName("Ready");
        app.persistence().persistInTransaction(defect);
    }

    @Test
    public void testGetByProductAndDrop() {
        app.persistence().cleanupTables();

        Product enm = buildProduct("ENM").build();
        Product oss = buildProduct("OSS-RC").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, null).build();
        ProductFeature ossFm = buildFeature("OSS_FM", oss, null).build();
        Drop enmDrop1 = buildDrop(enm, "16.3").build();
        Drop enmDrop2 = buildDrop(enm, "16.4").build();
        Drop ossDrop1 = buildDrop(oss, "1.0").build();

        app.persistence().persistInTransaction(enm, oss, enmFm, ossFm, enmDrop1, enmDrop2, ossDrop1);
        persistTestCampaigns(Lists.newArrayList("ENM Test Campaign #1", "ENM Test Campaign #2"), enmDrop1, enmFm, null);
        persistTestCampaigns(Lists.newArrayList("ENM Test Campaign #3", "ENM Test Campaign #4"), enmDrop2, enmFm, null);
        persistTestCampaigns(Lists.newArrayList("OSS Test Campaign #1", "OSS Test Campaign #2"), ossDrop1, ossFm, null);

        // search for ENM Test Campaigns
        Response response = searchTestCampaigns(enm.getId());
        List<TestCampaignInfo> results = assertNumberOfTestCampaignsInResponse(response, 4);
        results.forEach(r -> assertThat(r.getDrop().getProduct().getName(), equalTo("ENM")));

        // search for OSS-RC Test Campaigns
        response = searchTestCampaigns(oss.getId());
        results = assertNumberOfTestCampaignsInResponse(response, 2);
        results.forEach(r -> assertThat(r.getDrop().getProduct().getName(), equalTo("OSS-RC")));

        // search for ENM Test Campaigns by drop
        response = searchTestCampaigns(enm.getId(), enmDrop1.getId());

        results = assertNumberOfTestCampaignsInResponse(response, 2);
        results.forEach(r -> assertThat(r.getProduct().getName(), equalTo("ENM")));
        results.forEach(r -> assertThat(r.getDrop().getName(), equalTo("16.3")));
        List<String> testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());
        assertThat(testCampaignNames, hasItems("ENM Test Campaign #1", "ENM Test Campaign #2"));

        // search for ENM Test Campaigns by drop
        response = searchTestCampaigns(enm.getId(), enmDrop2.getId());

        results = assertNumberOfTestCampaignsInResponse(response, 2);
        results.forEach(r -> assertThat(r.getProduct().getName(), equalTo("ENM")));
        results.forEach(r -> assertThat(r.getDrop().getName(), equalTo("16.4")));
        testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());
        assertThat(testCampaignNames, hasItems("ENM Test Campaign #3", "ENM Test Campaign #4"));

        // search for OSS-RC Test Campaigns by drop
        response = searchTestCampaigns(oss.getId(), ossDrop1.getId());

        results = assertNumberOfTestCampaignsInResponse(response, 2);
        results.forEach(r -> assertThat(r.getProduct().getName(), equalTo("OSS-RC")));
        results.forEach(r -> assertThat(r.getDrop().getName(), equalTo("1.0")));
        testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());
        assertThat(testCampaignNames, hasItems("OSS Test Campaign #1", "OSS Test Campaign #2"));
    }

    @Test
    public void testGetByProductDrop_featureComponent_andQuery() {
        app.persistence().cleanupTables();

        Product enm = buildProduct("ENM").build();
        Product oss = buildProduct("OSS-RC").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, null).build();
        ProductFeature enmPm = buildFeature("ENM_PM", enm, null).build();

        ProductFeature ossPm = buildFeature("OSS_PM", oss, null).build();
        Drop enmDrop1 = buildDrop(enm, "16.3").build();
        Drop enmDrop2 = buildDrop(enm, "16.4").build();
        Drop ossDrop1 = buildDrop(oss, "1.0").build();

        TechnicalComponent enmFmComponent1 = createComponentEntity("ENM_FM_COMPONENT_1", enmFm);
        TechnicalComponent enmFmComponent2 = createComponentEntity("ENM_FM_COMPONENT_2", enmFm);
        TechnicalComponent enmFmComponent3 = createComponentEntity("ENM_FM_COMPONENT_3", enmFm);
        TechnicalComponent enmPmComponent1 = createComponentEntity("ENM_PM_COMPONENT_1", enmPm);
        TechnicalComponent enmPmComponent2 = createComponentEntity("ENM_PM_COMPONENT_2", enmPm);

        TechnicalComponent ossPmComponent1 = createComponentEntity("OSS_PM_COMPONENT_1", ossPm);
        TechnicalComponent ossPmComponent2 = createComponentEntity("OSS_PM_COMPONENT_2", ossPm);
        TechnicalComponent ossPmComponent3 = createComponentEntity("OSS_PM_COMPONENT_3", ossPm);

        app.persistence().persistInTransaction(enm, oss, enmFm, ossPm, enmPm, enmDrop1, enmDrop2, ossDrop1);

        persistTestCampaigns(
                Lists.newArrayList("ENM Test Campaign #1", "ENM Test Campaign #2"),
                enmDrop1,
                enmFm,
                Sets.newHashSet(enmFmComponent1, enmFmComponent2, enmFmComponent3)
        );
        persistTestCampaigns(
                Lists.newArrayList("ENM Test Campaign #3", "ENM Test Campaign #4"),
                enmDrop2,
                enmPm,
                Sets.newHashSet(enmPmComponent1, enmPmComponent2)
        );
        persistTestCampaigns(
                Lists.newArrayList("OSS-RC Test Campaign #1", "OSS-RC Test Campaign #2"),
                ossDrop1,
                ossPm,
                Sets.newHashSet(ossPmComponent1, ossPmComponent2)
        );
        persistTestCampaigns(
                Lists.newArrayList("OSS-RC Test Campaign #3"),
                ossDrop1,
                ossPm,
                Sets.newHashSet(ossPmComponent3)
        );

        // search ENM Test Campaigns by product, drop and feature
        Response response = searchTestCampaigns(enm.getId(), enmDrop1.getId(), enmFm.getId());

        List<TestCampaignInfo> results = assertNumberOfTestCampaignsInResponse(response, 2);
        List<String> testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

        assertThat(testCampaignNames, hasItems("ENM Test Campaign #1", "ENM Test Campaign #2"));

        // search OSS-RC Test Campaigns by product, drop, feature and components
        response = searchTestCampaigns(oss.getId(), ossDrop1.getId(), ossPm.getId(),
                Lists.newArrayList(ossPmComponent1.getId(), ossPmComponent2.getId()), null);

        results = assertNumberOfTestCampaignsInResponse(response, 2);
        testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

        assertThat(testCampaignNames, hasItems("OSS-RC Test Campaign #1", "OSS-RC Test Campaign #2"));

        // search OSS-RC Test Campaigns by product, drop, feature and components
        response = searchTestCampaigns(oss.getId(), ossDrop1.getId(), ossPm.getId(),
                Lists.newArrayList(ossPmComponent3.getId()), null);

        results = assertNumberOfTestCampaignsInResponse(response, 1);
        testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

        assertThat(testCampaignNames, hasItems("OSS-RC Test Campaign #3"));

        // search OSS-RC Test Campaigns by product, drop, feature, components and query
        final String nameLikeQuery = "name~#1";
        response = searchTestCampaigns(oss.getId(), ossDrop1.getId(), ossPm.getId(),
                Lists.newArrayList(ossPmComponent1.getId(), ossPmComponent2.getId()), nameLikeQuery);

        results = assertNumberOfTestCampaignsInResponse(response, 1);
        testCampaignNames = results.stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

        assertThat(testCampaignNames, hasItems("OSS-RC Test Campaign #1"));
    }

    private List<TestCampaignInfo> assertNumberOfTestCampaignsInResponse(Response response, long expectedResults) {
        assertStatus(response, Response.Status.OK);
        PageWrapper<TestCampaignInfo> page = response.readEntity(TEST_PLAN_PAGE);
        assertThat(page.getTotalCount(), equalTo(expectedResults));
        List<TestCampaignInfo> items = page.getItems();
        assertThat(items, hasSize((int) expectedResults));

        return items;
    }

    @Test
    public void testGetAll() {
        TestCampaign testPlan1 = createTestCampaign();
        TestCampaign testPlan2 = createTestCampaign();
        app.persistence().persistInTransaction(testPlan1, testPlan2);

        Response response = app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignInfo> page = response.readEntity(TEST_PLAN_PAGE);
        response.close();

        assertThat(page.getTotalCount(), equalTo(2L));
        List<TestCampaignInfo> items = page.getItems();
        assertThat(items, hasSize(2));

        List<String> testCampaignNames = items.stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());

        assertTrue(testCampaignNames.containsAll(testCampaignNames));
    }

    @Test
    public void testGetAllByOrder() {
        TestCampaign testCampaign1 = createTestCampaign();
        TestCampaign testCampaign2 = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign1, testCampaign2);

        Response response = app.client().path(TEST_CAMPAIGNS_URL)
                .queryParam("orderBy", "id")
                .queryParam("orderMode", "asc")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignInfo> page = response.readEntity(TEST_PLAN_PAGE);
        response.close();

        assertThat(page.getTotalCount(), equalTo(2L));
        List<TestCampaignInfo> items = page.getItems();
        assertThat(items, hasSize(2));

        Iterator<TestCampaignInfo> testPlanInfoIterator = items.iterator();

        assertTestPlanInfo(testPlanInfoIterator.next(), mapTestCampaign(testCampaign1), hasSize(0), true);
        assertTestPlanInfo(testPlanInfoIterator.next(), mapTestCampaign(testCampaign2), hasSize(0), true);
    }

    @Test
    public void testGetAllDetailed() {
        TestCampaign testCampaign1 = createTestCampaign();
        TestCampaign testCampaign2 = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign1, testCampaign2);

        Response response = app.client().path(TEST_CAMPAIGNS_URL)
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignInfo> page = response.readEntity(TEST_PLAN_PAGE);
        response.close();

        assertThat(page.getTotalCount(), equalTo(2L));
        List<TestCampaignInfo> items = page.getItems();
        assertThat(items, hasSize(2));

        Iterator<TestCampaignInfo> testPlanInfoIterator = items.iterator();

        assertTestPlanInfo(testPlanInfoIterator.next(), mapTestCampaign(testCampaign2), hasSize(0), true);
        assertTestPlanInfo(testPlanInfoIterator.next(), mapTestCampaign(testCampaign1), hasSize(0), true);
    }

    private TestCampaignInfo mapTestCampaign(TestCampaign testCampaign) {
        return testCampaignMapper.mapEntity(testCampaign, TestCampaignInfo.class, TestCampaignView.Detailed.class);
    }

    private TestCaseInfo mapTestCase(TestCase testCase) {
        return testCaseMapper.mapEntity(testCase, TestCaseInfo.class);
    }

    private void assertTestPlanInfo(
            TestCampaignInfo testCampaignInfo,
            TestCampaignInfo testPlan1,
            Matcher<Collection<?>> assignmentsSizeMatcher,
            Boolean isProjectNull) {
        assertThat(testCampaignInfo.getName(), equalTo(testPlan1.getName()));
        assertThat(testCampaignInfo.getTestCampaignItems(), assignmentsSizeMatcher);

        if (isProjectNull) {
            assertThat(testCampaignInfo.getProject(), nullValue());
        } else {
            assertThat(testCampaignInfo.getProject().getName(), equalTo(testPlan1.getProject().getName()));
        }
    }

    @Test
    public void testGetOne() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        Response response = searchTestCampaignById(testCampaign.getId(), false);
        assertStatus(response, Response.Status.OK);

        TestCampaignInfo foundTestCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();
        assertThat(foundTestCampaignInfo.getName(), equalTo(testCampaign.getName()));
        assertThat(foundTestCampaignInfo.getTestCampaignItems(), hasSize(0));
        assertThat(foundTestCampaignInfo.getProject(), nullValue());
    }

    @Test
    public void testGetOneDetailed() {
        Project project = fixture().persistProject("PRJ", "Project");
        TestCampaign testCampaign = createTestCampaign();
        testCampaign.setProject(project);

        TestCampaignItem testCampaignItem = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem);
        TestExecution testExecution = fixture().constructTestExecution(testCampaign, testCampaignItem.getTestCaseVersion());
        testExecution.setResult(PASS);

        app.persistence().persistInTransaction(testCampaign, testExecution);

        Response response = searchTestCampaignById(testCampaign.getId(), true);
        assertStatus(response, Response.Status.OK);

        TestCampaignInfo testCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();

        assertThat(testCampaignInfo.getName(), equalTo(testCampaign.getName()));
        assertThat(testCampaignInfo.getProject().getName(), equalTo(project.getName()));

        Set<TestCampaignItemInfo> testCampaignItemInfos = testCampaignInfo.getTestCampaignItems();

        assertThat(testCampaignItemInfos, hasSize(1));

        TestCampaignItemInfo testCaseInfo = testCampaignItemInfos.iterator().next();

        assertThat(testCaseInfo.getResult().getTitle(), equalTo(testExecution.getResult().getName()));
    }

    @Test
    public void testPut() {
        TestCampaign testCampaign = createTestCampaign();
        final String newDescription = "New Description";
        testCampaign.setDescription(newDescription);
        app.persistence().persistInTransaction(testCampaign);

        DateTime now = DateTime.now();
        testCampaign.setStartDate(now.toDate());
        testCampaign.setEndDate(now.plusDays(1).toDate());

        Response response = app
                .client()
                .path(TEST_CAMPAIGNS_URL + testCampaign.getId())
                .request()
                .accept(ACCEPTED)
                .put(Entity.entity(mapTestCampaign(testCampaign), MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);
        response.close();

        response = searchTestCampaignById(testCampaign.getId(), true);

        TestCampaignInfo updatedTestCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();
        assertThat(updatedTestCampaignInfo.getDescription(), equalTo(newDescription));
    }

    @Test
    public void testPutWithNameChange() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);
        final String newName = "New Name";
        testCampaign.setName(newName);

        Response response = app
                .client()
                .path(TEST_CAMPAIGNS_URL + testCampaign.getId())
                .request()
                .accept(ACCEPTED)
                .put(Entity.entity(mapTestCampaign(testCampaign), MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);
    }

    @Test
    public void testPost() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignInfo testCampaignInfo = mapTestCampaign(testCampaign);
        Response response = createTestCampaign(testCampaignInfo);

        assertStatus(response, Response.Status.CREATED);
        TestCampaignInfo newTestCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();
        assertThat(newTestCampaignInfo.getName(), equalTo(testCampaign.getName()));
    }

    @Test
    public void testPostWithProjectIdOnly() {
        Product product = buildProduct().build();
        Project project = buildProject(product).build();
        app.persistence().persistInTransaction(product, project);

        TestCampaign testCampaign = createTestCampaign();
        testCampaign.setProject(new Project(project.getExternalId()));

        Response response = createTestCampaign(mapTestCampaign(testCampaign));

        assertStatus(response, Response.Status.CREATED);
        TestCampaignInfo newTestCampaignInfo = response.readEntity(TestCampaignInfo.class);

        assertThat(newTestCampaignInfo.getName(), equalTo(testCampaign.getName()));
        assertThat(newTestCampaignInfo.getProject().getExternalId(), equalTo(testCampaign.getProject().getExternalId()));
    }

    @Test
    public void testPostWithProjectInvalidProject() {
        Project badProject = new Project("BadProject");
        TestCampaign testCampaign = createTestCampaign();
        testCampaign.setProject(badProject);

        Response response = createTestCampaign(mapTestCampaign(testCampaign));
        response.close();

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testPostWithoutName() {
        TestCampaign testCampaign = buildTestPlan()
                .withName(null)
                .withFeature(createFeature())
                .build();

        Response response = createTestCampaign(mapTestCampaign(testCampaign));

        assertStatus(response, Response.Status.BAD_REQUEST);
        ArrayList<ValidationError> validationErrors = Lists.newArrayList(response.readEntity(ValidationError[].class));
        response.close();

        assertThat(validationErrors, hasSize(1));
        assertThat(validationErrors.get(0).getMessage(), equalTo("Field 'name' must not be null or empty"));
    }

    @Test
    public void testPostWithExtremelyLargeNames() {
        TestCampaign testCampaign = buildTestPlan()
                .withName(Faker.randomString(STRING_LENGTH))
                .withDescription(Faker.randomString(STRING_LENGTH))
                .withEnvironment(Faker.randomString(STRING_LENGTH))
                .withFeature(createFeature())
                .build();

        Response response = createTestCampaign(mapTestCampaign(testCampaign));

        assertStatus(response, Response.Status.BAD_REQUEST);

        ArrayList<ValidationError> validationErrors = Lists.newArrayList(response.readEntity(ValidationError[].class));
        response.close();
        ArrayList<String> validationErrorStrings = Lists.newArrayList();
        for (ValidationError validationError : validationErrors) {
            validationErrorStrings.add(validationError.getMessage());
        }

        assertThat(validationErrorStrings, containsInAnyOrder("Field 'name' must be shorter than 255 characters",
                "Field 'environment' must be shorter than 255 characters"));
    }

    @Test
    public void testPostWithInvalidDropForProduct() {
        Product enm = buildProduct("ENM").build();
        enm.setDropCapable(true);
        Product oss = buildProduct("OSS").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, Sets.newHashSet()).build();
        Drop enmDrop = buildDrop(enm, "ENM_DROP").build();
        enm.addDrop(enmDrop);
        Drop ossDrop = buildDrop(oss, "OSS_DROP").build();

        app.persistence().persistInTransaction(enm, oss, enmFm, ossDrop);

        TestCampaign enmTestCampaignWithIncorrectDrop = buildTestPlan()
                .withFeature(enmFm) //ENM feature and product
                .withDrop(ossDrop) //OSS drop
                .build();

        TestCampaignInfo testCampaignInfo = mapTestCampaign(enmTestCampaignWithIncorrectDrop);

        Response response = createTestCampaign(testCampaignInfo);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testPostWithInvalidFeatureForProduct() {
        app.persistence().cleanupTables();

        Product enm = fixture().persistProduct("ENM");
        Product oss = fixture().persistProduct("OSS");
        ProductFeature enmFm = fixture().persistFeature("ENM_FM", enm);
        ProductFeature enmCm = fixture().persistFeature("ENM_CM", enm);
        ProductFeature ossPm = fixture().persistFeature("OSS_PM", oss);

        enm.setFeatures(Sets.newHashSet(enmFm, enmCm));
        oss.setFeatures(Sets.newHashSet(ossPm));
        app.persistence().persistInTransaction(enm, oss);

        TestCampaign testCampaign = buildTestPlan()
                .build();

        TestCampaignInfo testCampaignInfo = mapTestCampaign(testCampaign);
        // set product to enm
        testCampaignInfo.setProduct(mapProduct(enm));
        // set feature to an oss feature
        Set<ProductFeature> features = Sets.newHashSet(ossPm);
        testCampaignInfo.setFeatures(mapFeatures(features));

        Response response = createTestCampaign(testCampaignInfo);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testPostWithInvalidComponentForFeature() {
        Product enm = fixture().persistProduct("ENM");
        Product oss = fixture().persistProduct("OSS");
        ProductFeature enmFm = fixture().persistFeature("ENM_FM", enm);
        ProductFeature ossPm = fixture().persistFeature("OSS_PM", oss);

        TechnicalComponent enmFmComponent1 = fixture().persistTechnicalComponent("ENM_FM_COMPONENT_1", enmFm);
        TechnicalComponent enmFmComponent2 = fixture().persistTechnicalComponent("ENM_FM_COMPONENT_2", enmFm);
        enmFm.setComponents(Sets.newHashSet(enmFmComponent1, enmFmComponent2));

        TechnicalComponent ossPmComponent = fixture().persistTechnicalComponent("OSS_PM_COMPONENT", ossPm);
        ossPm.setComponents(Sets.newHashSet(ossPmComponent));

        enm.setFeatures(Sets.newHashSet(enmFm));
        oss.setFeatures(Sets.newHashSet(ossPm));
        app.persistence().persistInTransaction(enm, oss);

        TestCampaign testCampaign = buildTestPlan()
                .withFeature(enmFm) // ENM feature
                .withComponents(Sets.newHashSet(ossPmComponent)) // OSS component
                .build();

        TestCampaignInfo testCampaignInfo = mapTestCampaign(testCampaign);

        Response response = createTestCampaign(testCampaignInfo);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    private ProductInfo mapProduct(Product product) {
        return productMapper.mapEntity(product, ProductInfo.class);
    }

    private Set<FeatureInfo> mapFeatures(Set<ProductFeature> entities) {
        Set<FeatureInfo> featureInfos = entities.stream()
                .map(entity -> featureMapper.mapEntity(entity, FeatureInfo.class))
                .collect(Collectors.toSet());

        return featureInfos;
    }

    @Test
    public void testPostWhenTestCampaign_andTestCaseFeatures_dontMatch() throws IOException {
        // create test campaign
        Product product = fixture().persistProduct("AIA");
        ProductFeature testCampaignFeature = fixture().persistFeature("TEST_CAMPAIGN_FEATURE", product);
        TestCampaign testCampaign = buildTestPlan(testCampaignFeature).build();

        // create test case
        ProductFeature testCaseFeature = fixture().persistFeature("TEST_CASE_FEATURE", product);
        TechnicalComponent testCaseComponent = fixture().persistTechnicalComponent("TEST_CASE_COMPONENT", testCaseFeature);
        TestType testType = fixture().persistTestType(UUID.randomUUID().toString(), product);
        TestCase testCase = fixture().persistTestCase(UUID.randomUUID().toString(), testCaseComponent, testCaseFeature, testType);

        // create test campaign dto
        TestCampaignInfo testCampaignInfo = mapTestCampaign(testCampaign);
        TestCaseInfo testCaseInfo = mapTestCase(testCase);

        TestCampaignItemInfo testCampaignItemInfo = new TestCampaignItemInfo();
        testCampaignItemInfo.setTestCase(testCaseInfo);
        testCampaignItemInfo.setResult(new ReferenceDataItem("2", "Pass"));
        testCampaignItemInfo.addRequirementIds("CIP-123");

        Set<TestCampaignItemInfo> testCampaignItemInfos = Sets.newHashSet();
        testCampaignItemInfos.add(testCampaignItemInfo);
        testCampaignInfo.setTestCampaignItems(testCampaignItemInfos);

        Response response = createTestCampaign(testCampaignInfo);
        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testPostWhenTestCampaign_andTestCaseComponents_dontMatch() throws IOException {
        // create test campaign
        Product product = fixture().persistProduct("AIA");
        ProductFeature feature = fixture().persistFeature("TEST_CAMPAIGN_FEATURE", product);
        TechnicalComponent testCampaignComponent = fixture().persistTechnicalComponent("TEST_CAMPAIGN_COMPONENT", feature);
        Set<TechnicalComponent> testCampaignComponents = Sets.newHashSet(testCampaignComponent);
        TestCampaign testCampaign = buildTestPlan(feature).build();
        testCampaign.setComponents(testCampaignComponents);

        // create test case
        TechnicalComponent testCaseComponent = fixture().persistTechnicalComponent("TEST_CASE_COMPONENT", feature);
        TestType testType = fixture().persistTestType(UUID.randomUUID().toString(), product);
        TestCase testCase = fixture().persistTestCase(UUID.randomUUID().toString(), testCaseComponent, feature, testType);

        // create test campaign dto
        TestCampaignInfo testCampaignInfo = mapTestCampaign(testCampaign);
        TestCaseInfo testCaseInfo = mapTestCase(testCase);

        TestCampaignItemInfo testCampaignItemInfo = new TestCampaignItemInfo();
        testCampaignItemInfo.setTestCase(testCaseInfo);
        testCampaignItemInfo.setResult(new ReferenceDataItem("2", "Pass"));
        testCampaignItemInfo.addRequirementIds("CIP-123");

        Set<TestCampaignItemInfo> testCampaignItemInfos = Sets.newHashSet();
        testCampaignItemInfos.add(testCampaignItemInfo);
        testCampaignInfo.setTestCampaignItems(testCampaignItemInfos);

        Response response = createTestCampaign(testCampaignInfo);
        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testDelete() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId())
                .request()
                .delete();

        assertStatus(response, Response.Status.NO_CONTENT);
        response.close();

        response = searchTestCampaignById(testCampaign.getId(), true);

        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void testGetTestCaseVersion() {
        TestCampaign testCampaign = createTestCampaign();
        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        app.persistence().persistInTransaction(testCampaign);

        TestCaseVersion testCaseVersion = testCampaignItem1.getTestCaseVersion();
        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases/" + testCaseVersion.getTestCase().getId())
                .queryParam("view", "detailed-test-case")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        TestCaseInfo testCaseInfo = response.readEntity(TestCaseInfo.class);
        response.close();

        assertThat(testCaseInfo.getId(), equalTo(testCaseVersion.getTestCase().getId()));
        assertThat(testCaseInfo.getVersionId(), equalTo(testCaseVersion.getId()));
    }

    @Test
    public void testAttachTestCase_WhenTestPlanIsOpen_ShouldSuccessfullyAttachTestCase() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        app.persistence().persistInTransaction(testCampaign);

        SimpleTestCaseInfo testInfo = new SimpleTestCaseInfo();
        testInfo.setId(testCampaignItem2.getTestCaseVersion().getTestCase().getId());
        TestInfoList testInfoList = new TestInfoList();
        testInfoList.setTestCases(Lists.newArrayList(testInfo));

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .request()
                .post(Entity.entity(testInfoList, MediaType.APPLICATION_JSON_TYPE));

        assertStatus(response, Response.Status.NO_CONTENT);
        response.close();

        response = searchTestCampaignById(testCampaign.getId(), true);

        TestCampaignInfo testCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();
        assertThat(testCampaignInfo.getTestCampaignItems().size(), equalTo(2));
    }

    @Test
    public void testAttachTestCase_WhenTestPlanIsLocked_ShouldReturnBadRequest() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        testCampaign.setLocked(true);

        app.persistence().persistInTransaction(testCampaign);

        SimpleTestCaseInfo testInfo = new SimpleTestCaseInfo();
        testInfo.setId(testCampaignItem2.getId());
        TestInfoList testInfoList = new TestInfoList();
        testInfoList.setTestCases(Lists.newArrayList(testInfo));

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .request()
                .post(Entity.entity(testInfoList, MediaType.APPLICATION_JSON_TYPE));

        assertStatus(response, Response.Status.BAD_REQUEST);
        response.close();

        response = searchTestCampaignById(testCampaign.getId(), true);

        TestCampaignInfo testCampaignInfo = response.readEntity(TestCampaignInfo.class);
        response.close();
        assertThat(testCampaignInfo.getTestCampaignItems().size(), equalTo(1));
    }

    @Test
    public void testGetCompletions_WhenNoProjectIdSpecified_ShouldReturnCompletionInfoWithAllTestPlans() {
        createTestPlansWithProjects(20);

        Response response = getCompletion(null);
        CompletionInfo info = response.readEntity(CompletionInfo.class);
        response.close();

        assertThat(info.getSearch(), nullValue());
        assertThat(info.getItems().size(), equalTo(20));
    }

    @Test
    public void testGetCompletions_WhenProductIdSpecified_ShouldReturnCompletionInfoWithTestPlansForProductId() {
        createTestPlansWithProjects(20);

        Response response = getCompletion("productName=ENM");
        CompletionInfo info = response.readEntity(CompletionInfo.class);
        response.close();

        assertThat(info.getSearch(), equalTo("productName=ENM"));
        assertThat(info.getItems().size(), equalTo(20));
    }

    private void createTestPlansWithProjects(int num) {
        Product product = buildProduct("ENM").build();
        ProductFeature feature = buildFeature("FM", product, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(product, feature);

        for (int i = 0; i < num; i++) {
            Project project = buildProject(product)
                    .withExternalId("ID" + i)
                    .build();
            app.persistence().persistInTransaction(project);

            TestCampaign testCampaign = buildTestPlan()
                    .withProject(project)
                    .withFeature(feature)
                    .build();

            app.persistence().persistInTransaction(testCampaign);
        }
    }

    private Response getCompletion(String projectId) {
        return app
                .client()
                .path(TEST_PLANS_URL)
                .path("completion")
                .queryParam("search", projectId)
                .request()
                .get();
    }

    @Test
    public void testCloseTestPlan_WhenTestPlanIsOpen_ShouldCloseTestPlan() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setId(testCampaign.getId());
        testCampaignInfo.setLocked(true);

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/status")
                .request()
                .put(Entity.entity(testCampaignInfo, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);

        Response responseWithTestPlan = searchTestCampaignById(testCampaign.getId(), true);

        assertStatus(responseWithTestPlan, Response.Status.OK);

        TestCampaignInfo modifiedTestPlan = responseWithTestPlan.readEntity(TestCampaignInfo.class);
        assertThat(modifiedTestPlan.isLocked(), is(true));

    }

    @Test
    public void testCloseTestPlan_WhenTestPlanIsClosed_ShouldOpenTestPlan() {
        TestCampaign testCampaign = createTestCampaign();
        testCampaign.setLocked(true);
        app.persistence().persistInTransaction(testCampaign);

        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setId(testCampaign.getId());
        testCampaignInfo.setLocked(false);

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/status")
                .request()
                .put(Entity.entity(testCampaignInfo, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);

        Response responseWithTestPlan = searchTestCampaignById(testCampaign.getId(), true);

        assertStatus(responseWithTestPlan, Response.Status.OK);

        TestCampaignInfo modifiedTestPlan = responseWithTestPlan.readEntity(TestCampaignInfo.class);
        assertThat(modifiedTestPlan.isLocked(), is(false));
    }

    @Test
    public void testGetExecutions() {
        TestCampaign testCampaign = createTestCampaign();
        TestCase testCase = fixture().persistTestCase("A");
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();

        TestExecution execution1 = fixture().constructTestExecution(testCampaign, testCaseVersion);
        execution1.setResult(FAIL);
        execution1.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        TestExecution execution2 = fixture().constructTestExecution(testCampaign, testCaseVersion);
        execution2.setResult(PASS);
        execution2.setCreatedAt(new DateTime().plusSeconds(20).toDate());

        app.persistence().persistInTransaction(testCampaign, execution1, execution2);

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases/" + testCase.getId() + "/executions")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestExecutionInfo> executedTestCases = response.readEntity(TEST_EXECUTION_LIST);
        assertThat(executedTestCases.getTotalCount(), equalTo(2L));
        assertThat(executedTestCases.getItems().get(0).getResult().getTitle(), equalTo("Pass"));
        assertThat(executedTestCases.getItems().get(1).getResult().getTitle(), equalTo("Fail"));
    }

    @Test
    public void testCreateExecution() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);

        String requirementId = "CIP-1234_1";
        fixture().persistRequirement(requirementId, null);

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));
        info.setComment("it executed");
        info.setExecutionTime("11:23:54");
        Set<String> requirementIds = new HashSet<>();
        requirementIds.add(requirementId);
        info.setRequirementIds(requirementIds);

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        assertStatus(response, Response.Status.CREATED);

        TestExecutionInfo execution = response.readEntity(TestExecutionInfo.class);
        assertThat(execution.getId(), notNullValue());
    }

    @Test
    public void testCreateExecutionWithInvalidTime() {
        TestCampaign testCampaign = createTestCampaign();
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));
        info.setExecutionTime("60:89");

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testCreateExecutionWithInvalidType() {
        TestCampaign testCampaign = createTestCampaign();
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);

        String requirementId = "CIP-1234_1";
        fixture().persistRequirementOfType(requirementId, "MR", null);

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));
        info.setComment("it executed");
        info.setExecutionTime("00:23:54");
        Set<String> requirementIds = new HashSet<>();
        requirementIds.add(requirementId);
        info.setRequirementIds(requirementIds);

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        assertStatus(response, Response.Status.BAD_REQUEST);
        String value = response.readEntity(String.class);
        assertThat(value, containsString("Invalid requirement type"));
    }

    @Test
    public void testCreateExecutionWithInvalidKpiLength() {
        TestCampaign testCampaign = createTestCampaign();
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));
        info.setComment("it executed");
        info.setKpiMeasurement("String greater than 255 characters. iewrfieilgfegbilhelghiberhgberwluiygeuygeruygbeuygbe" +
                "wuygbehyuwbghewbgkhjebghjdbsfjhgbdsjhkgfbdhjkfgbdufsgbudsfbgdfuybgukdfybdjf;ngjkdfngkj;" +
                "dngkjdfgkjdkljgdfkjhsgkjsdfkjgjdksfgnkjdfsngjkdsfngkjndsfkjgldksfjlngkjldsfngkjnsdkfjlg" +
                "nkjsdlfngkjdsfkjgfnkjdsfngkjsdngkjdnfgg");

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        assertStatus(response, Response.Status.BAD_REQUEST);
        String value = response.readEntity(String.class);
        assertThat(value, containsString("size must be between"));
    }

    @Test
    public void testCreateTestExecutionWithoutIso() {
        TestCampaign testCampaign = createTestCampaign_withDropCapableProduct();
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        response.close();
        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testCreateTestExecutionWithIso() {
        TestCampaign testCampaign = createTestCampaign_withDropCapableProduct();
        TestCaseVersion testCaseVersion = createTestCampaignData(testCampaign);
        ISO iso = fixture().persistIso("ENM", "1.2.3");

        TestExecutionInfo info = new TestExecutionInfo();
        info.setTestPlan(testCampaign.getId());
        info.setResult(new ReferenceDataItem("pass", "pass"));
        info.setIso(new IsoInfo(iso.getId(), iso.getName(), iso.getVersion()));

        Response response = postTestExecution(testCampaign.getId(), testCaseVersion.getTestCase().getId(), info);
        response.close();
        assertStatus(response, Response.Status.CREATED);
    }

    public Response postTestExecution(Long testCampaignId, Long testCaseId, TestExecutionInfo testExecution) {
        return app.client()
                .path(TEST_PLANS_URL + testCampaignId + "/test-cases/" + testCaseId + "/executions")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(testExecution, MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetExecutedTestCases() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestExecution execution1a = fixture().constructTestExecution(testCampaign, testCampaignItem1.getTestCaseVersion());
        execution1a.setResult(FAIL);
        execution1a.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        TestExecution execution1b = fixture().constructTestExecution(testCampaign, testCampaignItem1.getTestCaseVersion());
        execution1b.setResult(PASS);
        execution1b.setCreatedAt(new DateTime().plusSeconds(20).toDate());

        TestExecution execution2 = fixture().constructTestExecution(testCampaign, testCampaignItem2.getTestCaseVersion());
        execution2.setResult(FAIL);
        execution2.setCreatedAt(new DateTime().plusSeconds(15).toDate());

        app.persistence().persistInTransaction(testCampaign, execution1a, execution1b, execution2);

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        List<TestCaseInfo> executedTestCases = response.readEntity(TEST_CASE_LIST);
        assertThat(executedTestCases.size(), equalTo(2));
    }

    @Test
    public void testTestPlanCSV() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        app.persistence().persistInTransaction(testCampaign);

        SimpleTestCaseInfo testCaseInfo = new SimpleTestCaseInfo();
        testCaseInfo.setId(testCampaignItem2.getTestCaseVersion().getTestCase().getId());
        TestInfoList testInfoList = new TestInfoList();
        testInfoList.setTestCases(Lists.newArrayList(testCaseInfo));

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .request()
                .post(Entity.entity(testInfoList, MediaType.APPLICATION_JSON_TYPE));

        assertStatus(response, Response.Status.NO_CONTENT);

        response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases/csv")
                .request()
                .get();

        String responseData = response.readEntity(String.class);
        List<String> data = Arrays.asList(responseData.split("\n"));
        assertEquals(data.get(0).trim(), headers);
        assertEquals(20, countOccurences(data.get(1), ','));
        assertEquals(20, countOccurences(data.get(2), ','));
    }

    @Test
    public void testTestPlanCSVWithFileAttached() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestExecution execution1 = fixture().constructTestExecution(testCampaign, testCampaignItem1.getTestCaseVersion());
        execution1.setResult(PASS);
        execution1.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        FileData fileData = new FileData();
        fileData.setCreated(new Date());
        fileData.setFileCategory(FileCategory.TEST_EXECUTION_FILE);
        fileData.setFilename("Simba");
        fileData.setAuthor("Arthur");
        fileData.setLocation("/anywhere");

        app.persistence().persistInTransaction(testCampaign, execution1);
        fileData.setEntityId(execution1.getId());
        app.persistence().persistInTransaction(fileData);

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases/csv")
                .request()
                .get();

        String responseData = response.readEntity(String.class);
        List<String> rowData = Arrays.asList(responseData.split("\n"));
        assertEquals(rowData.size(), 3);
        List<String> columnDataRowOne = Arrays.asList(rowData.get(1).split(","));
        List<String> columnDataRowTwo = Arrays.asList(rowData.get(2).split(","));
        assertEquals(Boolean.valueOf(columnDataRowOne.get(20).trim()), true);
        assertEquals(Boolean.valueOf(columnDataRowTwo.get(20).trim()), false);
    }

    @Test
    public void testFilteredTestCaseCSV() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        TestCampaignItem testCampaignItem3 = constructTestPlanItem(3, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);
        testCampaign.addTestCampaignItem(testCampaignItem2);
        testCampaign.addTestCampaignItem(testCampaignItem3);


        String headers = "Test Case ID,Title,Assigned,Result,Comments,Defect ID's";

        app.persistence().persistInTransaction(testCampaign);

        SimpleTestCaseInfo testInfo1 = new SimpleTestCaseInfo();
        testInfo1.setId(testCampaignItem1.getTestCaseVersion().getTestCase().getId());
        SimpleTestCaseInfo testInfo2 = new SimpleTestCaseInfo();
        testInfo2.setId(testCampaignItem2.getTestCaseVersion().getTestCase().getId());
        SimpleTestCaseInfo testInfo3 = new SimpleTestCaseInfo();
        testInfo2.setId(testCampaignItem3.getTestCaseVersion().getTestCase().getId());

        String testCaseId = testCampaignItem1.getTestCaseVersion().getTestCase().getTestCaseId();

        TestInfoList testInfoList = new TestInfoList();
        testInfoList.setTestCases(Lists.newArrayList(testInfo1, testInfo2, testInfo3));

        Response response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .request()
                .post(Entity.entity(testInfoList, MediaType.APPLICATION_JSON_TYPE));

        assertStatus(response, Response.Status.NO_CONTENT);

        response = app
                .client()
                .path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases/csv")
                .queryParam("filter", testCaseId + ",")
                .request()
                .get();

        String responseData = response.readEntity(String.class);
        List<String> data = Arrays.asList(responseData.split("\n"));
        assertEquals(data.get(0).trim(), headers);
        assertEquals(2, data.size());
        assertEquals(5, countOccurences(data.get(1), ','));

    }

    @Test
    public void testCreateMultipleTestExecutions() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestCampaignItem testCampaignItem3 = constructTestPlanItem(3, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem3);

        app.persistence().persistInTransaction(testCampaign, testCampaignItem1, testCampaignItem2, testCampaignItem3);

        List<TestExecutionInfo> listOfInfos = Lists.newArrayList();

        Long testPlanId = testCampaign.getId();
        Long testCaseId1 = testCampaignItem1.getTestCaseVersion().getTestCase().getId();
        Long testCaseId2 = testCampaignItem2.getTestCaseVersion().getTestCase().getId();
        Long testCaseId3 = testCampaignItem3.getTestCaseVersion().getTestCase().getId();

        TestExecutionInfo testExecutionInfo2 = constructTestExecutionInfo(testPlanId, testCaseId2, "fail", "it executed and failed");
        testExecutionInfo2.setDefectIds(Sets.newHashSet(DEFECT_ID));

        listOfInfos.add(constructTestExecutionInfo(testPlanId, testCaseId1, "pass", "it executed"));
        listOfInfos.add(testExecutionInfo2);
        listOfInfos.add(constructTestExecutionInfo(testPlanId, testCaseId3, "blocked", "it broke and can't be fixed"));

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() +
                        "/test-cases/executions")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(listOfInfos, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        List<TestExecutionInfo> executions = response.readEntity(TEST_EXECUTION_LIST_NO_PAGINATION);

        assertEquals(executions.size(), 3);
        assertEquals(executions.get(0).getResult().getTitle(), "Pass");
        assertEquals(executions.get(1).getResult().getTitle(), "Fail");
        assertEquals(executions.get(2).getResult().getTitle(), "Blocked");
    }

    @Test
    public void testCreateMultipleTestExecutions_withId() {
        TestCampaign testCampaign = createTestCampaign();
        app.persistence().persistInTransaction(testCampaign);

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestCampaignItem testCampaignItem3 = constructTestPlanItem(3, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem3);

        app.persistence().persistInTransaction(testCampaign, testCampaignItem1, testCampaignItem2, testCampaignItem3);

        List<TestExecutionInfo> listOfInfos = Lists.newArrayList();

        Long testPlanId = testCampaign.getId();
        Long testCaseId = testCampaignItem2.getTestCaseVersion().getTestCase().getId();
        Long testCaseId1 = testCampaignItem3.getTestCaseVersion().getTestCase().getId();
        Long testCaseId3 = testCampaignItem3.getTestCaseVersion().getTestCase().getId();

        TestExecutionInfo testExecutionInfo = constructTestExecutionInfo(testPlanId, testCaseId, "fail", "it executed and failed");
        testExecutionInfo.setId(1L); //evil id

        listOfInfos.add(testExecutionInfo);
        listOfInfos.add(constructTestExecutionInfo(testPlanId, testCaseId1, "pass", "it executed"));
        listOfInfos.add(constructTestExecutionInfo(testPlanId, testCaseId3, "blocked", "it broke and can't be fixed"));

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() +
                        "/test-cases/executions")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(listOfInfos, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.BAD_REQUEST);

    }

    @Test
    public void getLatestTestExecutions() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestCampaignItem testCampaignItem3 = constructTestPlanItem(3, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem3);

        TestExecution execution1 = fixture().constructTestExecution(testCampaign, testCampaignItem1.getTestCaseVersion());
        execution1.setResult(FAIL);
        execution1.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        TestExecution execution2 = fixture().constructTestExecution(testCampaign, testCampaignItem2.getTestCaseVersion());
        execution2.setResult(FAIL);
        execution2.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        TestExecution execution3 = fixture().constructTestExecution(testCampaign, testCampaignItem3.getTestCaseVersion());
        execution3.setResult(FAIL);
        execution3.setCreatedAt(new DateTime().plusSeconds(10).toDate());

        TestExecution execution4 = fixture().constructTestExecution(testCampaign, testCampaignItem1.getTestCaseVersion());
        execution4.setResult(PASS);
        execution4.setCreatedAt(new DateTime().plusMinutes(1).toDate());

        TestExecution execution5 = fixture().constructTestExecution(testCampaign, testCampaignItem2.getTestCaseVersion());
        execution5.setResult(PASS);
        execution5.setCreatedAt(new DateTime().plusMinutes(1).toDate());

        TestExecution execution6 = fixture().constructTestExecution(testCampaign, testCampaignItem3.getTestCaseVersion());
        execution6.setResult(PASS);
        execution6.setCreatedAt(new DateTime().plusMinutes(1).toDate());

        app.persistence().persistInTransaction(testCampaign, testCampaignItem1, testCampaignItem2, testCampaignItem3,
                execution1, execution2, execution3,
                execution4, execution5, execution6);

        Response response = app.client()
                .path(TEST_PLANS_URL + testCampaign.getId() +
                        "/test-cases/executions")
                .request()
                .accept(ACCEPTED)
                .get();

        List<TestExecutionInfo> executions = response.readEntity(TEST_EXECUTION_LIST_NO_PAGINATION);
        assertStatus(response, Response.Status.OK);

        assertEquals(executions.size(), 3);
        assertEquals(executions.get(0).getResult().getTitle(), "Pass");
        assertEquals(executions.get(1).getResult().getTitle(), "Pass");
        assertEquals(executions.get(2).getResult().getTitle(), "Pass");
    }

    @Test
    public void checkOrderOfTestCasesIsConsistant() {
        TestCampaign testCampaign = createTestCampaign();

        TestCampaignItem testCampaignItem1 = constructTestPlanItem(1, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem1);

        TestCampaignItem testCampaignItem2 = constructTestPlanItem(2, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem2);

        TestCampaignItem testCampaignItem3 = constructTestPlanItem(3, testCampaign);
        testCampaign.addTestCampaignItem(testCampaignItem3);

        app.persistence().persistInTransaction(testCampaign, testCampaignItem1, testCampaignItem2, testCampaignItem3);

        Response response = app.client().path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .queryParam("view", "detailed")
                .request()
                .accept(ACCEPTED)
                .get();

        List<TestCampaignItemInfo> testCampaignItemInfos = response.readEntity(TEST_CASE_ITEM_LIST);
        assertStatus(response, Response.Status.OK);

        assertEquals(testCampaignItemInfos.size(), 3);
        List testCaseIds = Lists.newArrayList();

        for (TestCampaignItemInfo testCampaignItemInfo : testCampaignItemInfos) {
            testCaseIds.add(testCampaignItemInfo.getTestCase().getTestCaseId());
        }

        Response response2 = app.client().path(TEST_PLANS_URL + testCampaign.getId() + "/test-cases")
                .queryParam("view", "detailed")
                .request()
                .accept(ACCEPTED)
                .get();

        int index = 0;
        List<TestCampaignItemInfo> testPlanItemEntities = response2.readEntity(TEST_CASE_ITEM_LIST);
        for (TestCampaignItemInfo testPlanItemEntity : testPlanItemEntities) {
            assertThat(testPlanItemEntity.getTestCase().getTestCaseId(), equalTo(testCaseIds.get(index)));
            index++;
        }
    }

    @Test
    public void testCopyTestCampaigns() {
        app.persistence().cleanupTables();

        Product enm = buildProduct("ENM").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, null).build();
        Drop enmDrop1 = buildDrop(enm, "16.3").build();
        Drop enmDrop2 = buildDrop(enm, "16.4").build();

        app.persistence().persistInTransaction(enm, enmFm, enmDrop1, enmDrop2);
        persistTestCampaigns(Lists.newArrayList("ENM Test Campaign #1", "ENM Test Campaign #2"), enmDrop1, enmFm, null);

        List<TestCampaignInfo> originals = searchTestCampaigns(enm.getId(), enmDrop1.getId(), enmFm.getId())
                .readEntity(TEST_PLAN_PAGE)
                .getItems();

        assertThat(originals.size(), equalTo(2));

        List<String> originalNames = originals.stream()
                .map(o -> o.getName())
                .collect(Collectors.toList());

        assertThat(originalNames, containsInAnyOrder("ENM Test Campaign #1", "ENM Test Campaign #2"));
        originals.forEach(o -> {
            assertThat(o.getProduct().getId(), equalTo(enm.getId()));
            assertThat(o.getDrop().getId(), equalTo(enmDrop1.getId()));
            assertThat(o.getFeatures().stream().findFirst().get().getId(), equalTo(enmFm.getId()));
        });

        CopyTestCampaignsRequest request = createCopyRequest(enm, enmDrop1, enmFm, enmDrop2);

        Response response = copyTestCampaigns(request);

        List<TestCampaignInfo> copies = response.readEntity(TEST_CAMPAIGN_LIST);
        assertThat(copies.size(), equalTo(2));

        List<String> copiedNames = copies.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(copiedNames, containsInAnyOrder("ENM Test Campaign #1", "ENM Test Campaign #2"));

        copies.forEach(c -> {
            assertThat(c.getProduct().getId(), equalTo(enm.getId()));
            assertThat(c.getDrop().getId(), equalTo(enmDrop2.getId()));
            assertThat(c.getFeatures().stream().findFirst().get().getId(), equalTo(enmFm.getId()));
        });
    }

    @Test
    public void testCopyTestCampaignsToDropFromAnotherProduct() {
        app.persistence().cleanupTables();

        Product enm = buildProduct("ENM").build();
        Product oss = buildProduct("OSS").build();
        Drop enmDrop = buildDrop(enm, "16.3").build();
        Drop ossDrop = buildDrop(oss, "OSS_DROP").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(enm, oss, enmFm, ossDrop, enmDrop);

        CopyTestCampaignsRequest request = createCopyRequest(enm, enmDrop, enmFm, ossDrop);
        Response response = copyTestCampaigns(request);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testCopyTestCampaignsToSameDrop() {
        app.persistence().cleanupTables();

        Product enm = buildProduct("ENM").build();
        Drop enmDrop = buildDrop(enm, "16.3").build();
        ProductFeature enmFm = buildFeature("ENM_FM", enm, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(enm, enmFm, enmDrop);

        CopyTestCampaignsRequest request = createCopyRequest(enm, enmDrop, enmFm, enmDrop);
        Response response = copyTestCampaigns(request);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    private Response copyTestCampaigns(CopyTestCampaignsRequest request) {
        return app
                .client()
                .path(TEST_CAMPAIGNS_URL + "copy")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));
    }

    private CopyTestCampaignsRequest createCopyRequest(Product product, Drop fromDrop, ProductFeature feature, Drop copyToDrop) {
        CopyTestCampaignsRequest request = new CopyTestCampaignsRequest();
        request.setProductId(product.getId());
        request.setFromDropId(fromDrop.getId());
        request.setFeatureIds(Lists.newArrayList(feature.getId()));
        request.setCopyToDropId(copyToDrop.getId());
        return request;
    }

    private int countOccurences(String s, final char chr) {
        List<Character> characters = Chars.asList(s.toCharArray());
        return FluentIterable.from(characters)
                .filter(new Predicate<Character>() {
                    @Override
                    public boolean apply(@Nullable Character input) {
                        return Objects.equals(chr, input);
                    }
                })
                .size();
    }

    private TestCampaignItem constructTestPlanItem(int id, TestCampaign testCampaign) {
        TestCampaignItem testCampaignItem = new TestCampaignItem();
        TestCase testCase = fixture().persistTestCase("testcase" + id);
        testCampaignItem.setTestCaseVersion(testCase.getCurrentVersion());
        testCampaignItem.setUser(fixture().persistUser("mruser" + id));
        testCampaignItem.setTestCampaign(testCampaign);

        return testCampaignItem;
    }

    private TestExecutionInfo constructTestExecutionInfo(Long testPlanId, Long testCaseId, String result, String comment) {
        TestExecutionInfo testExecutionInfo = new TestExecutionInfo();
        testExecutionInfo.setTestPlan(testPlanId);
        testExecutionInfo.setTestCase(testCaseId);
        testExecutionInfo.setResult(new ReferenceDataItem(result, result));
        testExecutionInfo.setComment(comment);

        return testExecutionInfo;
    }

    private TestCaseVersion createTestCampaignData(TestCampaign testCampaign) {
        TestCase testCase = fixture().persistTestCase("A");

        app.persistence().persistInTransaction(testCampaign);

        TestCampaignItem testCampaignItem = new TestCampaignItem();
        testCampaignItem.setTestCampaign(testCampaign);
        testCampaignItem.setTestCaseVersion(testCase.getCurrentVersion());
        testCampaign.addTestCampaignItem(testCampaignItem);

        app.persistence().persistInTransaction(testCampaignItem, testCampaign, testCase);

        return testCase.getCurrentVersion();
    }

    private ProductFeature createFeature() {
        Product product = buildProduct("product").build();
        ProductFeature feature = buildFeature("feature", product, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(product, feature);
        return feature;
    }

    private TestCampaign createTestCampaign() {
        Product product = buildProduct(UUID.randomUUID().toString()).build();
        ProductFeature feature = buildFeature(UUID.randomUUID().toString(), product, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(product, feature);

        Drop drop = buildDrop(product, UUID.randomUUID().toString()).build();
        app.persistence().persistInTransaction(drop);

        TestCampaign testCampaign = buildTestPlan(feature).build();
        testCampaign.setDrop(drop);

        return testCampaign;
    }

    private TestCampaign createTestCampaign_withDropCapableProduct() {
        Product product = buildProduct(UUID.randomUUID().toString()).build();
        product.setDropCapable(true);
        Drop drop = buildDrop(product, "Drop 1").build();
        ProductFeature feature = buildFeature(UUID.randomUUID().toString(), product, Sets.newHashSet()).build();
        app.persistence().persistInTransaction(product, feature, drop);
        TestCampaign testCampaign = buildTestPlan(feature).build();
        testCampaign.setDrop(drop);
        app.persistence().persistInTransaction(testCampaign);
        return testCampaign;
    }

    private TechnicalComponent createComponentEntity(String name, ProductFeature feature) {
        return new TechnicalComponent(name, feature);
    }

    private void persistTestCampaigns(List<String> names, Drop drop, ProductFeature feature, Set<TechnicalComponent> components) {
        names.forEach(name -> {
            TestCampaign testCampaign = buildTestPlan(name, drop, feature, components).build();
            app.persistence().persistInTransaction(testCampaign);
        });
    }

    private Response searchTestCampaigns(Long productId) {
        return app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .queryParam("product", productId)
                .request()
                .get();
    }

    private Response searchTestCampaigns(Long productId, Long dropId) {
        return app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .queryParam("product", productId)
                .queryParam("drop", dropId)
                .request()
                .get();
    }

    private Response searchTestCampaigns(Long productId, Long dropId, Long featureId) {
        return app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .queryParam("product", productId)
                .queryParam("drop", dropId)
                .queryParam("feature", featureId)
                .request()
                .get();
    }

    private Response searchTestCampaigns(Long productId, Long dropId, Long featureId, List<Long> componentIds, String query) {
        WebTarget target = app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .queryParam("product", productId)
                .queryParam("drop", dropId)
                .queryParam("feature", featureId);

        for (Long id : componentIds) {
            target = target.queryParam("component", id);
        }
        if (query != null) {
            target = target.queryParam("q", query);
        }
        return target.request().get();
    }

    private Response searchTestCampaignById(Long testCampaignId, boolean detailed) {
        WebTarget target = app
                .client()
                .path(TEST_PLANS_URL + testCampaignId);

        if (detailed) {
            target = target.queryParam("view", "detailed");
        }
        return target.request().get();
    }

    private Response createTestCampaign(TestCampaignInfo testCampaignInfo) {
        return app
                .client()
                .path(TEST_CAMPAIGNS_URL)
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(testCampaignInfo, MediaType.APPLICATION_JSON));
    }
}
