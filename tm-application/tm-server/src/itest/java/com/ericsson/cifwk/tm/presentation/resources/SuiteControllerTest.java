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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.SuiteInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SuiteControllerTest extends BaseControllerLevelTest {

    public static final String SUITE_URL = "/tm-server/api/suite/";

    private TestSuite suite;
    private Product product;
    private ProductFeature productFeature;

    private String suiteId;

    private static final GenericType<PageWrapper<SuiteInfo>> SUITE_INFO_PAGE = new GenericType<PageWrapper<SuiteInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product("Suite Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        productFeature = new ProductFeature();
        productFeature.setName("suite Feature");
        productFeature.setProduct(product);
        app.persistence().persistInTransaction(productFeature);

        suite = new TestSuite();
        suite.setName("Test Suite");
        suite.setFeature(productFeature);
        app.persistence().persistInTransaction(suite);
        suiteId = "" + suite.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        Response response = app.client().path(SUITE_URL + suiteId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(SUITE_URL + suiteId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        SuiteInfo json = new SuiteInfo();
        json.setName("New Suite");
        json.setFeature(featureInfo);

        // creating entity
        Response response = app.client().path(SUITE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(SUITE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<SuiteInfo> page = response.readEntity(SUITE_INFO_PAGE);
        assertEquals("New Suite", page.getItems().get(0).getName());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        // updating entity
        SuiteInfo json = new SuiteInfo();
        json.setName("Updated Suite");
        json.setId(Long.parseLong(suiteId));

        json.setFeature(featureInfo);

        Response response = app.client().path(SUITE_URL + suiteId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(SUITE_URL)
                .queryParam("q", "id=" + suiteId)
                .request().get();

        PageWrapper<SuiteInfo> page = response.readEntity(SUITE_INFO_PAGE);
        assertEquals("Updated Suite", page.getItems().get(0).getName());
    }

    @Test
    public void recreateTestSuite() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        SuiteInfo json = new SuiteInfo();
        json.setName("New Suite");
        json.setFeature(featureInfo);

        Response response = app.client().path(SUITE_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        SuiteInfo createdEntity = response.readEntity(SuiteInfo.class);
        Long createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(SUITE_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse.getStatus());

        Response deleteResponse = app.client().path(SUITE_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response response2 = app.client().path(SUITE_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());
    }
}
