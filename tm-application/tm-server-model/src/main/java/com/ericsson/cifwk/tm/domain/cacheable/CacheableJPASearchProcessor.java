package com.ericsson.cifwk.tm.domain.cacheable;
/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.MetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

public class CacheableJPASearchProcessor extends JPASearchProcessor {
    public CacheableJPASearchProcessor(MetadataUtil mdu) {
        super(mdu);
    }


    @Override
    public Object searchUnique(EntityManager entityManager, Class<?> entityClass, ISearch search)
            throws NonUniqueResultException {
        return super.searchUnique(replaceEntityManagerIfRequired(entityManager, search), entityClass, search);
    }

    @Override
    public List search(EntityManager entityManager, Class<?> searchClass, ISearch search) {
        return super.search(replaceEntityManagerIfRequired(entityManager, search), searchClass, search);
    }

    private EntityManager replaceEntityManagerIfRequired(EntityManager entityManager, ISearch search) {
        return search instanceof CacheableSearch ? new CacheableEntityManager(entityManager) : entityManager;
    }

    public class CacheableEntityManager extends EntityManagerWrapper {
        public CacheableEntityManager(EntityManager delegate) {
            super(delegate);
        }

        @Override
        public Query createQuery(String s) {
            Query query = super.createQuery(s);
            query.setHint("org.hibernate.cacheable", true);
            return query;
        }
    }
}
