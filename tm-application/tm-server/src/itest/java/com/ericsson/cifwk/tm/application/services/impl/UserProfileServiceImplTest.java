package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class UserProfileServiceImplTest extends BaseServiceLayerTest {

    @Inject
    private UserProfileService userProfileService;

    @Before
    public void setUp() {
        persistence.cleanupTables();
    }

    @Test
    public void testFindOrCreate() {
        User user = fixture().persistUser("simpleUser");

        UserProfile profile = userProfileService.findOrCreate(user);

        assertThat(profile, notNullValue());
        assertThat(profile.getUser().getId(), equalTo(user.getId()));
    }

    @Test
    public void testUserProfile() {
        Project project = new Project();
        project.setId(1L);
        project.setName("name");
        Product product = fixture().persistProduct("name", project);
        User user = fixture().persistUser("testCascadeUser");
        UserProfile userProfile = new UserProfile();
        userProfile.setProduct(product);
        userProfile.setUser(user);

        persistence.persistInTransaction(userProfile);

        UserProfile result = userProfileService.findOrCreate(user);

        assertThat(result.getProduct(), equalTo(product));
        assertThat(result.getUser(), equalTo(user));
    }

}
