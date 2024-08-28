/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.domain.cacheable.CacheableJPASearchProcessor;
import com.ericsson.cifwk.tm.infrastructure.annotations.GuiceModule;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.googlecode.genericdao.search.hibernate.HibernateMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

@GuiceModule(priority = 5)
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        String persistenceUnit = new Environment().getPersistenceUnit();
        JpaPersistModule module = new JpaPersistModule(persistenceUnit);

        Properties properties = loadProperties();
        module.properties(properties);

        install(module);
        bind(PersistenceSetup.class).asEagerSingleton();
    }

    private Properties loadProperties() {
        URL resource = Resources.getResource("hibernate.properties");
        Properties properties = new Properties();
        try {
            File file = new File(resource.toURI());
            FileInputStream stream = new FileInputStream(file);
            properties.load(stream);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to load hibernate.properties");
        }
        return properties;
    }

    @Provides
    JPASearchProcessor provideJPASearchProcessor(EntityManagerFactory entityManagerFactory) {
        SessionFactory sessionFactory = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();
        HibernateMetadataUtil metadataUtil = HibernateMetadataUtil.getInstanceForSessionFactory(sessionFactory);
        return new CacheableJPASearchProcessor(metadataUtil);
    }

}
