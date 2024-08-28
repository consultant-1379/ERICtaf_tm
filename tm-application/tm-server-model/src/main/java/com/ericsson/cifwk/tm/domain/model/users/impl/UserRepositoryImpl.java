/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.users.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepository {

    public static final String EXTERNAL_ID = "externalId";
    public static final String EXTERNAL_EMAIL = "externalEmail";
    public static final String USERNAME = "userName";
    public static final String EXTERNAL_NAME = "externalName";

    @Override
    public User findByExternalId(String externalId) {
        Search search = new Search(User.class);
        search.addFilterEqual(EXTERNAL_ID, externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public User findByExternalEmail(String externalId) {
        Search search = new Search(User.class);
        search.addFilterEqual(EXTERNAL_EMAIL, externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> findMatchingExternalIdEmailAndUserName(String userInput, int limit) {
        Search search = new Search(User.class);

        search.addFilter(Filter.and(Filter.or(
                Filter.ilike(USERNAME, SearchHelpers.toLikePattern(userInput)),
                Filter.ilike(EXTERNAL_ID, SearchHelpers.toLikePattern(userInput)),
                Filter.ilike(EXTERNAL_EMAIL, SearchHelpers.toLikePattern(userInput))),
                Filter.isNotEmpty(EXTERNAL_NAME)));

        search.addSortAsc(USERNAME);
        search.setMaxResults(limit);
        try {
            return this.search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public User findByExternalIdOrEmail(String query) {
        Search search = new Search(User.class);
        search.addFilterOr(Filter.equal(EXTERNAL_ID, query), Filter.equal(EXTERNAL_EMAIL, query));
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

}
