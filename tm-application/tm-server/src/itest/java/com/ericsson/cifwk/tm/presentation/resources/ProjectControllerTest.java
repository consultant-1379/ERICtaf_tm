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

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.PageWrapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectControllerTest extends BaseControllerLevelTest {

    public static final String PROJECT_URL = "/tm-server/api/projects";
    public static final String PRODUCT_URL = "/tm-server/api/products/";

    GenericType<List<ProductInfo>> productType = new GenericType<List<ProductInfo>>() {
    };

    @Before
    public void setUp() {
        Product product = new Product(Product.DEFAULT_EXTERNAL_ID);
        product.setName("");

        Product product2 = new Product("ENM");
        product2.setName("ENM");

        app.persistence().persistInTransaction(product, product2);

        UserProfile userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void shouldGetProjectList() {
        GenericType<PageWrapper<ProjectInfo>> projectsType = new GenericType<PageWrapper<ProjectInfo>>() {
        };

        Response response = app.client().path(PROJECT_URL).request().get();
        assertStatus(response, Response.Status.OK);

        PageWrapper<ProjectInfo> projects = response.readEntity(projectsType);
        assertEquals(0, projects.getItems().size());
    }

    @Test
    public void shouldFailToFindProject() {
        Response response = app.client().path(PROJECT_URL + "/XYZ").request().get();
        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void shouldFailValidationOnCreateProject() {
        ProjectInfo project = new ProjectInfo();
        Entity<ProjectInfo> entity = Entity.entity(project, MediaType.APPLICATION_JSON);

        Response response = app.client().path(PROJECT_URL).request().post(entity);

        assertStatus(response, Response.Status.BAD_REQUEST);
    }

    @Test
    public void shouldCreateProject() {
        Response response = createProject("TEST");

        assertStatus(response, Response.Status.CREATED);
        response.close();

        Response response2 = app.client().path(PROJECT_URL).request().get();
        response2.close();
        assertStatus(response2, Response.Status.OK);
    }

    @Test
    public void shouldCreateProjectWithProduct() {

        ProjectInfo project = new ProjectInfo();
        project.setExternalId("TEST");
        project.setName("Test project");

        Response productResponse = app.client().path(PRODUCT_URL).request().get();
        assertStatus(productResponse, Response.Status.OK);

        List<ProductInfo> products = productResponse.readEntity(productType);

        project.setProduct(products.get(1));

        Entity<ProjectInfo> entity = Entity.entity(project, MediaType.APPLICATION_JSON);
        Response response = app.client().path(PROJECT_URL).request().post(entity);
        response.close();
        assertStatus(response, Response.Status.CREATED);

    }

    @Test
    public void shouldCreateProjectAndDelete() {
        Response response = createProject("TEST");
        assertStatus(response, Response.Status.CREATED);
        ProjectInfo projectInfo = response.readEntity(ProjectInfo.class);
        response.close();

        Response response2 = app.client().path(PROJECT_URL + "/" + projectInfo.getId()).request().delete();
        response2.close();
        assertStatus(response2, Response.Status.NO_CONTENT);

    }

    @Test
    public void shouldFailToDeleteProject() {
        String badId = "200";
        Response response = app.client().path(PROJECT_URL + badId).request().delete();
        response.close();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void shouldFailToCreateNonUniqueProject() {
        Response response = createProject("TEST");
        response.close();
        assertStatus(response, Response.Status.CREATED);

        Response response2 = createProject("TEST");
        response2.close();
        assertStatus(response2, Response.Status.BAD_REQUEST);
    }

    private Response createProject(String id) {
        ProjectInfo project = new ProjectInfo();
        project.setExternalId(id);
        project.setName("Test project");
        Entity<ProjectInfo> entity = Entity.entity(project, MediaType.APPLICATION_JSON);

        return app.client().path(PROJECT_URL).request().post(entity);
    }

    @Test
    public void recreateDeletedProject() {
        Response response = createProject("TEST");
        assertStatus(response, Response.Status.CREATED);

        ProjectInfo projectInfo = response.readEntity(ProjectInfo.class);
        response.close();

        Response deleteResponse = app.client().path(PROJECT_URL + "/" + projectInfo.getId()).request().delete();
        deleteResponse.close();
        assertStatus(deleteResponse, Response.Status.NO_CONTENT);

        Response response2 = app.client().path(PROJECT_URL).request().post(Entity.entity(projectInfo, MediaType.APPLICATION_JSON));
        assertEquals(201, response2.getStatus());

        projectInfo = response2.readEntity(ProjectInfo.class);
        String projectInfoId = projectInfo.getId().toString();
        assertNotNull(projectInfoId);

        Response getResponse = app.client().path(PROJECT_URL)
                .queryParam("q", "id=" + projectInfoId)
                .request().get();

        assertEquals(200, getResponse.getStatus());

    }
}
