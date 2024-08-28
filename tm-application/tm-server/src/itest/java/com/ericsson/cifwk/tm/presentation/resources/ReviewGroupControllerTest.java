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

import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.BaseControllerLevelTest;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.ericsson.cifwk.tm.test.ResponseAsserts.assertStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;

public class ReviewGroupControllerTest extends BaseControllerLevelTest {

    private static final String REVIEW_GROUP_URL = "/tm-server/api/review-group/";

    private UserProfile userProfile;

    @Before
    public void setUp() {
        this.userProfile = new UserProfile();
        userProfile.setAdministrator(true);

        app.insertProfileForUser("taf", userProfile);
    }

    @Test
    public void createEditDeleteReviewGroup() {
        ReviewGroupInfo reviewGroup = getReviewGroup();

        Response response = app.client().path(REVIEW_GROUP_URL).request().post(Entity.entity(reviewGroup, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.CREATED);

        ReviewGroupInfo reviewGroupInfo = response.readEntity(ReviewGroupInfo.class);
        assertThat(reviewGroupInfo.getName(), equalTo(reviewGroup.getName()));
        assertThat(reviewGroupInfo.getUsers().size(), equalTo(1));


        reviewGroupInfo.setUsers(Sets.newHashSet());
        Response response2 = app.client().path(REVIEW_GROUP_URL + reviewGroupInfo.getId())
                .request()
                .put(Entity.entity(reviewGroupInfo, MediaType.APPLICATION_JSON));

        assertStatus(response2, Response.Status.OK);

        ReviewGroupInfo reviewGroupInfo2 = response2.readEntity(ReviewGroupInfo.class);

        assertThat(reviewGroupInfo2.getUsers().size(), equalTo(0));

        Response response3 = app.client().path(REVIEW_GROUP_URL + reviewGroupInfo.getId())
                .request()
                .delete();
        assertStatus(response3, Response.Status.NO_CONTENT);
    }

    private ReviewGroupInfo getReviewGroup() {
        ReviewGroupInfo reviewGroupInfo = new ReviewGroupInfo();

        reviewGroupInfo.setName("Review Group");
        User user = this.userProfile.getUser();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setName(user.getExternalName());
        reviewGroupInfo.setUsers(Sets.newHashSet(userInfo));
        return reviewGroupInfo;
    }

    @Test
    public void recreateReviewGroup() {
        ReviewGroupInfo reviewGroup = getReviewGroup();

        Response response = app.client().path(REVIEW_GROUP_URL).request().post(Entity.entity(reviewGroup, MediaType.APPLICATION_JSON));
        assertStatus(response, Response.Status.CREATED);

        ReviewGroupInfo createdEntity = response.readEntity(ReviewGroupInfo.class);
        assertThat(createdEntity.getName(), equalTo(reviewGroup.getName()));
        assertThat(createdEntity.getUsers().size(), equalTo(1));
        String createdEntityId = createdEntity.getId().toString();
        assertNotNull(createdEntityId);

        Response getResponse = app.client().path(REVIEW_GROUP_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertStatus(getResponse, Response.Status.OK);

        Response deleteResponse = app.client().path(REVIEW_GROUP_URL + createdEntity.getId()).request().delete();
        deleteResponse.close();
        assertStatus(deleteResponse, Response.Status.NO_CONTENT);


        Response response2 = app.client().path(REVIEW_GROUP_URL).request().post(Entity.entity(createdEntity, MediaType.APPLICATION_JSON));
        assertStatus(response2, Response.Status.CREATED);

        createdEntity = response2.readEntity(ReviewGroupInfo.class);
        createdEntityId = createdEntity.getId().toString();
        Response getResponse2 = app.client().path(REVIEW_GROUP_URL)
                .queryParam("q", "id=" + createdEntityId)
                .request().get();

        assertStatus(getResponse2, Response.Status.OK);
    }


}
