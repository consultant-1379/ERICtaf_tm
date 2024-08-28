/**
 * ****************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * <p>
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 * ****************************************************************************
 */

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.testdesign.Context;
import com.ericsson.cifwk.tm.domain.model.testdesign.FieldType;
import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestField;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestStep;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.VerifyStep;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.infrastructure.mapping.EnumReferenceMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserMapper;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.CompletionItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.RequirementInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseSubscriptionInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.ValidationError;
import com.ericsson.cifwk.tm.presentation.dto.VerifyStepInfo;
import com.ericsson.cifwk.tm.presentation.dto.VersionModification;
import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.TestRequirements.EPIC2;
import static com.ericsson.cifwk.tm.test.TestRequirements.NON_EXISTING;
import static com.ericsson.cifwk.tm.test.TestRequirements.STORY1;
import com.ericsson.cifwk.tm.test.fixture.BadRequest;
import com.ericsson.cifwk.tm.test.fixture.Faker;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.xlsx4j.exceptions.Xlsx4jException;

public class TestCaseControllerTest extends BaseControllerLevelTest {

    public static final String TEST_CASES_URL = "/tm-server/api/test-cases/";

    private static final String PROJECT_1 = "Project1";
    private static final String REQUIREMENT_ID1 = "CIP-4200";
    private static final String REQUIREMENT_ID2 = "CIP-4177";
    private static final String REQUIREMENT_ID3 = "BIP-100";
    private static final String REQUIREMENT_ID4 = "CIP-100";
    private static final String REQUIREMENT_ID5 = "ATS-100";
    private static final String TEST_CASE_TITLE = "The Test Case";
    private static final String TEST_CASE_TYPE_PERFORMANCE = "Performance";
    private static final String TEST_CASE_TYPE_FUNCTIONAL = "Functional";

    public static final long NON_EXISTING_TEST_CASE_ID = 404L;

    private static final MediaType[] ACCEPTED = {MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.WILDCARD_TYPE};

    private static final GenericType<List<String>> STRING_LIST = new GenericType<List<String>>() {
    };

    private static final GenericType<List<TestCaseInfo>> TEST_CASE_INFO_LIST = new GenericType<List<TestCaseInfo>>() {
    };

    private static final GenericType<PageWrapper<TestCaseInfo>> TEST_CASE_INFO_PAGE = new GenericType<PageWrapper<TestCaseInfo>>() {
    };

    private static final GenericType<List<BadRequest>> BAD_REQUEST_LIST = new GenericType<List<BadRequest>>() {
    };

    private Scope scope;
    private Project project;
    private EnumReferenceMapper referenceMapper;
    private TechnicalComponent component;
    private Product product;
    private ProductFeature feature;
    private ProductFeature feature2;
    private TestType testTypePerformance;
    private TestType testTypeFunctional;
    private TestTeam team;
    private TestSuite suite;

    @Before
    public void setUp() {
        referenceMapper = new EnumReferenceMapper();
        project = fixture().persistProject(PROJECT_1);
        product = project.getProduct();
        scope = fixture().persistScope("Acceptance", product);
        feature = fixture().persistFeature("ENM_FM", product);
        feature2 = fixture().persistFeature("ENM_PM", product);
        testTypePerformance = fixture().persistTestType(TEST_CASE_TYPE_PERFORMANCE, product);
        testTypeFunctional = fixture().persistTestType(TEST_CASE_TYPE_FUNCTIONAL, product);
        component = fixture().persistTechnicalComponent("Component", feature);

        team = new TestTeam();
        team.setName("Test Team");
        team.setFeature(feature);
        app.persistence().persistInTransaction(team);

        suite = new TestSuite();
        suite.setName("Test Suite");
        suite.setFeature(feature);
        app.persistence().persistInTransaction(suite);
    }

    @Test
    public void testGetAllTestCases() {
        createDefaultTestCase();
        Response response = app.client()
                .path(TEST_CASES_URL)
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);

