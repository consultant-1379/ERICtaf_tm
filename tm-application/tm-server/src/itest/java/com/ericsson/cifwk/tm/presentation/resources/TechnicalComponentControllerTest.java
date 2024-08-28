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
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TechnicalComponentControllerTest extends BaseControllerLevelTest {

    public static final String TECHNICAL_COMPONENT_URL = "/tm-server/api/components/";

    private TechnicalComponent technicalComponent;
    private Product product;
    private ProductFeature productFeature;

    private String itemId;

    private static final GenericType<PageWrapper<TechnicalComponentInfo>> TECHNICAL_COMPONENT_INFO_PAGE =
            new GenericType<PageWrapper<TechnicalComponentInfo>>() {
            };

    @Before
    public void setUp() {
        product = new Product("Component Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        productFeature = new ProductFeature();
        productFeature.setName("Component Feature");
        productFeature.setProduct(product);
        app.persistence().persistInTransaction(productFeature);

        technicalComponent = new TechnicalComponent();
        technicalComponent.setName("Component Test");
        technicalComponent.setFeature(productFeature);
        app.persistence().persistInTransaction(technicalComponent);
        itemId = "" + technicalComponent.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        Response response = app.client().path(TECHNICAL_COMPONENT_URL + itemId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(TECHNICAL_COMPONENT_URL + itemId).request().delete();
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

        // request load
        TechnicalComponentInfo json = new TechnicalComponentInfo();
        json.setName("New Component");
        json.setFeature(featureInfo);

        // creating entity
        Response response = app.client().path(TECHNICAL_COMPONENT_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        TechnicalComponentInfo createdEntity = response.readEntity(TechnicalComponentInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(TECHNICAL_COMPONENT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<TechnicalComponentInfo> page = response.readEntity(TECHNICAL_COMPONENT_INFO_PAGE);
        assertEquals("New Component", page.getItems().get(0).getName());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        // updating entity
        TechnicalComponentInfo json = new TechnicalComponentInfo();
        json.setName("Updated Component");
        json.setId(Long.parseLong(itemId));

        json.setFeature(featureInfo);

        Response response = app.client().path(TECHNICAL_COMPONENT_URL + itemId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(TECHNICAL_COMPONENT_URL)
                .queryParam("q", "id=" + itemId)
                .request().get();

        PageWrapper<TechnicalComponentInfo> page = response.readEntity(TECHNICAL_COMPONENT_INFO_PAGE);
        assertEquals("Updated Component", page.getItems().get(0).getName());
    }

    @Test
    public void searchWrongTeam() {
        Response response = app.client().path(TECHNICAL_COMPONENT_URL)
                .queryParam("q", "name=somethingIsWrong")
                .request().get();

        PageWrapper<TechnicalComponentInfo> page = response.readEntity(TECHNICAL_COMPONENT_INFO_PAGE);
        assertEquals(page.getTotalCount(), 0);
    }

    @Test
    public void recreateDeletedComponent() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        TechnicalComponentInfo json = new TechnicalComponentInfo();
        json.setName("New Component");
        json.setFeature(featureInfo);

        Response response = app.client().path(TECHNICAL_COMPONENT_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        TechnicalComponentInfo createdEntity = response.readEntity(TechnicalComponentInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(TECHNICAL_COMPONENT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse.getStatus());
        PageWrapper<TechnicalComponentInfo> page = getResponse.readEntity(TECHNICAL_COMPONENT_INFO_PAGE);
        assertEquals("New Component", page.getItems().get(0).getName());

        Response deleteResponse = app.client().path(TECHNICAL_COMPONENT_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response deleteResponse2 = app.client().path(TECHNICAL_COMPONENT_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());

        Response response2 = app.client().path(TECHNICAL_COMPONENT_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

        createdEntity = response2.readEntity(TechnicalComponentInfo.class);
        createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse2 = app.client().path(TECHNICAL_COMPONENT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse2.getStatus());
    }

}
