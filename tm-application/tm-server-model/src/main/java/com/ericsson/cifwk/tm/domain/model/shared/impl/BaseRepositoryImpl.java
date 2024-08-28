/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.shared.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.jpa.internal.EntityManagerImpl;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseRepositoryImpl<T, ID extends Serializable>
        extends GenericDAOImpl<T, ID>
        implements BaseRepository<T, ID> {

    public static final String DELETED_ENTITY_FILTER = "deletedEntityFilter";

    private boolean filter = true;

    private Provider<EntityManager> entityManagerProvider;

    @Override
    protected EntityManager em() {
        EntityManager entityManager = entityManagerProvider.get();
        Optional<Session> session = getSession(entityManager);

        if (this.filter && session.isPresent() && null == session.get().getEnabledFilter(DELETED_ENTITY_FILTER)) {
            session.get().enableFilter(DELETED_ENTITY_FILTER);
        }
        return entityManager;
    }

    @Override
    public Optional<Session> getSession(EntityManager entityManager) {
        if (entityManager instanceof EntityManagerImpl) {
            EntityManagerImpl hibernateEntityManager = (EntityManagerImpl) entityManager;
            Optional<Session> session = Optional.ofNullable(hibernateEntityManager.getSession());
            return session;
        } else {
            return Optional.ofNullable(null);
        }
    }

    @Override
    public void lock(T object) {
        EntityManager entityManager = entityManagerProvider.get();
        Optional<Session> session = getSession(entityManager);
        if (session.isPresent()) {
            LockOptions lockOptions = new LockOptions(LockMode.PESSIMISTIC_WRITE);
            Session.LockRequest lockRequest = session.get().buildLockRequest(lockOptions);

            lockRequest.lock(object);
        }
    }

    @Inject
    @Override
    public final void setSearchProcessor(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    @Override
    public void setEntityManager(EntityManager em) {
        throw new IllegalStateException(
                "Don't inject EntityManager, please use setEntityManager(Provider<EntityManager>... instead!");
    }


    @Inject
    public final void setEntityManager(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    public Paginated<T> searchPaginated(Search search, int page, int perPage) {
        Preconditions.checkArgument(page > 0);
        Preconditions.checkArgument(perPage > 0);
        Search searchPage = search
                .setPage(page - 1)
                .setMaxResults(perPage);
        searchPage.setDistinct(true);
        SearchResult<T> searchResult = this.searchAndCount(searchPage);
        List<T> result = searchResult.getResult();
        int totalCount = searchResult.getTotalCount();
        return new Paginated<>(page, perPage, result, totalCount);
    }

    @Override
    public void disableFilter() {
        EntityManager entityManager = entityManagerProvider.get();
        Optional<Session> session = getSession(entityManager);
        if (session.isPresent()) {
            session.get().disableFilter(DELETED_ENTITY_FILTER);
            this.filter = false;
        }
    }

    @Override
    public void enableFilter() {
        EntityManager entityManager = entityManagerProvider.get();
        Optional<Session> session = getSession(entityManager);
        if (session.isPresent()) {
            session.get().enableFilter(DELETED_ENTITY_FILTER);
            this.filter = true;
        }
    }

}
