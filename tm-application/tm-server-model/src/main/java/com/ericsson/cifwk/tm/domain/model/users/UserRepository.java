/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.users;

import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.users.impl.UserRepositoryImpl;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(UserRepositoryImpl.class)
public interface UserRepository extends BaseRepository<User, Long> {

    User findByExternalId(String externalId);

    User findByExternalIdOrEmail(String externalId);

    User findByExternalEmail(String externalId);

    List<User> findMatchingExternalIdEmailAndUserName(String userInput, int limit);

}
