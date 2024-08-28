/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.ValidationError;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class ScopeControllerTest extends BaseControllerLevelTest {

    public static final String SCOPES_URL = "/tm-server/api/scopes/";

    private Scope scope;
    private Product product;

    private String scopeId;

    @Before
    public void setUp() {
        scope = new Scope("Existing Scope");
        app.persistence().persistInTransaction(scope);
        scopeId = "" + scope.getId();

        product = new Product("Scope Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        // regular delete
        Response response = app.client().path(SCOPES_URL + scopeId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        // repeated delete
        response = app.client().path(SCOPES_URL + scopeId).request().delete();
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void create() throws IOException {

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        // request load
        GroupInfo json = new GroupInfo();
        json.setEnabled(true);
        json.setName("New Scope");
        json.setProduct(productInfo);

        // creating entity
        Response response = app.client().path(SCOPES_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        response.close();
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(SCOPES_URL + createdEntityId).request().get();
        response.close();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateExisting() {

        // updating entity
        GroupInfo json = new GroupInfo();
        json.setName("Updated Scope");
        json.setId(Long.parseLong(scopeId));
        ProductInfo productInfo = new ProductInfo(product.getId(), product.getExternalId(), product.getName());
        json.setProduct(productInfo);

        Response response = app.client().path(SCOPES_URL + scopeId).request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(SCOPES_URL + scopeId).request().get();
        ReferenceDataItem updatedEntity = response.readEntity(ReferenceDataItem.class);
        response.close();
        assertEquals("Updated Scope", updatedEntity.getTitle());
    }

    @Test
    public void updateNonExisting() {
        GroupInfo json = new GroupInfo();
        json.setName("Updated Scope");
        json.setId(404L);

        ProductInfo productInfo = new ProductInfo(product.getId(), product.getExternalId(), product.getName());
        json.setProduct(productInfo);
        Response response = app.client().path(SCOPES_URL + "404").request().put(Entity.entity(json, MediaType.APPLICATION_JSON));
        response.close();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void groupValidationErrors() {

        GroupInfo json = new GroupInfo();
        json.setName("Updated Scope");
        MediaType[] acceptMediaTypes = {MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.WILDCARD_TYPE};
        Response response = app.client().path(SCOPES_URL + scopeId).request().accept(acceptMediaTypes).put(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(400, response.getStatus());

        // checking validation error messages
        assertEquals(400, response.getStatus());
        ValidationError[] validationErrors = response.readEntity(ValidationError[].class);
        response.close();
        Arrays.sort(validationErrors);
        assertEquals(2, validationErrors.length);
        assertThat(validationErrors[0].getMessage(), equalTo("Field 'id' must not be null"));
        assertThat(validationErrors[1].getMessage(), equalTo("Field 'product' must not be null"));
    }

    @Test
    public void recreateDeletedScope() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        GroupInfo json = new GroupInfo();
        json.setEnabled(true);
        json.setName("New Scope");
        json.setProduct(productInfo);

        Response response = app.client().path(SCOPES_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);

        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(SCOPES_URL + createdEntityId).request().get();
        getResponse.close();
        assertEquals(200, getResponse.getStatus());

        Response deleteResponse = app.client().path(SCOPES_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response deleteResponse2 = app.client().path(SCOPES_URL + createdEntityId).request().delete();
        deleteResponse2.close();
        assertEquals(404, deleteResponse2.getStatus());

        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(Long.parseLong(createdEntity.getId()));
        groupInfo.setName(createdEntity.getTitle());
        groupInfo.setProduct(productInfo);
        groupInfo.setEnabled(true);

        Response response2 = app.client().path(SCOPES_URL).request().post(Entity.entity(groupInfo, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());
    }
}
