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
import com.ericsson.cifwk.tm.domain.model.users.UserSession;
import com.ericsson.cifwk.tm.domain.model.users.UserSessionRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserSessionRepositoryImpl extends BaseRepositoryImpl<UserSession, Long> implements UserSessionRepository {

    @Override
    public UserSession findBySessionId(String sessionId) {
        Search search = new Search(UserSession.class);
        search.addFilterEqual("sessionId", sessionId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserSession findActiveSessionId(String sessionId) {
        Search search = new Search(UserSession.class);
        search.addFilterEqual("sessionId", sessionId);
        search.addFilterNull("deletedAt");
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Object[]> getMonthlyUsers() {
        Query query = em().createNativeQuery("SELECT DATE_FORMAT(created_at,'%Y-%m') as name, " +
                "CONCAT(MONTHNAME(created_at), ' ', YEAR(created_at)) as label, " +
                "count(id) as value " +
                "FROM USER_SESSIONS " +
                "WHERE created_at > DATE_SUB(now(), INTERVAL 7 MONTH) " +
                "GROUP BY YEAR(created_at), MONTH(created_at) " +
                "ORDER BY name ASC");

        return (List<Object[]>) query.getResultList();
    }

}
