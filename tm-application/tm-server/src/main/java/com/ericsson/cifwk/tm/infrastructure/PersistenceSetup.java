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

import com.google.common.collect.Maps;
import com.google.inject.Inject;


import com.netflix.governator.annotations.AutoBindSingleton;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Map;

@AutoBindSingleton
public final class PersistenceSetup {
    private final Logger logger = LoggerFactory.getLogger(PersistenceSetup.class);

    private static final String CONN_URL_PROP = "javax.persistence.jdbc.url";
    private static final String CONN_USER_PROP = "javax.persistence.jdbc.user";
    private static final String CONN_PASSWORD_PROP = "javax.persistence.jdbc.password";
    private static final String FLYWAY = "_flyway";
    public static final String DEFAULT_MIGRATION_LOCATION = "db/migration";

    @Inject
    public void setup(Flyway flyway,
                      Environment environment) {
        System.out.println("[INFO] Environment env: " + environment.toString());
        String persistenceUnit = environment.getPersistenceUnit() + FLYWAY;
        System.out.println("[INFO] persistence unit: " + persistenceUnit);
        String environmentMigrationPath = environment.getMigrationPath();
        System.out.println("[INFO] environmentMigrationPath: " + environmentMigrationPath);
        Map<String, ?> properties = getPropertiesFromFactory(persistenceUnit);
        String url = (String) properties.get(CONN_URL_PROP);
        String user = (String) properties.get(CONN_USER_PROP);
        String password = (String) properties.get(CONN_PASSWORD_PROP);
        System.out.println("[INFO] url: " + url + "user: " + user + "password: " + password);
        setup(flyway, url, user, password, environmentMigrationPath);
    }

    private Map<String, ?> getPropertiesFromFactory(String persistenceUnit) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistenceUnit);
        try {
            if (factory instanceof EntityManagerFactoryImpl) {
                // otherwise passwords are masked (in EntityManagerFactoryImpl constructor)
                return Maps.fromProperties(((EntityManagerFactoryImpl) factory).getSessionFactory().getProperties());
            } else {
                return factory.getProperties();
            }
        } finally {
            factory.close();
        }
    }

    void setup(Flyway flyway,
               String url,
               String user,
               String password,
               String environmentMigrationPath) {
        flyway.setLocations(DEFAULT_MIGRATION_LOCATION, "db/" + environmentMigrationPath);
        flyway.setDataSource(url, user, password);
        flyway.setValidateOnMigrate(false);
        logger.info("Migration locations: {}", Arrays.toString(flyway.getLocations()));
        logger.info("Data source: {}", url);
        flyway.migrate();
    }

}