        assertThat(page.getTotalCount(), equalTo(1L));
    }

    @Test
    public void testGetTestCasesById() {
        TestCase testCase1 = createTestCase("TestCase 1");
        TestCase testCase2 = createTestCase("TestCase 2");
        TestCase testCase3 = createTestCase("TestCase 3");
        app.persistence().persistInTransaction(testCase1, testCase2, testCase3);

        long id1 = testCase1.getId();
        long id3 = testCase3.getId();

        Response response = app.client()
                .path(TEST_CASES_URL)
                .queryParam("id", id1 + "," + id3)
                .request().get();

        assertStatus(response, Response.Status.OK);

        List<TestCaseInfo> infos = response.readEntity(TEST_CASE_INFO_LIST);
        response.close();

        assertThat(infos.get(0).getId(), equalTo(id1));
        assertThat(infos.get(1).getId(), equalTo(id3));
    }

    @Test
    public void testGetNoTestCaseIds() {
        Response response = app.client()
                .path(TEST_CASES_URL)
                .path("ids")
                .request().get();

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void testGetExistingTestCaseIds() {
        String testCaseId1 = fixture().persistTestCase().getTestCaseId();
        String testCaseId2 = fixture().persistTestCase().getTestCaseId();
        ImmutableSet<String> testCaseIds = ImmutableSet.of(
                Faker.getUUID(),
                testCaseId1,
                testCaseId2,
                Faker.getUUID()
        );

        Response response = app.client()
                .path(TEST_CASES_URL)
                .path("ids")
                .queryParam("id", Joiner.on(",").join(testCaseIds))
                .request().get();

        assertStatus(response, Response.Status.OK);

        List<String> foundIds = response.readEntity(STRING_LIST);
        response.close();
        assertThat(foundIds, hasSize(2));
        assertThat(foundIds, hasItems(testCaseId1, testCaseId2));
    }

    @Test
    public void testSearchTestCasesByType() {
        createDefaultTestCase();
        Response response = app.client()
                .path(TEST_CASES_URL)
                .queryParam("q", "type=" + TEST_CASE_TYPE_PERFORMANCE)
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);

        assertThat(page.getItems().size(), is(1));
        assertThat(page.getItems().get(0).getType().getTitle(), is(TEST_CASE_TYPE_PERFORMANCE));
        assertThat(page.getMeta(), hasKey("query"));
    }

    @Test
    public void testSearchLike() {
        createDefaultTestCase();
        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "title~test")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();

        assertThat(page.getItems().size(), equalTo(1));
    }

    @Test
    public void testSearchTestCasesByAny() {
        String requirementId1 = "CIP-222";
        Requirement requirement1 = createRequirement(requirementId1);
        String testCase1Title = "TestCase NEW 1";
        TestCase testCase1 = createTestCase(testCase1Title);
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement1);
        String testCase2Title = "TestCase NEW 2";
        TestCase testCase2 = createTestCase(testCase2Title);
        TestCaseVersion testCaseVersion2 = testCase2.getCurrentVersion();
        testCaseVersion2.addRequirement(requirement1);
        app.persistence().persistInTransaction(requirement1, testCase1, testCase2);

        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "any~TestCase&any=" + requirementId1)
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();

        List<TestCaseInfo> items = page.getItems();
        assertThat(items.size(), equalTo(2));
        Iterator<TestCaseInfo> testCaseInfoIterator = items.iterator();
        assertThat(testCaseInfoIterator.next().getTitle(), equalTo(testCase2Title));
        assertThat(testCaseInfoIterator.next().getTitle(), equalTo(testCase1Title));
    }

    @Test
    public void testSearchTestCasesByOrder() {
        String requirementId1 = "CIP-222";
        Requirement requirement1 = createRequirement(requirementId1);
        String testCase1Title = "TestCase NEW 1";
        TestCase testCase1 = createTestCase(testCase1Title);
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement1);
        String testCase2Title = "TestCase NEW 2";
        TestCase testCase2 = createTestCase(testCase2Title);
        TestCaseVersion testCaseVersion2 = testCase2.getCurrentVersion();
        testCaseVersion2.addRequirement(requirement1);
        app.persistence().persistInTransaction(requirement1, testCase1, testCase2);

        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "any~TestCase&any=" + requirementId1)
                .queryParam("orderBy", "id")
                .queryParam("orderMode", "asc")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();

        List<TestCaseInfo> items = page.getItems();
        assertThat(items.size(), equalTo(2));
        Iterator<TestCaseInfo> testCaseInfoIterator = items.iterator();
        assertThat(testCaseInfoIterator.next().getTitle(), equalTo(testCase1Title));
        assertThat(testCaseInfoIterator.next().getTitle(), equalTo(testCase2Title));
    }

    @Test
    public void testSearchTestCasesByComponent() throws Exception {
        String testCase1Title = "TestCase with Components 1";
        TestCase testCase1 = createTestCase(testCase1Title);
        String componentName = component.getName();

        TestCase testCase2 = createTestCase("TestCase with Components 2");
        TechnicalComponent otherComponent = fixture().persistTechnicalComponent("Other", feature);
        TestCaseVersion testCase2Version = testCase2.getCurrentVersion();
        testCase2Version.addTechnicalComponent(otherComponent);
        testCase2Version.clearOptionalFields();
        testCase2Version.addOptionalField(createOptionalField(componentName, componentName));

        app.persistence().persistInTransaction(testCase1, testCase2);

        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "component~" + componentName)
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();
        List<TestCaseInfo> items = page.getItems();

        // ~ MySQL returns 2 items, H2 returns 1 item
        assertTrue(items.size() == 1 || items.size() == 2);
        TestCaseInfo testCaseInfo = Iterables.getLast(items);
        assertThat(testCaseInfo.getTitle(), equalTo(testCase1Title));
        assertThat(testCaseInfo.getComponentTitle(), equalTo(componentName));
        String componentNames = testCaseInfo.getComponentTitle();
        String[] componentNameArray = componentNames.split(", ");
        assertThat(componentNameArray.length, equalTo(1));
        assertThat(componentNameArray[0], equalTo(componentName));
    }

    @Test
    public void testEqualSearchByNonExistingClassifier() {
        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "priority=undefined")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();
        assertThat(page.getItems().size(), equalTo(0));
    }

    @Test
    public void testLikeSearchByCompleteClassifier() {
        createDefaultTestCase();
        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "priority~Normal")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();
        assertThat(page.getItems().size(), equalTo(1));
    }

    @Test
    public void testLikeSearchByIncompleteClassifier() {
        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "priority~Bl")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();
        assertThat(page.getItems().size(), equalTo(0));
    }

    @Test
    public void testSearchTestCasesMixed() {
        Requirement requirement1 = createRequirement("CIP-111");
        Requirement requirement2 = createRequirement("CIP-112");
        TestCase testCase1 = createTestCase("TestCase ONE");
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement1);
        TestCase testCase2 = createTestCase("TestCase TWO");
        TestCaseVersion testCaseVersion2 = testCase2.getCurrentVersion();
        testCaseVersion2.addRequirement(requirement2);
        TestCase testCase3 = createTestCase("TestCase THREE");
        TestCaseVersion testCaseVersion3 = testCase3.getCurrentVersion();
        testCaseVersion3.addRequirement(requirement2);

        app.persistence().persistInTransaction(
                requirement1, requirement2,
                testCase1, testCase2, testCase3);

        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "any~testcase&requirementId=CIP-112")
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();

        assertThat(page.getItems().size(), equalTo(2));
    }

    @Test
    public void shouldAutoCompleteRequirements_filterByComponent() {
        TechnicalComponent component1 = fixture().persistTechnicalComponent("component1", feature);
        TechnicalComponent component2 = fixture().persistTechnicalComponent("component2", feature);

        String title1 = "compl-1z";
        String title2 = "compl-2az";
        String title3 = "compl-2b";
        TestCase testCase1 = createTestCase(title1, component1);
        TestCase testCase2 = createTestCase(title2, component1);
        TestCase testCase3 = createTestCase(title3, component2);

        app.persistence().persistInTransaction(testCase1, testCase2, testCase3);

        Long component1Id = component1.getId();
        Long component2Id = component2.getId();

        assertCompletion(getCompletion(Arrays.asList(), "compl-*", 5), Arrays.asList(title1, title2, title3));  // if no component(s) specified, return all results for product
        assertCompletion(getCompletion(Arrays.asList(component1Id, component2Id), "compl-*", 5), Arrays.asList(title1, title2, title3));
        assertCompletion(getCompletion(Arrays.asList(component1Id), "compl-*z", 5), Arrays.asList(title1, title2));
        assertCompletion(getCompletion(Arrays.asList(component2Id), "compl-2", 5), Arrays.asList(title3));
        assertCompletion(getCompletion(Arrays.asList(), "", 2), Collections.<String>emptyList());
        assertThat(getCompletion(Arrays.asList(), "compl", 0).getStatus(), equalTo(400));
    }

    private Response getCompletion(List<Long> componentIds, String search, int limit) {

        WebTarget target = app.client()
                .path(TEST_CASES_URL)
                .path("completion")
                .queryParam("product", product.getId())
                .queryParam("feature", feature.getId())
                .queryParam("search", search)
                .queryParam("limit", limit);

        if (!componentIds.isEmpty()) {
            for (Long id : componentIds) {
                target = target.queryParam("component", id);
            }
        }

        return target.request().get();
    }

    private void assertCompletion(Response response, List<String> expected) {
        assertStatus(response, Response.Status.OK);
        CompletionInfo info = response.readEntity(CompletionInfo.class);
        response.close();
        List<String> values = Lists.newArrayList();
        for (CompletionItemInfo item : info.getItems()) {
            values.add(item.getValue());
        }
        assertThat(values, equalTo(expected));
    }

    @Test
    public void testGetTestCaseByRequirement() {
        Requirement requirement1 = createRequirement("CIP-1");
        final String title1 = "The Test Case 1";
        TestCase testCase1 = createTestCase(title1);
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement1);
        final String title2 = "The Test Case 2";
        TestCase testCase2 = createTestCase(title2);
        TestCaseVersion testCaseVersion2 = testCase2.getCurrentVersion();
        testCaseVersion2.addRequirement(requirement1);

        Requirement requirement2 = createRequirement("CIP-2");
        TestCase testCase3 = createTestCase("The Test Case 3");
        TestCaseVersion testCaseVersion3 = testCase3.getCurrentVersion();
        testCaseVersion3.addRequirement(requirement2);

        app.persistence()
                .persistInTransaction(requirement1, requirement2,
                        testCase1, testCase2, testCase3);

        Response response = app.client().path(TEST_CASES_URL)
                .queryParam("q", "requirementId=" + requirement1.getExternalId())
                .request().get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCaseInfo> page = response.readEntity(TEST_CASE_INFO_PAGE);
        response.close();

        assertThat(page.getTotalCount(), equalTo(2L));
        assertThat(page.getItems(), Matchers.<TestCaseInfo>hasItems(
                hasProperty("title", is(title1)),
                hasProperty("title", is(title2))));
    }

    @Test
    public void testGetTestCase() {
        Requirement requirement = createRequirement(EPIC2);
        final String title1 = "The Test Case 1";
        TestCase testCase1 = createTestCase(title1);
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement);

        app.persistence().persistInTransaction(requirement, testCase1);

        Response response = app.client()
                .path(TEST_CASES_URL + testCase1.getTestCaseId())
                .request().get();
        assertStatus(response, Response.Status.OK);

        TestCaseInfo info = response.readEntity(TestCaseInfo.class);
        response.close();
        assertThat(info.getTitle(), equalTo(title1));
    }

    @Test
    public void testGetTestCaseVersion() {
        TestCase testCase = createTestCase("The Test Case 1");
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        Requirement requirement = createRequirement(STORY1);
        testCaseVersion.addRequirement(requirement);

        app.persistence().persistInTransaction(requirement, testCase);

        Response response = app.client()
                .path(TEST_CASES_URL + testCase.getTestCaseId())
                .queryParam("version", 0.1)
                .request().get();
        assertStatus(response, Response.Status.OK);

        TestCaseInfo info = response.readEntity(TestCaseInfo.class);
        response.close();
        assertThat(info.getId(), equalTo(testCase.getId()));
    }

    @Test
    public void testGetTestCaseByExternalId() {
        createDefaultTestCase();
        Response response = app.client()
                .path(TEST_CASES_URL + TEST_CASE_TITLE)
                .request().get();

        response.close();
        assertStatus(response, Response.Status.OK);
    }

    @Test
    public void testGetUnknownTestCases() {
        Response response = app.client().path(TEST_CASES_URL + "NONEXISTING").request().get();

        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void testGetMultiProjectTestCase() {
        Project project1 = fixture().persistProject("P1");
        Project project2 = fixture().persistProject("P2");
        Requirement requirement1 = createRequirement("R1");
        requirement1.setProject(project1);
        Requirement requirement2 = createRequirement("R2");
        requirement2.setProject(project2);
        TestCase testCase1 = createTestCase("TC1");
        TestCaseVersion testCaseVersion1 = testCase1.getCurrentVersion();
        testCaseVersion1.addRequirement(requirement1);
        testCaseVersion1.addRequirement(requirement2);

        app.persistence().persistInTransaction(
                project1, project2, requirement1, requirement2, testCase1);

        Response response = app.client()
                .path(TEST_CASES_URL + testCase1.getTestCaseId())
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);

        TestCaseInfo testCaseInfo = response.readEntity(TestCaseInfo.class);
        response.close();

        assertThat(testCaseInfo.getRequirements(), hasSize(2));
        assertThat(testCaseInfo.getRequirements(), Matchers.<RequirementInfo>hasItems(
                hasProperty("externalId", equalTo(requirement1.getExternalId())),
                hasProperty("externalId", equalTo(requirement2.getExternalId()))
        ));
    }

    @Test
    public void deleteAsAdministrator() {
        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);
        app.insertProfileForUser("taf", userProfile);

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();

        // regular delete
        Response response = app.client().path(TEST_CASES_URL + testCaseId).request().delete();
        response.close();
        assertStatus(response, Response.Status.NO_CONTENT);

        // repeated delete
        response = app.client().path(TEST_CASES_URL + testCaseId).request().delete();
        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void deleteAsTMSUserShouldFail() {
        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(false);
        app.insertProfileForUser("taf", userProfile);

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();

        // regular delete
        Response response = app.client().path(TEST_CASES_URL + testCaseId).request().delete();
        response.close();
        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void createDeleteAndRecreate() throws IOException {
        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);
        app.insertProfileForUser("taf", userProfile);

        // request load
        createDefaultRequirements();
        TestCaseInfo json = getTestCaseJson();

        // creating entity
        Response response = app.client().path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseInfo testCaseInfo = response.readEntity(TestCaseInfo.class);

        // regular delete
        Response deleteResponse = app.client().path(TEST_CASES_URL + testCaseInfo.getId()).request().delete();
        deleteResponse.close();
        assertStatus(deleteResponse, Response.Status.NO_CONTENT);

        // checking that entity has be undeleted
        Response response2 = app.client().path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response2, Response.Status.CREATED);

        TestCaseInfo testCaseInfo2 = response2.readEntity(TestCaseInfo.class);

        assertThat(testCaseInfo2.getId(), equalTo(testCaseInfo.getId()));

    }

    @Test
    public void create() throws IOException {

        // request load
        createDefaultRequirements();
        TestCaseInfo json = getTestCaseJson();

        // creating entity
        Response response = app.client().path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        // checking that returned entities contain ids
        TestCaseInfo createdEntity = response.readEntity(TestCaseInfo.class);
        TestStepInfo createdTestStep = createdEntity.getTestSteps().listIterator().next();
        VerifyStepInfo createdVerification = createdTestStep.getVerifies().iterator().next();
        Long createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);
        assertThat(createdEntity.getTestCaseId(), equalTo(json.getTestCaseId()));
        assertNotNull(createdTestStep.getId());
        assertNotNull(createdVerification.getId());

        // checking that entity exists
        response = app.client().path(TEST_CASES_URL + createdEntityId)
                .queryParam("view", "detailed")
                .request().get();
        assertStatus(response, Response.Status.OK);

        // checking step creation
        TestCaseInfo entity = response.readEntity(TestCaseInfo.class);
        TestStepInfo testStep = entity.getTestSteps().iterator().next();
        VerifyStepInfo verificationStep = testStep.getVerifies().iterator().next();
        assertEquals("newTestStep", testStep.getName());
        assertEquals("newVerificationStep", verificationStep.getName());
        assertNotNull(testStep.getId());
        assertNotNull(verificationStep.getId());

        // checking requirements
        assertThat(entity.getRequirementIds().size(), equalTo(2));
        assertThat(entity.getRequirementIds(), hasItems(REQUIREMENT_ID2, REQUIREMENT_ID1));
    }

    @Test
    public void createWithSuiteAndTeam() throws IOException {

        // request load
        createDefaultRequirements();
        TestCaseInfo json = getTestCaseJson();

        TestTeam team = new TestTeam();
        team.setName("Test Team");
        team.setFeature(feature2);
        app.persistence().persistInTransaction(team);

        TestSuite suite = new TestSuite();
        suite.setName("Test Suite");
        suite.setFeature(feature2);
        app.persistence().persistInTransaction(suite);

        ReferenceDataItem testTeam = new ReferenceDataItem(team.getId().toString(), team.getName());
        ReferenceDataItem testSuite = new ReferenceDataItem(suite.getId().toString(), suite.getName());

        json.setTestSuite(testSuite);
        json.setTestTeam(testTeam);

        // creating entity
        Response response = app.client().path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseInfo createdEntity = response.readEntity(TestCaseInfo.class);
        assertThat(createdEntity.getTestSuite().getTitle(), equalTo(testSuite.getTitle()));
        assertThat(createdEntity.getTestTeam().getTitle(), equalTo(testTeam.getTitle()));

    }

    @Test
    public void create_get_andCheckRequirementIDSorting() throws IOException {

        createDefaultRequirements();
        fixture().persistRequirement(REQUIREMENT_ID3, project);
        fixture().persistRequirement(REQUIREMENT_ID4, project);
        fixture().persistRequirement(REQUIREMENT_ID5, project);

        TestCaseInfo json = getTestCaseJson();
        json.getRequirementIds().add(REQUIREMENT_ID3);
        json.getRequirementIds().add(REQUIREMENT_ID4);
        json.getRequirementIds().add(REQUIREMENT_ID5);

        List requirmentIds = Lists.newArrayList();
        requirmentIds.add(REQUIREMENT_ID5);
        requirmentIds.add(REQUIREMENT_ID3);
        requirmentIds.add(REQUIREMENT_ID4);
        requirmentIds.add(REQUIREMENT_ID2);
        requirmentIds.add(REQUIREMENT_ID1);

        Iterator<String> requirmentIdsIterator = requirmentIds.iterator();

        // creating entity
        Response response = app.client().path(TEST_CASES_URL)
                .request()
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseInfo createdEntity = response.readEntity(TestCaseInfo.class);
        response.close();

        assertThat(createdEntity.getRequirementIds().size(), equalTo(5));
        Long createdEntityId = createdEntity.getId();

        Iterator<RequirementInfo> requirementsIterator = createdEntity.getRequirements().iterator();

        while (requirementsIterator.hasNext() && requirmentIdsIterator.hasNext()) {
            RequirementInfo requirementInfo = requirementsIterator.next();
            String result = requirmentIdsIterator.next();
            assertThat(requirementInfo.getExternalId(), equalTo(result));
        }

        response = app.client().path(TEST_CASES_URL + createdEntityId)
                .queryParam("view", "detailed")
                .request().get();

        assertStatus(response, Response.Status.OK);
        TestCaseInfo entity = response.readEntity(TestCaseInfo.class);
        response.close();

        assertThat(createdEntity.getRequirementIds().size(), equalTo(5));

        requirementsIterator = entity.getRequirements().iterator();

        while (requirementsIterator.hasNext() && requirmentIdsIterator.hasNext()) {
            RequirementInfo requirementInfo = requirementsIterator.next();
            String result = requirmentIdsIterator.next();
            assertThat(requirementInfo.getExternalId(), equalTo(result));
        }
    }

    @Test
    public void testCaseValidationErrors() {

        // base entity without mandatory fields
        TestCaseInfo json = getTestCaseJson();
        json.clearGroups();
        json.clearTestSteps();
        json.clearContexts();
        json.setTitle("");
        json.setType(null);
        json.setTestCaseId("");
        json.setTechnicalComponents(new LinkedHashSet<>());
        json.setRequirementIds(Sets.newLinkedHashSet(Lists.newArrayList(NON_EXISTING)));
        json.setPriority(null);

        // checking validation error messages
        Response response = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError[] validationErrors = response.readEntity(ValidationError[].class);

        Arrays.sort(validationErrors);
        assertEquals(6, validationErrors.length);
        int i = 0;
        assertThat(validationErrors[i++].getMessage(), equalTo("Field 'priority' must not be null"));
        assertThat(validationErrors[i++].getMessage(), equalTo("Field 'technicalComponents' must not be null or empty"));
        assertThat(validationErrors[i++].getMessage(), equalTo("Field 'testCaseId' must not be null or empty"));
        assertThat(validationErrors[i++].getMessage(), equalTo("Field 'title' must not be null or empty"));
        assertThat(validationErrors[i++].getMessage(), equalTo("Field 'type' must not be null"));
        assertThat(validationErrors[i++].getMessage(), equalTo("One or more requirements linked to Test Case " +
                "could not be linked to JIRA Epic/Story/MR/Improvement"));

        // retry with components set to null
        json.setTechnicalComponents(null);

        Response response2 = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError[] validationErrors2 = response2.readEntity(ValidationError[].class);

        Arrays.sort(validationErrors2);
        assertEquals(8, validationErrors2.length);
        i = 0;
        assertThat(validationErrors2[i++].getMessage(), equalTo("At least one field out of [technicalComponents, component] must not be null or empty"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("Field 'priority' must not be null"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("Field 'technicalComponents' must not be null or empty"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("Field 'testCaseId' must not be null or empty"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("Field 'title' must not be null or empty"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("Field 'type' must not be null"));
        assertThat(validationErrors2[i++].getMessage(), equalTo("One or more requirements linked to Test Case " +
                "could not be linked to JIRA Epic/Story/MR/Improvement"));
        assertThat(validationErrors2[i].getMessage(), equalTo("The selected feature and component is not valid"));
    }

    @Test
    public void createTestWithNonUniqueTestCaseId() {
        TestCase testCase = createDefaultTestCase();

        // new test case with already existing test case id
        TestCaseInfo json = getTestCaseJson();
        json.setTestCaseId(testCase.getTestCaseId());

        // checking validation error message
        Response response = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError validationError = response.readEntity(ValidationError[].class)[0];
        response.close();
        assertThat(validationError.getMessage(), equalTo("Test with such test case id already exists"));
    }

    @Test
    public void updateTestWithNonUniqueTestCaseId() {
        TestCase testCase = createDefaultTestCase();

        // creating another entity
        TestCaseInfo json = getTestCaseJson();
        Response response = app.client().path(TEST_CASES_URL)
                .request().accept(ACCEPTED)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.CREATED);
        TestCaseInfo createdEntity = response.readEntity(TestCaseInfo.class);
        response.close();
        Long newEntityId = createdEntity.getId();

        // updating entity with test case id of current entity - SUCCESS
        json.setId(newEntityId);
        json.setDescription("updated description");
        response = app.client().path(TEST_CASES_URL + newEntityId)
                .request().accept(ACCEPTED).put(Entity.entity(json, MediaType.APPLICATION_JSON));
        response.close();
        assertStatus(response, Response.Status.OK);

        // updating entity with test case id of another entity - FAILURE
        json.setTestCaseId(testCase.getTestCaseId());
        response = app.client().path(TEST_CASES_URL + newEntityId)
                .request().accept(ACCEPTED).put(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError validationError = response.readEntity(ValidationError[].class)[0];
        response.close();
        assertThat(validationError.getMessage(), equalTo("Test with such test case id already exists"));
    }

    @Test
    public void updateRequirements() {
        String rId1 = "XXX-1";
        String rId2 = "XXX-2";
        String rId3 = "XXX-3";
        app.persistence().persistInTransaction(
                fixture().persistRequirement(rId1, project),
                fixture().persistRequirement(rId2, project),
                fixture().persistRequirement(rId3, project)
        );

        TestCaseInfo json = getTestCaseJson();
        json.setRequirementIds(Sets.newLinkedHashSet(Lists.newArrayList(rId1, rId2)));
        Response response = app.client()
                .path(TEST_CASES_URL)
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseInfo entity = response.readEntity(TestCaseInfo.class);
        response.close();

        assertEquals(entity.getRequirementIds(), Sets.newHashSet(rId1, rId2));

        Long entityId = entity.getId();
        json.setId(entityId);
        json.setRequirementIds(Sets.newLinkedHashSet(Lists.newArrayList(rId2, rId3)));
        Response response2 = app.client()
                .path(TEST_CASES_URL + entityId)
                .request()
                .accept(ACCEPTED)
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        response2.close();

        TestCaseInfo updatedEntity = app.client()
                .path(TEST_CASES_URL + entityId)
                .queryParam("view", "simple-requirements")
                .request()
                .accept(ACCEPTED)
                .get(TestCaseInfo.class);

        assertThat(updatedEntity.getRequirementIds(), containsInAnyOrder(rId2, rId3));
    }

    @Test
    public void testStepValidationErrors() {

        // test case with invalid test step
        TestCaseInfo json = getTestCaseJson();
        TestStepInfo testStep = json.getTestSteps().iterator().next();
        testStep.setName("");
        testStep.clearVerifies();

        // checking validation error messages
        Response response = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError[] validationErrors = response.readEntity(ValidationError[].class);
        response.close();
        Arrays.sort(validationErrors);
        assertEquals(1, validationErrors.length);
        assertThat(validationErrors[0].getMessage(), equalTo("Field 'testStepName' must not be null or empty"));
    }

    @Test
    public void testFieldLengthValidationErrors() {
        TestCaseInfo json = getTestCaseJson();
        json.setTitle(generateLongString());
        json.getTestSteps().get(0).setName(generateLongString());

        Response response = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.BAD_REQUEST);

        ValidationError[] validationErrors = response.readEntity(ValidationError[].class);
        response.close();
        Arrays.sort(validationErrors);

        assertEquals(1, validationErrors.length);
        assertThat(validationErrors[0].getMessage(), equalTo("Field 'title' must be shorter than 255 characters"));
    }

    private String generateLongString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append(i % 10);
        }
        return sb.toString();
    }

    @Test
    public void verificationValidationErrors() {

        // test case with invalid verification
        TestCaseInfo json = getTestCaseJson();
        VerifyStepInfo verification = json.getTestSteps().iterator().next().getVerifies().iterator().next();
        verification.setName("");

        // checking validation error messages
        Response response = app.client().path(TEST_CASES_URL).request().accept(ACCEPTED).post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.BAD_REQUEST);
        ValidationError[] validationErrors = response.readEntity(ValidationError[].class);
        assertEquals(1, validationErrors.length);
        assertThat(validationErrors[0].getMessage(), equalTo("Field 'verifyStepName' must not be null or empty"));
    }

    @Test
    public void updateExisting() {
        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();
        app.client().login();

        // updating entity
        TestCaseInfo json = getTestCaseJson();
        updateJson(json, testCase);
        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
        response.close();
        assertStatus(response, Response.Status.OK);

        // checking that entity updated
        response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        TestCaseInfo updatedEntity = response.readEntity(TestCaseInfo.class);
        assertEquals("Updated test case", updatedEntity.getDescription());

        // checking that user is audited
        List<VersionModification> modifications = updatedEntity.getModifications();
        assertThat(modifications, hasSize(2));
        assertThat(modifications.get(0).getUsername(), containsString("taf"));
        assertThat(modifications.get(0).getTimestamp(), notNullValue());
    }

    @Test
    public void updateNonExisting() {
        TestCaseInfo json = getTestCaseJson();
        json.setId(NON_EXISTING_TEST_CASE_ID);
        Response response = app.client().path(TEST_CASES_URL + NON_EXISTING_TEST_CASE_ID).request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void updateSteps() {
        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();

        // checking that entity updated
        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        TestCaseInfo currentEntity = response.readEntity(TestCaseInfo.class);
        response.close();
        Collection<String> testStepNames = getTestStepNames(currentEntity);
        assertEquals(2, testStepNames.size());
        assertTrue(testStepNames.contains("testStep1"));
        assertTrue(testStepNames.contains("testStep2"));
        Collection<String> verificationNames = getVerificationNames(currentEntity);
        assertEquals(4, verificationNames.size());
        assertTrue(verificationNames.contains("testStep1Verification1"));
        assertTrue(verificationNames.contains("testStep1Verification2"));
        assertTrue(verificationNames.contains("testStep2Verification1"));
        assertTrue(verificationNames.contains("testStep2Verification2"));

        // updating steps
        TestCaseInfo json = getTestCaseJson();
        updateJson(json, testCase);
        response = app.client().path(TEST_CASES_URL + testCaseId).request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
        response.close();
        assertStatus(response, Response.Status.OK);

        // checking that steps are updated
        response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        TestCaseInfo updatedEntity = response.readEntity(TestCaseInfo.class);
        response.close();
        testStepNames = getTestStepNames(updatedEntity);
        assertEquals(2, testStepNames.size());
        assertTrue(testStepNames.contains("newTestStep"));
        assertTrue(testStepNames.contains("updatedTestStep"));

        verificationNames = getVerificationNames(updatedEntity);
        assertEquals(2, verificationNames.size());
        assertTrue(verificationNames.contains("newVerificationStep"));
        assertTrue(verificationNames.contains("updatedVerificationStep"));
    }

    @Test
    public void reviewTestCaseWithReviewGroup() {
        User user = fixture().persistUser("taffo", "taffo.taffington@taf.com", "taffo");
        ReviewGroup reviewGroup = new ReviewGroup();
        reviewGroup.setName("group");
        reviewGroup.setUsers(Sets.newHashSet(user));
        app.persistence().persistInTransaction(reviewGroup);

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();
        String approved = "Approved";
        String rejected = "Rejected";

        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        response.close();

        TestCaseInfo json = getTestCaseJson();
        updateJson(json, testCase);
        response = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", rejected)
                .queryParam("type", "unknown")
                .queryParam("reviewGroup", reviewGroup.getId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);

        TestCaseInfo testCaseInfo = response.readEntity(TestCaseInfo.class);

        assertThat(testCaseInfo.getTestCaseStatus().getTitle(), equalTo(rejected));
        assertThat(testCaseInfo.getVersion(), equalTo("0.1"));

        Response response2 = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", approved)
                .queryParam("type", "major")
                .queryParam("reviewGroup", reviewGroup.getId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response2, Response.Status.OK);

        TestCaseInfo testCaseInfo2 = response2.readEntity(TestCaseInfo.class);
        assertThat(testCaseInfo2.getTestCaseStatus().getTitle(), equalTo(approved));
        assertThat(testCaseInfo2.getVersion(), equalTo("1.0"));

        Response response3 = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", rejected)
                .queryParam("type", "unknown")
                .queryParam("reviewGroup", reviewGroup.getId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response3, Response.Status.BAD_REQUEST);

    }

    @Test
    public void reviewTestCaseWithSingleUser() {
        User user = fixture().persistUser("taffo", "taffo.taffington@taf.com", "taffo");

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();
        String approved = "Approved";
        String rejected = "Rejected";

        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        response.close();

        TestCaseInfo json = getTestCaseJson();

        UserMapper userMapper = new UserMapper();
        json.setReviewUser(userMapper.mapEntity(user, UserInfo.class));

        updateJson(json, testCase);
        response = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", rejected)
                .queryParam("type", "unknown")
                .queryParam("reviewUser", user.getExternalId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);

        TestCaseInfo testCaseInfo = response.readEntity(TestCaseInfo.class);

        assertThat(testCaseInfo.getTestCaseStatus().getTitle(), equalTo(rejected));
        assertThat(testCaseInfo.getVersion(), equalTo("0.1"));

        Response response2 = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", approved)
                .queryParam("type", "major")
                .queryParam("reviewUser", user.getExternalId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response2, Response.Status.OK);

        TestCaseInfo testCaseInfo2 = response2.readEntity(TestCaseInfo.class);
        assertThat(testCaseInfo2.getTestCaseStatus().getTitle(), equalTo(approved));

        Response response3 = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", rejected)
                .queryParam("type", "unknown")
                .queryParam("reviewUser", user.getExternalId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response3, Response.Status.BAD_REQUEST);
    }

    @Test
    public void reviewTestCaseWithBothReviewTypes() {
        User user = fixture().persistUser("taffo", "taffo.taffington@taf.com", "taffo");
        ReviewGroup reviewGroup = new ReviewGroup();
        reviewGroup.setName("group");
        reviewGroup.setUsers(Sets.newHashSet(user));
        app.persistence().persistInTransaction(reviewGroup);

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();
        String approved = "Approved";

        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        response.close();

        TestCaseInfo json = getTestCaseJson();
        UserMapper userMapper = new UserMapper();
        json.setReviewUser(userMapper.mapEntity(user, UserInfo.class));

        updateJson(json, testCase);
        response = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", approved)
                .queryParam("type", "unknown")
                .queryParam("reviewGroup", reviewGroup.getId())
                .queryParam("reviewUser", user.getExternalId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.BAD_REQUEST);

    }

    @Test
    public void cancelReviewOfTestCase() {
        User user = fixture().persistUser("taffo", "taffo.taffington@taf.com", "taffo");

        TestCase testCase = createDefaultTestCase();
        Long testCaseId = testCase.getId();
        String review = "Review";
        String cancelled = "Cancelled";

        Response response = app.client().path(TEST_CASES_URL + testCaseId)
                .queryParam("view", "detailed")
                .request().get();
        response.close();

        TestCaseInfo json = getTestCaseJson();

        UserMapper userMapper = new UserMapper();
        json.setReviewUser(userMapper.mapEntity(user, UserInfo.class));

        updateJson(json, testCase);
        response = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", review)
                .queryParam("type", "unknown")
                .queryParam("reviewUser", user.getExternalId())
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.OK);

        Response response2 = app.client().path(TEST_CASES_URL + testCaseId + "/review")
                .queryParam("status", cancelled)
                .queryParam("type", "unknown")
                .request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertStatus(response2, Response.Status.OK);
        TestCaseInfo testCaseInfo = response2.readEntity(TestCaseInfo.class);
        assertThat(testCaseInfo.getTestCaseStatus().getTitle(), equalTo(cancelled));

    }

    @Test
    public void generateExportReportTest() throws Xlsx4jException, Docx4JException, IOException {

        createDefaultTestCase();

        Response generatedReport = app.client().path(TEST_CASES_URL + "export")
                .request()
                .accept(ACCEPTED)
                .get();

        assertStatus(generatedReport, Response.Status.OK);

        InputStream inputStream = generatedReport.readEntity(InputStream.class);
        SpreadsheetMLPackage pkg = (SpreadsheetMLPackage) SpreadsheetMLPackage.load(inputStream);
        inputStream.close();
        WorksheetPart worksheet = pkg.getWorkbookPart().getWorksheet(0);

        assertThat(worksheet.getContents().getSheetData().getRow().size(), equalTo(2));
        assertThat(worksheet.getContents().getSheetData().getRow().get(0).getC().size(), equalTo(14));
    }

    @Test
    public void importExcelReportTest() throws Xlsx4jException, Docx4JException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("reporting/testImportReport.xlsx");
        File fileToUpload = new File(resource.toURI());

        createDefaultRequirements();

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload,
                MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = app.client().path(TEST_CASES_URL + "import")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
        assertStatus(response, Response.Status.OK);

        List<TestCaseInfo> infos = response.readEntity(TEST_CASE_INFO_LIST);

        assertThat(infos.size(), equalTo(2));
        assertThat(infos.get(0).getTestCaseId(), equalTo("Test1"));
        assertThat(infos.get(1).getTestCaseId(), equalTo("Test2"));

    }

    @Test
    public void importBadExcelReportTest() throws Xlsx4jException, Docx4JException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("reporting/testBadImportReport.xlsx");
        File fileToUpload = new File(resource.toURI());

        createDefaultRequirements();

        FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload,
                MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = app.client().path(TEST_CASES_URL + "import")
                .request()
                .accept(ACCEPTED)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));

        assertStatus(response, Response.Status.BAD_REQUEST);

        List<BadRequest> badRequests = response.readEntity(BAD_REQUEST_LIST);

        assertThat(badRequests.size(), equalTo(6));
        assertThat(badRequests.get(0).getMessage(), containsString("Component doesn't exist or is not connected to the selected feature"));
        assertThat(badRequests.get(1).getMessage(), containsString("Test1 - Test case cannot be updated while in review or approved status"));
        assertThat(badRequests.get(2).getMessage(), containsString("Test type does not have the correct value"));
        assertThat(badRequests.get(3).getMessage(), containsString("Priority does not have the correct value"));
        assertThat(badRequests.get(4).getMessage(),
                containsString("Requirement is not connected to project: CIP-42001"));
        assertThat(badRequests.get(5).getMessage(),
                containsString("Product was not found from requirement specified"));

    }

    @Test
    public void subscribeToTestCase() {
        createDefaultTestCase();
        UserProfile userProfile = new UserProfile();
        app.insertProfileForUser("taf", userProfile);

        TestCaseSubscriptionInfo subscriptionInfo = new TestCaseSubscriptionInfo();
        subscriptionInfo.setTestCaseId(TEST_CASE_TITLE);
        subscriptionInfo.setUserId(userProfile.getUser().getExternalId());

        Response response = app.client().path(TEST_CASES_URL + "subscription")
                .request()
                .post(Entity.entity(subscriptionInfo, MediaType.APPLICATION_JSON));

        assertStatus(response, Response.Status.CREATED);

        TestCaseSubscriptionInfo createdSubscription = response.readEntity(TestCaseSubscriptionInfo.class);
        assertThat(createdSubscription.getTestCaseId(), equalTo(TEST_CASE_TITLE));
        assertThat(createdSubscription.getUserId(), equalTo(userProfile.getUser().getExternalId()));
        assertTrue(createdSubscription.isSubscribed());

        response = app.client().path(TEST_CASES_URL + "subscription")
                .queryParam("testCaseId", TEST_CASE_TITLE)
                .queryParam("userId", userProfile.getUser().getExternalId())
                .request()
                .get();

        TestCaseSubscriptionInfo maybeSubscription = response.readEntity(TestCaseSubscriptionInfo.class);
        assertThat(createdSubscription.getTestCaseId(), equalTo(TEST_CASE_TITLE));
        assertThat(createdSubscription.getUserId(), equalTo(userProfile.getUser().getExternalId()));
        assertTrue(createdSubscription.isSubscribed());
    }

    private TestCase createDefaultTestCase() {
        TestCase testCase = createTestCase(TEST_CASE_TITLE);
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        Requirement requirement1 = fixture().persistRequirement(REQUIREMENT_ID1, project);
        Requirement requirement2 = fixture().persistRequirement(REQUIREMENT_ID2, project);
        testCaseVersion.addRequirement(requirement1);
        testCaseVersion.addRequirement(requirement2);
        testCaseVersion.setProductFeature(feature);
        testCaseVersion.setTestCaseStatus(TestCaseStatus.PRELIMINARY);
        app.persistence().persistInTransaction(requirement1, requirement2, testCase);
        return testCase;
    }

    private void createDefaultRequirements() {
        Requirement requirement1 = fixture().persistRequirement(REQUIREMENT_ID1, project);
        Requirement requirement2 = fixture().persistRequirement(REQUIREMENT_ID2, project);
        app.persistence().persistInTransaction(requirement1, requirement2);
    }

    private Collection<String> getTestStepNames(TestCaseInfo testCase) {
        List<TestStepInfo> testSteps = testCase.getTestSteps();
        return Collections2.transform(testSteps, new Function<TestStepInfo, String>() {
            @Override
            public String apply(TestStepInfo testStep) {
                return testStep.getName();
            }
        });
    }

    private Collection<String> getVerificationNames(TestCaseInfo testCase) {
        List<String> result = Lists.newArrayList();
        for (TestStepInfo testStep : testCase.getTestSteps()) {
            for (VerifyStepInfo verification : testStep.getVerifies()) {
                result.add(verification.getName());
            }
        }
        return result;
    }

    private Requirement createRequirement(String id) {
        return fixture().persistRequirement(id, project);
    }

    private TestCase createTestCase(String title) {
        TestCase testCase = TestEntityFactory
                .buildTestCase()
                .withTestCaseId(title)
                .withTitle(title)
                .withProductFeature(feature)
                .withType(testTypePerformance)
                .withDescription(title)
                .withPriority(Priority.NORMAL)
                .addScope(scope)
                .addTechnicalComponent(component)
                .withOptionalField(createComponentField(component.getName()))
                .withTestSuite(suite)
                .withTestTeam(team)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();
        TestCaseVersion result = testCase.getCurrentVersion();
        TestStep testStep1 = createTestStep("testStep1");
        TestStep testStep2 = createTestStep("testStep2");
        result.addTestStep(testStep1);
        result.addTestStep(testStep2);

        return testCase;
    }

    private TestCase createTestCase(String title, TechnicalComponent component) {
        TestCase testCase = createTestCase(title);
        testCase.getCurrentVersion().addTechnicalComponent(component);
        return testCase;
    }

    private TestField createComponentField(String componentName) {
        return createOptionalField(TestField.COMPONENT, componentName);
    }

    private TestField createOptionalField(String name, String value) {
        return TestEntityFactory.buildTestField()
                .withFieldType(FieldType.STRING)
                .withName(name)
                .withValue(value)
                .build();
    }

    private TestStep createTestStep(String name) {
        TestStep testStep = new TestStep();
        testStep.setTitle(name);
        testStep.addVerification(createVerification(name + "Verification1"));
        testStep.addVerification(createVerification(name + "Verification2"));
        return testStep;
    }

    private VerifyStep createVerification(String name) {
        VerifyStep verifyStep = new VerifyStep();
        verifyStep.setVerifyStep(name);
        return verifyStep;
    }

    private TestCaseInfo getTestCaseJson() {
        VerifyStepInfo newVerifyStep = new VerifyStepInfo();
        newVerifyStep.setName("newVerificationStep");

        TestStepInfo newTestStep = new TestStepInfo();
        newTestStep.setName("newTestStep");
        newTestStep.addVerify(newVerifyStep);

        TestCaseInfo json = new TestCaseInfo();
        json.setTitle("title");
        json.setType(new ReferenceDataItem(testTypePerformance.getId().toString(), testTypePerformance.getName()));
        json.setPackageName("com.ericsson");
        json.setPrecondition("true == true");
        json.addTechnicalComponent(referenceMapper.mapEntity(component, new ReferenceDataItem()));
        json.setDescription("Updated test case");
        json.setTestCaseId("testCaseId");
        json.setTestCaseStatus(referenceMapper.mapEntity(TestCaseStatus.PRELIMINARY, new ReferenceDataItem()));
        json.setPriority(referenceMapper.mapEntity(Priority.MINOR, new ReferenceDataItem()));
        json.addTestStep(newTestStep);
        json.addContext(referenceMapper.mapEntity(Context.REST, new ReferenceDataItem()));
        json.addGroup(new ReferenceDataItem(scope.getId().toString(), scope.getName()));
        json.setFeature(new FeatureInfo(feature.getId(), feature.getName()));
        LinkedHashSet<String> requirementIds = Sets.newLinkedHashSet();
        requirementIds.add(REQUIREMENT_ID1);
        requirementIds.add(REQUIREMENT_ID2);
        json.setRequirementIds(requirementIds);
        return json;
    }

    private void updateJson(TestCaseInfo json, TestCase testCase) {
        TestCaseVersion testCaseVersion = testCase.getCurrentVersion();
        TestStep testStep = testCaseVersion.getTestSteps().iterator().next();
        VerifyStep verifyStep = testStep.getVerifications().iterator().next();
        VerifyStepInfo updatedVerifyStep = new VerifyStepInfo();
        updatedVerifyStep.setName("updatedVerificationStep");
        updatedVerifyStep.setId(verifyStep.getId());
        TestStepInfo updatedTestStep = new TestStepInfo();
        updatedTestStep.setName("updatedTestStep");
        updatedTestStep.setId(testStep.getId());
        updatedTestStep.addVerify(updatedVerifyStep);
        json.addTestStep(updatedTestStep);
        json.setId(testCase.getId());
    }

}
