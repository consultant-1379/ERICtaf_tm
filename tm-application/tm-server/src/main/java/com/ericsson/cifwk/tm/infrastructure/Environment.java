package com.ericsson.cifwk.tm.infrastructure;

import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.Properties;

@Singleton
public class Environment {

    private static final String DEFAULT_ENV = "dev";

    private final String env;
    private final String persistence;
    private final String migrationPath;

    @Inject
    public Environment() {
        env = System.getProperty("env", DEFAULT_ENV);
        persistence = System.getProperty("persistence", env);
        migrationPath = System.getProperty("migrationPath", env);
    }

    public String getModuleType() {
        return env;
    }

    public String getPersistenceUnit() {
        return persistence;
    }

    public String getMigrationPath() {
        return migrationPath;
    }

    public Properties toProperties() {
        Properties properties = new Properties();
        properties.put("env", env);
        properties.put("persistence", persistence);
        properties.put("migrationPath", migrationPath);
        return properties;
    }

}
