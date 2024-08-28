/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.domain.cacheable;

import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.impl.RequirementRepositoryImpl;
import com.ericsson.cifwk.tm.infrastructure.GuiceTestRunner;
import com.ericsson.cifwk.tm.infrastructure.PersistenceHelper;
import com.ericsson.cifwk.tm.infrastructure.PersistenceModule;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.SessionImpl;
import org.hibernate.stat.Statistics;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(GuiceTestRunner.class)
@GuiceTestRunner.GuiceModules({
        PersistenceModule.class
})
public class CacheTest {

    @Inject
    private PersistenceHelper persistence;

    @Inject
    private EntityManagerFactory entityManagerFactory;

    @Inject
    private JPASearchProcessor jpaSearchProcessor;

    @Test
    @Ignore
    public void testSecondLevelCache() {
        SessionFactoryImplementor sessionFactory = ((SessionImpl) persistence.em().getDelegate()).getSessionFactory();
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();
        sessionFactory.getCache().evictAllRegions();

        Session session = sessionFactory.openSession();
        session.get(Requirement.class, 1L);
        session.close();

        session = sessionFactory.openSession();
        session.get(Requirement.class, 1L);
        session.close();

        session = sessionFactory.openSession();
        session.get(Requirement.class, 1L);
        session.close();


        assertThat(statistics.getSecondLevelCachePutCount(), equalTo(1L));
        assertThat(statistics.getSecondLevelCacheHitCount(), equalTo(2L));
        assertThat(statistics.getSecondLevelCacheMissCount(), equalTo(1L));
    }

    @Test
    @Ignore
    public void testQueryCacheSearchUniq() {
        SessionFactoryImplementor sessionFactory = ((SessionImpl) persistence.em().getDelegate()).getSessionFactory();
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();
        sessionFactory.getCache().evictAllRegions();

        RequirementRepositoryImpl repository = createRequirementRepository();
        repository.findByExternalId("CIP-4209");

        repository = createRequirementRepository();
        repository.findByExternalId("CIP-4209");

        repository = createRequirementRepository();
        repository.findByExternalId("CIP-4209");


        assertThat(statistics.getSecondLevelCachePutCount(), equalTo(1L));
        assertThat(statistics.getSecondLevelCacheHitCount(), equalTo(2L));
        assertThat(statistics.getSecondLevelCacheMissCount(), equalTo(0L));
    }

    @Test
    @Ignore
    public void testQueryCacheSearch() {
        SessionFactoryImplementor sessionFactory = ((SessionImpl) persistence.em().getDelegate()).getSessionFactory();
        Statistics statistics = sessionFactory.getStatistics();
        statistics.clear();
        sessionFactory.getCache().evictAllRegions();

        RequirementRepositoryImpl repository = createRequirementRepository();
        repository.findTopLevel(null);

        repository = createRequirementRepository();
        repository.findTopLevel(null);

        repository = createRequirementRepository();
        repository.findTopLevel(null);


        assertThat(statistics.getEntityFetchCount(), equalTo(6L));
        assertThat(statistics.getSecondLevelCachePutCount(), equalTo(3L));
        assertThat(statistics.getSecondLevelCacheHitCount(), equalTo(6L));
        assertThat(statistics.getSecondLevelCacheMissCount(), equalTo(0L));
    }

    private RequirementRepositoryImpl createRequirementRepository() {
        RequirementRepositoryImpl repository = new RequirementRepositoryImpl();
        repository.setEntityManager(entityManagerFactory.createEntityManager());
        repository.setSearchProcessor(jpaSearchProcessor);
        return repository;
    }
}
