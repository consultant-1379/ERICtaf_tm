/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.buildFeature;

public class ProductsControllerTest extends BaseControllerLevelTest {

    public static final String PRODUCT_URL = "/tm-server/api/products/";

    GenericType<List<ProductInfo>> productType = new GenericType<List<ProductInfo>>() {
    };

    GenericType<List<DropInfo>> dropType = new GenericType<List<DropInfo>>() {
    };

    GenericType<List<FeatureInfo>> featureType = new GenericType<List<FeatureInfo>>() {
    };

    GenericType<List<TechnicalComponentInfo>> componentType = new GenericType<List<TechnicalComponentInfo>>() {
    };

    private Product enm;
    private Product product;
    private ProductFeature fm;
    private ProductFeature cm;

    private String itemId;

    @Before
    public void setUp() {
        enm = new Product("ENM");
        enm.setName("ENM");

        Drop drop1 = new Drop(enm, "16.3");
        Drop drop2 = new Drop(enm, "16.4");

        fm = buildFeature("FM", enm, Sets.newHashSet()).build();
        cm = buildFeature("CM", enm, Sets.newHashSet()).build();

        TechnicalComponent component1 = new TechnicalComponent("ENM_FM_COMPONENT_1", fm);
        TechnicalComponent component2 = new TechnicalComponent("ENM_FM_COMPONENT_2", fm);
        TechnicalComponent component3 = new TechnicalComponent("ENM_CM_COMPONENT_1", cm);
        TechnicalComponent component4 = new TechnicalComponent("ENM_CM_COMPONENT_2", cm);

        fm.setComponents(Sets.newHashSet(component1, component2));
        cm.setComponents(Sets.newHashSet(component3, component4));

        app.persistence().persistInTransaction(enm, drop1, drop2, fm, cm);

        product = new Product("Feature Product");
        product.setName("test");
        app.persistence().persistInTransaction(product);
        itemId = "" + product.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void shouldGetProductList() {
        Response response = app.client()
                .path(PRODUCT_URL)
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        List<ProductInfo> products = response.readEntity(productType);
        response.close();

        assertEquals(2, products.size());
        assertThat(products.get(0).getName(), equalTo("ENM"));
    }

    @Test
    public void shouldGetDropsByProduct() {
        Response response = app.client()
                .path(PRODUCT_URL + enm.getId() + "/drops")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        List<DropInfo> drops = response.readEntity(dropType);

        assertThat(drops.size(), equalTo(2));

        List<String> dropNames = drops.stream()
                .map(d -> d.getName())
                .collect(Collectors.toList());

        drops.forEach(d -> assertThat(d.getProduct().getExternalId(), equalTo("ENM")));
        assertThat(dropNames, hasItems("16.3", "16.4"));
    }

    @Test
    public void shouldGetFeaturesByProduct() {
        Response response = app.client()
                .path(PRODUCT_URL + enm.getId() + "/features")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        List<FeatureInfo> features = response.readEntity(featureType);

        assertThat(features.size(), equalTo(2));

        List<String> featureNames = features.stream()
                .map(f -> f.getName())
                .collect(Collectors.toList());

        features.forEach(f -> assertThat(f.getProduct().getName(), equalTo("ENM")));
        assertThat(featureNames, hasItems("FM", "CM"));
    }

    @Test
    public void shouldGetComponentsByProductAndDrop() {
        Response response = app.client()
                .path(PRODUCT_URL + "features/components")
                .queryParam("featureId", fm.getId())
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        List<TechnicalComponentInfo> components = response.readEntity(componentType);

        assertThat(components.size(), equalTo(2));

        List<String> fmComponentNames = components.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(fmComponentNames, hasItems("ENM_FM_COMPONENT_1", "ENM_FM_COMPONENT_2"));

        response = app.client()
                .path(PRODUCT_URL + "features/components")
                .queryParam("featureId", cm.getId())
                .request()
                .get();

        assertStatus(response, Response.Status.OK);
        components = response.readEntity(componentType);

        assertThat(components.size(), equalTo(2));

        List<String> cmComponentNames = components.stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        assertThat(cmComponentNames, hasItems("ENM_CM_COMPONENT_1", "ENM_CM_COMPONENT_2"));
    }

    @Test
    public void delete() {
        Response response = app.client().path(PRODUCT_URL + itemId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(PRODUCT_URL + itemId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {
        // request load
        ProductInfo json = new ProductInfo();
        json.setName("New Product");
        json.setExternalId("New Product");

        // creating entity
        Response response = app.client().path(PRODUCT_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ProductInfo createdEntity = response.readEntity(ProductInfo.class);
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(PRODUCT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        List<ProductInfo> page = response.readEntity(productType);
        assertEquals("New Product", page.get(2).getName());
    }

    @Test
    public void updateExisting() {
        // updating entity
        ProductInfo json = new ProductInfo();
        json.setName("Updated Product");
        json.setExternalId("New Product");
        json.setId(Long.parseLong(itemId));

        Response response = app.client().path(PRODUCT_URL + itemId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(PRODUCT_URL)
                .queryParam("q", "id=" + itemId)
                .request().get();

        List<ProductInfo> page = response.readEntity(productType);
        assertEquals("Updated Product", page.get(1).getName());
    }

    @Test
    public void recreateDeletedProduct() {
        ProductInfo json = new ProductInfo();
        json.setName("New Product");
        json.setExternalId("New Product");

        Response response = app.client().path(PRODUCT_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        ProductInfo createdEntity = response.readEntity(ProductInfo.class);

        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);
        Response getResponse = app.client().path(PRODUCT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, getResponse.getStatus());
        List<ProductInfo> page = getResponse.readEntity(productType);
        assertEquals("New Product", page.get(2).getName());

        Response deleteResponse1 = app.client().path(PRODUCT_URL + createdEntityId).request().delete();
        deleteResponse1.close();
        assertEquals(204, deleteResponse1.getStatus());

        Response deleteResponse2 = app.client().path(PRODUCT_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());

        Response response2 = app.client().path(PRODUCT_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

        createdEntity = response2.readEntity(ProductInfo.class);
        createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response GetResponse2 = app.client().path(PRODUCT_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, GetResponse2.getStatus());
        page = GetResponse2.readEntity(productType);
        assertEquals("New Product", page.get(2).getName());
    }
}
