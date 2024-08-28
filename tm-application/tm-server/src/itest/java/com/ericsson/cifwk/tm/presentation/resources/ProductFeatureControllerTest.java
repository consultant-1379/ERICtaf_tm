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

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
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

public class ProductFeatureControllerTest extends BaseControllerLevelTest {

    public static final String FEATURE_URL = "/tm-server/api/features/";

    private ProductFeature productFeature;
    private Product product;

    private String itemId;

    private static final GenericType<PageWrapper<FeatureInfo>> FEATURE_INFO_PAGE = new GenericType<PageWrapper<FeatureInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product("Feature Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        productFeature = new ProductFeature();
        productFeature.setName("Feature Test");
        productFeature.setProduct(product);
        app.persistence().persistInTransaction(productFeature);
        itemId = "" + productFeature.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {
        Response response = app.client().path(FEATURE_URL + itemId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(FEATURE_URL + itemId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // request load
        FeatureInfo json = new FeatureInfo();
        json.setName("New Feature");
        json.setProduct(productInfo);

        // creating entity
        Response response = app.client().path(FEATURE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        FeatureInfo createdEntity = response.readEntity(FeatureInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(FEATURE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<FeatureInfo> page = response.readEntity(FEATURE_INFO_PAGE);
        assertEquals("New Feature", page.getItems().get(0).getName());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // updating entity
        FeatureInfo json = new FeatureInfo();
        json.setName("Updated Feature");
        json.setId(Long.parseLong(itemId));

        json.setProduct(productInfo);

        Response response = app.client().path(FEATURE_URL + itemId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(FEATURE_URL)
                .queryParam("q", "id=" + itemId)
                .request().get();

        PageWrapper<FeatureInfo> page = response.readEntity(FEATURE_INFO_PAGE);
        assertEquals("Updated Feature", page.getItems().get(0).getName());
    }

    @Test
    public void searchWrongTeam() {
        Response response = app.client().path(FEATURE_URL)
                .queryParam("q", "name=somethingIsWrong")
                .request().get();

        PageWrapper<FeatureInfo> page = response.readEntity(FEATURE_INFO_PAGE);
        assertEquals(page.getTotalCount(), 0);
    }

    @Test
    public void recreateDeletedFeature() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo json = new FeatureInfo();
        json.setName("New Feature");
        json.setProduct(productInfo);

        Response response = app.client().path(FEATURE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        FeatureInfo createdEntity = response.readEntity(FeatureInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(FEATURE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse.getStatus());
        PageWrapper<FeatureInfo> page = getResponse.readEntity(FEATURE_INFO_PAGE);
        assertEquals("New Feature", page.getItems().get(0).getName());

        Response deleteResponse1 = app.client().path(FEATURE_URL + createdEntityId).request().delete();
        deleteResponse1.close();
        assertEquals(204, deleteResponse1.getStatus());

        Response deleteResponse2 = app.client().path(FEATURE_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());


        Response response2 = app.client().path(FEATURE_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

        createdEntity = response2.readEntity(FeatureInfo.class);
        createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse2 = app.client().path(FEATURE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse2.getStatus());
    }

}
