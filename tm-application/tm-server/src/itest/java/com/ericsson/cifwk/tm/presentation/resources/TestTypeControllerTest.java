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

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestTypeInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTypeControllerTest extends BaseControllerLevelTest {

    public static final String TEST_TYPE_URL = "/tm-server/api/test-types/";

    private TestType testType;
    private Product product;

    private String testTypeId;

    private static final GenericType<PageWrapper<TestTypeInfo>> TEST_TYPE_INFO_PAGE = new GenericType<PageWrapper<TestTypeInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product("Test Type Product");
        product.setName("Test Type Product");
        app.persistence().persistInTransaction(product);

        testType = new TestType();
        testType.setName("Test Type Test");
        testType.setProduct(product);
        app.persistence().persistInTransaction(testType);
        testTypeId = "" + testType.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        Response response = app.client().path(TEST_TYPE_URL + testTypeId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(TEST_TYPE_URL + testTypeId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // request load
        TestTypeInfo json = new TestTypeInfo();
        json.setName("New Test Type");
        json.setProduct(productInfo);

        // creating entity
        Response response = app.client().path(TEST_TYPE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(TEST_TYPE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<TestTypeInfo> page = response.readEntity(TEST_TYPE_INFO_PAGE);
        assertEquals("New Test Type", page.getItems().get(0).getName());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // updating entity
        TestTypeInfo json = new TestTypeInfo();
        json.setName("Updated Test Type");
        json.setId(Long.parseLong(testTypeId));

        json.setProduct(productInfo);

        Response response = app.client().path(TEST_TYPE_URL + testTypeId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(TEST_TYPE_URL)
                .queryParam("q", "id=" + testTypeId)
                .request().get();

        PageWrapper<TestTypeInfo> page = response.readEntity(TEST_TYPE_INFO_PAGE);
        assertEquals("Updated Test Type", page.getItems().get(0).getName());
    }

    @Test
    public void searchWrongTeam() {
        Response response = app.client().path(TEST_TYPE_URL)
                .queryParam("q", "name=somethingIsWrong")
                .request().get();

        PageWrapper<TestTypeInfo> page = response.readEntity(TEST_TYPE_INFO_PAGE);
        assertEquals(page.getTotalCount(), 0);
    }

    @Test
    public void recreateDeletedTestType() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        TestTypeInfo json = new TestTypeInfo();
        json.setName("New Test Type");
        json.setProduct(productInfo);

        Response response = app.client().path(TEST_TYPE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        TestTypeInfo createdEntity = response.readEntity(TestTypeInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(TEST_TYPE_URL).queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse.getStatus());
        PageWrapper<TestTypeInfo> page = getResponse.readEntity(TEST_TYPE_INFO_PAGE);
        assertEquals("New Test Type", page.getItems().get(0).getName());

        Response deleteResponse = app.client().path(TEST_TYPE_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response deleteResponse2 = app.client().path(TEST_TYPE_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());

        Response response2 = app.client().path(TEST_TYPE_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

        createdEntity = response2.readEntity(TestTypeInfo.class);
        createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse2 = app.client().path(TEST_TYPE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse2.getStatus());
    }

}
