package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.googlecode.genericdao.search.Search;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class UserRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private UserRepository userRepository;

    @Test
    public void testDAO() {
        String userExternalId = "testDAOUser";
        User userEntity = new User(userExternalId);

        persistence.persistInTransaction(userEntity);

        Search search = new Search();
        search.addFilterEqual("externalId", userExternalId);
        User persistedUser = userRepository.searchUnique(search);

        assertEquals(userExternalId, persistedUser.getExternalId());
    }

}
