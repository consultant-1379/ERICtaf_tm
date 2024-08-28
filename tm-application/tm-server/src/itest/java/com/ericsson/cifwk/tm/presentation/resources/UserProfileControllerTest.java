package com.ericsson.cifwk.tm.presentation.resources;

import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.infrastructure.ApplicationResource;
import com.ericsson.cifwk.tm.infrastructure.PersistenceHelper;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
public class UserProfileControllerTest extends BaseControllerLevelTest {

    @Before
    public void setUp() {
        app.insertFixtureData();
    }

    @Test
    public void shouldFetchCurrentUserWithoutProfile() {
        Response response = app.client().path("/tm-server/api/users/me").request().get();
        assertStatus(response, Response.Status.OK);

        UserProfileInfo userProfile = response.readEntity(UserProfileInfo.class);
        response.close();
        assertThat(userProfile.getUserId(), equalTo("taf"));
    }

    @Test
    public void shouldFetchCurrentUserWithProfile() {
        UserProfile userProfile = new UserProfile();
        app.insertProfileForUser(ApplicationResource.DEFAULT_USER_ID, userProfile);

        Response response = app.client().path("/tm-server/api/users/me").request().get();
        assertStatus(response, Response.Status.OK);

        UserProfileInfo responseUserProfile = response.readEntity(UserProfileInfo.class);
        response.close();
        assertThat(responseUserProfile.getUserId(), equalTo("taf"));
    }

    @Test
    public void shouldFetchCurrentUserWithProfileAndProject() {
        UserProfile userProfile = new UserProfile();
        app.insertProfileForUser(ApplicationResource.DEFAULT_USER_ID, userProfile);

        Product product = fixture().persistProduct("AAA");
        product.setName("Test Project");
        app.persistence().persistInTransaction(product);
        app.addProjectForProfile(userProfile, product);

        Response response = app.client().path("/tm-server/api/users/me").request().get();
        assertStatus(response, Response.Status.OK);

        UserProfileInfo responseUserProfile = response.readEntity(UserProfileInfo.class);
        response.close();
        assertThat(responseUserProfile.getUserId(), equalTo("taf"));
        assertThat(responseUserProfile.getProduct().getId(), equalTo(product.getId()));
    }

    @Test
    public void shouldFetchUserById() {
        PersistenceHelper persistence = app.persistence();
        User user = new User("test");
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        persistence.persistInTransaction(user, userProfile);

        Response response = app.client().path("/tm-server/api/users/test").request().get();
        response.close();
        assertStatus(response, Response.Status.OK);
    }

    @Test
    public void shouldFetchUserWithoutProfile() {
        PersistenceHelper persistence = app.persistence();
        User user = new User("test");
        persistence.persistInTransaction(user);

        Response response = app.client().path("/tm-server/api/users/test").request().get();
        assertStatus(response, Response.Status.OK);
    }

    @Test
    public void shouldFailToFindNonExistentUser() {
        Response response = app.client().path("/tm-server/api/users/xyz").request().get();
        assertStatus(response, Response.Status.NOT_FOUND);
    }

    @Test
    public void shouldUpdateUserProfile() {
        Project project = fixture().persistProject("AAA");
        project.setName("Test Project");
        app.persistence().persistInTransaction(project);

        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(project.getProduct().getId());
        productInfo.setExternalId(project.getProduct().getExternalId());

        UserProfileInfo userProfile = new UserProfileInfo();
        userProfile.setUserId("taf");
        userProfile.setProduct(productInfo);
        Entity<UserProfileInfo> entity = Entity.entity(userProfile, MediaType.APPLICATION_JSON);

        Response response = app.client().path("/tm-server/api/users/taf").request().put(entity);
        assertStatus(response, Response.Status.OK);

        Response response2 = app.client().path("/tm-server/api/users/taf").request().get();
        assertStatus(response2, Response.Status.OK);

        UserProfileInfo result = response2.readEntity(UserProfileInfo.class);
        assertThat(result.getUserId(), equalTo("taf"));
        assertThat(result.getProduct().getExternalId(), equalTo(project.getProduct().getExternalId()));
    }

