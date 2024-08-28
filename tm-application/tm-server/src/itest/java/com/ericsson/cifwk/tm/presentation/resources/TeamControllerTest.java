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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TeamInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TeamControllerTest extends BaseControllerLevelTest {

    public static final String TEAM_URL = "/tm-server/api/team/";

    private TestTeam team;
    private Product product;
    private ProductFeature productFeature;

    private String teamId;

    private static final GenericType<PageWrapper<TeamInfo>> TEAM_INFO_PAGE = new GenericType<PageWrapper<TeamInfo>>() {
    };

    @Before
    public void setUp() {
        product = new Product("Team Product");
        product.setName("");
        app.persistence().persistInTransaction(product);

        productFeature = new ProductFeature();
        productFeature.setName("Team Feature");
        productFeature.setProduct(product);
        app.persistence().persistInTransaction(productFeature);

        team = new TestTeam();
        team.setName("Test Team");
        team.setFeature(productFeature);
        app.persistence().persistInTransaction(team);
        teamId = "" + team.getId();

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void delete() {

        Response response = app.client().path(TEAM_URL + teamId).request().delete();
        response.close();
        assertEquals(204, response.getStatus());

        response = app.client().path(TEAM_URL + teamId).request().delete();
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
        TeamInfo json = new TeamInfo();
        json.setName("New Team");
        json.setFeature(featureInfo);

        // creating entity
        Response response = app.client().path(TEAM_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        // checking that returned entities contain ids
        ReferenceDataItem createdEntity = response.readEntity(ReferenceDataItem.class);
        String createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        // checking that entity exists
        response = app.client().path(TEAM_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());
        PageWrapper<TeamInfo> page = response.readEntity(TEAM_INFO_PAGE);
        assertEquals("New Team", page.getItems().get(0).getName());
    }

    @Test
    public void updateExisting() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        // updating entity
        TeamInfo json = new TeamInfo();
        json.setName("Updated Team");
        json.setId(Long.parseLong(teamId));

        json.setFeature(featureInfo);

        Response response = app.client().path(TEAM_URL + teamId).request()
                .put(Entity.entity(json, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        response.close();

        // checking that entity updated
        response = app.client().path(TEAM_URL)
                .queryParam("q", "id=" + teamId)
                .request().get();

        PageWrapper<TeamInfo> page = response.readEntity(TEAM_INFO_PAGE);
        assertEquals("Updated Team", page.getItems().get(0).getName());
    }

    @Test
    public void searchWrongTeam() {
        Response response = app.client().path(TEAM_URL)
                .queryParam("q", "name=somethingIsWrong")
                .request().get();

        PageWrapper<TeamInfo> page = response.readEntity(TEAM_INFO_PAGE);
        assertEquals(page.getTotalCount(), 0);
    }

    @Test
    public void recreateDeletedTeam() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(product.getId());

        FeatureInfo featureInfo = new FeatureInfo();
        featureInfo.setProduct(productInfo);
        featureInfo.setId(productFeature.getId());

        TeamInfo json = new TeamInfo();
        json.setName("New Team");
        json.setFeature(featureInfo);

        Response response = app.client().path(TEAM_URL).request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
        assertEquals(201, response.getStatus());

        TeamInfo createdEntity = response.readEntity(TeamInfo.class);
        Long createdEntityId = createdEntity.getId();
        assertNotNull(createdEntityId);

        response = app.client().path(TEAM_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertEquals(200, response.getStatus());

        Response deleteResponse = app.client().path(TEAM_URL + createdEntityId).request().delete();
        deleteResponse.close();
        assertEquals(204, deleteResponse.getStatus());

        Response response2 = app.client().path(TEAM_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

    }
}
