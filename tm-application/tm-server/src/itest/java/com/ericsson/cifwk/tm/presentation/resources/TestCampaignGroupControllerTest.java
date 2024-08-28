/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 * <p/>
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import jersey.repackaged.com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestCampaignGroupControllerTest extends BaseControllerLevelTest {

    public static final String TEST_CAMPAIGN_GROUP_URL = "/tm-server/api/testCampaignGroup/";

    private TestCampaignGroup testCampaignGroup;
    private Product product;
    private String testCampaignGroupId;
    private final String separator = ",";

    private static final GenericType<PageWrapper<TestCampaignGroupInfo>> TEST_CAMPAIGN_GROUP_INFO_PAGE = new GenericType<PageWrapper<TestCampaignGroupInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product("Campaign Group Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        testCampaignGroup = new TestCampaignGroup();
        testCampaignGroup.setName("Test Campaign Group");
        testCampaignGroup.setProduct(product);
        app.persistence().persistInTransaction(testCampaignGroup);
        testCampaignGroupId = "" + testCampaignGroup.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL + testCampaignGroupId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(TEST_CAMPAIGN_GROUP_URL + testCampaignGroupId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());


        TestCampaignGroupInfo json = new TestCampaignGroupInfo();
        json.setName("New Test Campaign Group");
        json.setProduct(productInfo);

        // creating entity
        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(TEST_CAMPAIGN_GROUP_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<TestCampaignGroupInfo> page = response.readEntity(TEST_CAMPAIGN_GROUP_INFO_PAGE);
        assertEquals("New Test Campaign Group", page.getItems().get(0).getName());
    }

    @Test
    public void createWithEmptyGroupCampaignName() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());

        TestCampaignInfo json = new TestCampaignInfo();
        json.setName("");
        json.setProduct(productInfo);

        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void createAndDeleteAsAnotherUser() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());

        TestCampaignGroupInfo json = new TestCampaignGroupInfo();
        json.setName("Created Test Campaign Group");
        json.setProduct(productInfo);

        // creating entity
        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        app.client().logout();

        UserProfile userProfile = new UserProfile();

        app.client().loginAsUser("taf2", "taf2");
        app.insertProfileForUser("taf2", userProfile);

        Response deleteResponse = app.client().path(TEST_CAMPAIGN_GROUP_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(400, deleteResponse.getStatus());

    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // updating entity
        TestCampaignGroupInfo json = new TestCampaignGroupInfo();
        json.setName("Updated Test Campaign Group");
        json.setId(Long.parseLong(testCampaignGroupId));
        json.setProduct(productInfo);

        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL + testCampaignGroupId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(TEST_CAMPAIGN_GROUP_URL)
                .queryParam("q", "id=" + testCampaignGroupId)
                .request().get();

        PageWrapper<TestCampaignGroupInfo> page = response.readEntity(TEST_CAMPAIGN_GROUP_INFO_PAGE);
        assertEquals("Updated Test Campaign Group", page.getItems().get(0).getName());
    }

    @Test
    public void updateExistingWithEmptyName() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        TestCampaignInfo json = new TestCampaignInfo();
        json.setName("");
        json.setId(Long.parseLong(testCampaignGroupId));
        json.setProduct(productInfo);

        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL + testCampaignGroupId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(400, response.getStatus());
        response.close();
    }

    @Test
    public void getTestCampaignGroupCSV() {
        TestCase testCase = fixture().persistTestCase("test Group test");
        Product product = testCase.getCurrentVersion().getProductFeature().getProduct();
        TestCampaign testCampaign = fixture().persistTestCampaignWithFeature(product, "PM");
        testCampaign.setGroup(Sets.newHashSet(testCampaignGroup));

        TestCampaignItem testCampaignItem = new TestCampaignItem();
        testCampaignItem.setTestCampaign(testCampaign);
        testCampaignItem.setTestCaseVersion(testCase.getCurrentVersion());

        testCampaign.addTestCampaignItem(testCampaignItem);

        app.persistence().persistInTransaction(testCampaign);

        Response response = app.client().path(TEST_CAMPAIGN_GROUP_URL + "csv/" + testCampaignGroupId)
                .request().get();

        String csvData = response.readEntity(String.class);
        List<String> data = Arrays.asList(csvData.split("\n"));
        assertEquals(2, data.size());
        String[] header = data.get(0).split(separator);
        String[] row1 = data.get(1).split(separator);
        assertEquals(23, header.length);
        assertEquals(23, row1.length);

        assertEquals(row1[0], testCase.getTestCaseId());
        assertEquals(row1[1], testCase.getCurrentVersion().getTitle());
        assertEquals(row1[2], testCampaign.getName());
    }

}
