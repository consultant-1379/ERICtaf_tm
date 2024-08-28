package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by ezhigci on 15/09/2017.
 */
public class DropControllerTest extends BaseControllerLevelTest {

    public static final String DROP_URL = "/tm-server/api/drops/";

    private Drop drop;
    private Product product;

    private String dropId;

    private static final GenericType<PageWrapper<DropInfo>> DROP_INFO_PAGE = new GenericType<PageWrapper<DropInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product();
        product.setName("Drop Product");
        product.setExternalId("Drop Product");
        app.persistence().persistInTransaction(product);

        drop = new Drop();
        drop.setName("Drop Test");
        drop.setProduct(product);
        app.persistence().persistInTransaction(drop);
        dropId = "" + drop.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void create() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());
        productInfo.setExternalId(product.getExternalId());

        DropInfo json = new DropInfo();
        json.setName("New Drop");
        json.setProduct(productInfo);
        json.setProductName(productInfo.getName());

        Response response = app.client().path(DROP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        DropInfo createdEntity = response.readEntity(DropInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        response = app.client().path(DROP_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void createWithNullNameFieldExpectError() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());
        productInfo.setExternalId(product.getExternalId());

        DropInfo json = new DropInfo();
        json.setProduct(productInfo);
        json.setProductName(productInfo.getName());

        Response response = app.client().path(DROP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void createWithNulProductFieldExpectError() {
        DropInfo json = new DropInfo();
        json.setName("New Drop");

        Response response = app.client().path(DROP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());
        productInfo.setName(product.getName());
        productInfo.setExternalId(product.getExternalId());

        DropInfo json = new DropInfo();
        json.setName("Updated drop");
        json.setId(Long.parseLong(dropId));

        json.setProduct(productInfo);

        Response response = app.client().path(DROP_URL + dropId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        response = app.client().path(DROP_URL)
                .queryParam("q", "id=" + dropId)
                .request().get();

        PageWrapper<DropInfo> page = response.readEntity(DROP_INFO_PAGE);
        assertEquals("Updated drop", page.getItems().get(0).getName());

    }

    @Test
    public void delete() {
        Response response = app.client().path(DROP_URL + dropId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(DROP_URL + dropId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }


    @Test
    public void recreateDeletedScope() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        DropInfo json = new DropInfo();
        json.setName("New Scope");
        json.setProduct(productInfo);

        Response response = app.client().path(DROP_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        DropInfo createdEntity = response.readEntity(DropInfo.class);

        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(DROP_URL + createdEntityId).request().get();
        getResponse.close();
        assertEquals(200, getResponse.getStatus());

        Response deleteResponse = app.client().path(DROP_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response deleteResponse2 = app.client().path(DROP_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());

        DropInfo drop = new DropInfo();
        drop.setId(createdEntity.getId());
        drop.setName(createdEntity.getName());
        drop.setProduct(productInfo);

        Response response2 = app.client().path(DROP_URL).request().post(Entity.entity(drop, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());
    }
}