    @Test
    public void shouldSaveSearchesAnDelete() {
        SavedSearchInfo savedSearchInfo = new SavedSearchInfo();
        savedSearchInfo.setName("Test");
        savedSearchInfo.setQuery("productName=test");

        UserProfileInfo userProfile = new UserProfileInfo();
        userProfile.setUserId("taf");
        userProfile.getSavedSearch().add(savedSearchInfo);
        Entity<UserProfileInfo> entity = Entity.entity(userProfile, MediaType.APPLICATION_JSON);

        Response response = app.client().path("/tm-server/api/users/taf").request().put(entity);
        assertStatus(response, Response.Status.OK);
        response.close();

        Response response2 = app.client().path("/tm-server/api/users/taf").request().get();
        assertStatus(response2, Response.Status.OK);

        UserProfileInfo result = response2.readEntity(UserProfileInfo.class);
        Set<SavedSearchInfo> savedSearch = result.getSavedSearch();
        assertThat(savedSearch.size(), equalTo(1));

        Optional<SavedSearchInfo> first = savedSearch.stream().findFirst();
        SavedSearchInfo searchInfo = first.get();
        Response response3 = app.client().path("/tm-server/api/users/saved-search/taf/" + searchInfo.getId()).request().delete();
        assertStatus(response3, Response.Status.NO_CONTENT);
        response3.close();

        Response response4 = app.client().path("/tm-server/api/users/taf").request().get();
        assertStatus(response4, Response.Status.OK);

        UserProfileInfo newResult = response4.readEntity(UserProfileInfo.class);
        assertThat(newResult.getSavedSearch().size(), equalTo(0));

    }

    @Test
    public void shouldUpdateUserProfileWithEmptyProject() {
        UserProfileInfo userProfile = new UserProfileInfo();
        userProfile.setUserId("taf");
        userProfile.setProduct(null);
        Entity<UserProfileInfo> entity = Entity.entity(userProfile, MediaType.APPLICATION_JSON);

        Response response = app.client().path("/tm-server/api/users/taf").request().put(entity);
        assertStatus(response, Response.Status.OK);

        Response response2 = app.client().path("/tm-server/api/users/taf").request().get();
        assertStatus(response2, Response.Status.OK);

        UserProfileInfo result = response2.readEntity(UserProfileInfo.class);
        assertThat(result.getUserId(), equalTo("taf"));
        assertThat(result.getProduct(), equalTo(null));
    }

    @Test
    public void shouldReturnListOfUsersContainingInputCritera() {
        fixture().persistUser("taf1", "email1", "userName1");
        fixture().persistUser("taf2", "email2", "userName2");
        fixture().persistUser("other", "taf3", "userName3");

        Response response = app.client()
                .path("tm-server/api/users/completion/")
                .queryParam("search", "taf")
                .queryParam("limit", "20")
                .request()
                .get();

        assertStatus(response, Response.Status.OK);

        List<LinkedHashMap> userInfoList = response.readEntity(List.class);
        assertThat(userInfoList.get(0).get("userName").toString(), equalTo("NameOftaf SurnameOftaf"));
        assertThat(userInfoList.get(1).get("userName").toString(), equalTo("userName1"));
        assertThat(userInfoList.get(2).get("userName").toString(), equalTo("userName2"));
        assertThat(userInfoList.get(3).get("userName").toString(), equalTo("userName3"));

        Response response2 = app.client()
                .path("tm-server/api/users/completion/")
                .queryParam("search", "ema")
                .queryParam("limit", "20")
                .request()
                .get();

        assertStatus(response2, Response.Status.OK);

        List<LinkedHashMap> userInfoList2 = response2.readEntity(List.class);
        assertThat(userInfoList2.get(0).get("userName").toString(), equalTo("userName1"));
        assertThat(userInfoList2.get(1).get("userName").toString(), equalTo("userName2"));
        assertThat(userInfoList2.size(), equalTo(2));

        Response response3 = app.client()
                .path("tm-server/api/users/completion/")
                .queryParam("search", "user")
                .queryParam("limit", "20")
                .request()
                .get();

        assertStatus(response3, Response.Status.OK);

        List<LinkedHashMap> userInfoList3 = response3.readEntity(List.class);
        assertThat(userInfoList3.get(0).get("userName").toString(), equalTo("userName1"));
        assertThat(userInfoList3.get(1).get("userName").toString(), equalTo("userName2"));
        assertThat(userInfoList3.get(2).get("userName").toString(), equalTo("userName3"));
        assertThat(userInfoList3.size(), equalTo(3));
    }
}
