/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.shared;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.search.Search;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

public interface BaseRepository<T, ID extends Serializable> extends GenericDAO<T, ID> {

    Paginated<T> searchPaginated(Search search, int page, int perPage);

    Optional<Session> getSession(EntityManager entityManager);

    void lock(T t);

    void disableFilter();

    void enableFilter();
}
