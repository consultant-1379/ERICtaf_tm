package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.test.fixture.TestEntityFactory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildTestCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class TestCampaignItemControllerTest extends BaseControllerLevelTest {

    public static final String ASSIGNMENTS_URL = "/tm-server/api/assignments/";
    private static final GenericType<PageWrapper<TestCampaignItemInfo>> ASSIGNMENTS_INFO_PAGE =
            new GenericType<PageWrapper<TestCampaignItemInfo>>() {
            };

    @Before
    public void setUp() {
    }

    @Test
    public void testGetAssignmentsForCurrentUser() throws Exception {
        User user = app.persistence().em()
                .createQuery("SELECT u from User u WHERE u.externalId='taf'", User.class)
                .getResultList()
                .iterator()
                .next();

        TestCampaign testCampaign1 = fixture().persistTestCampaignWithFeature("ENM", "FM");
        TestCampaign testCampaign2 = fixture().persistTestCampaignWithFeature("OSS", "PM");

        createTestPlanItem(user, testCampaign1);
        createTestPlanItem(user, testCampaign2);

        Response response = buildRequest()
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);
        response.close();
        assertThat(page.getTotalCount(), equalTo(2L));
    }

    @Test
    public void testAssignmentsAreEmptyForNonexistingUser() throws Exception {
        createUserWithAssignments(2);

        Response response = buildRequest()
                .queryParam("userId", "nonexistingUserId")
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);
        response.close();
        assertThat(page.getTotalCount(), equalTo(0L));
    }

    @Test
    public void testGetAssignmentsForExistingUser() throws Exception {
        User user = createUserWithAssignments(5);

        Response response = buildRequest()
                .queryParam("userId", user.getExternalId())
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);
        assertThat(page.getTotalCount(), equalTo(5L));
        assertThat(page.getItems(), hasSize(5));
    }

    @Test
    public void testGetAssignmentsSecondPage() throws Exception {
        User user = createUserWithAssignments(5);

        Response response = buildRequest()
                .queryParam("userId", user.getExternalId())
                .queryParam("page", 2)
                .queryParam("perPage", 3)
                .request()
                .get();
        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);
        response.close();
        assertThat(page.getTotalCount(), equalTo(5L));
        assertThat(page.getItems(), hasSize(2));
    }

    @Test
    public void testGetAssignmentsByDefaultOrder() throws Exception {
        User user = fixture().persistUser();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");
        TestCampaignItem testCampaignItem1 = createTestPlanItem(user, testCampaign);
        TestCampaignItem testCampaignItem2 = createTestPlanItem(user, testCampaign);

        Response response = buildRequest()
                .queryParam("userId", user.getExternalId())
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);
        response.close();

        assertThat(page.getItems(), hasSize(2));
        assertThat(page.getItems().get(0).getId(), equalTo(testCampaignItem2.getId()));
        assertThat(page.getItems().get(1).getId(), equalTo(testCampaignItem1.getId()));
    }

    @Test
    public void testGetAssignmentsOrderByIdAsc() throws Exception {
        User user = fixture().persistUser();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");
        TestCampaignItem testCampaignItem1 = createTestPlanItem(user, testCampaign);
        TestCampaignItem testCampaignItem2 = createTestPlanItem(user, testCampaign);

        Response response = buildRequest()
                .queryParam("userId", user.getExternalId())
                .queryParam("orderMode", "asc")
                .queryParam("orderBy", "id")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);

        assertThat(page.getItems(), hasSize(2));
        assertThat(page.getItems().get(0).getId(), equalTo(testCampaignItem1.getId()));
        assertThat(page.getItems().get(1).getId(), equalTo(testCampaignItem2.getId()));
    }

    @Test
    public void testGetFilteredAssignmentsByTitle() throws Exception {
        User user = fixture().persistUser();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");

        TestCampaignItem testCampaignItem1 = createAssignmentWithTitle(user, testCampaign, "title-1");
        TestCampaignItem testCampaignItem2 = createAssignmentWithTitle(user, testCampaign, "title-2");
        createAssignmentWithTitle(user, testCampaign, "another");

        Response response = buildRequest()
                .queryParam("userId", user.getExternalId())
                .queryParam("orderMode", "asc")
                .queryParam("orderBy", "id")
                .queryParam("q", "testCase.title~title")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        PageWrapper<TestCampaignItemInfo> page = response.readEntity(ASSIGNMENTS_INFO_PAGE);

        List<TestCampaignItemInfo> items = page.getItems();
        assertThat(items, hasSize(2));
        assertThat(items.get(0).getTestCase().getTitle(), equalTo(testCampaignItem1.getTestCaseVersion().getTitle()));
        assertThat(items.get(1).getTestCase().getTitle(), equalTo(testCampaignItem2.getTestCaseVersion().getTitle()));
    }

    private TestCampaignItem createAssignmentWithTitle(User user, TestCampaign testCampaign, String title) {
        Product product = new Product();
        String name = UUID.randomUUID().toString();
        product.setExternalId(name);
        product.setName(name);
        ProductFeature feature = new ProductFeature();
        feature.setProduct(product);
        feature.setName("feature");
        TestType testType = new TestType();
        testType.setName("Functional");
        testType.setProduct(product);


        TestCase testCase = buildTestCase()
                .withTitle(title)
                .withProductFeature(feature)
                .withType(testType)
                .withTestCaseStatus(TestCaseStatus.PRELIMINARY)
                .build();
        TestCampaignItem testCampaignItem = TestEntityFactory.buildTestPlanItem(
                user,
                testCampaign,
                testCase.getCurrentVersion()
        ).build();

        app.persistence().persistInTransaction(product, testType, feature, testCase, testCampaignItem);
        return testCampaignItem;
    }

    private WebTarget buildRequest() {
        return app.client().path(ASSIGNMENTS_URL);
    }

    private User createUserWithAssignments(int assignmentCount) {
        User user = fixture().persistUser();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature("ENM", "FM");

        for (int i = 0; i < assignmentCount; i++) {
            fixture().persistTestPlanItem(
                    user,
                    testCampaign,
                    fixture().persistTestCase().getCurrentVersion()
            );
        }

        return user;
    }

    private TestCampaignItem createTestPlanItem(User user, TestCampaign testCampaign) {
        return fixture().persistTestPlanItem(
                user, testCampaign, fixture().persistTestCase().getCurrentVersion()
        );
    }
}
