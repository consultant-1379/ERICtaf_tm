package com.ericsson.cifwk.tm.infrastructure.toggler;

import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.user.UserProvider;
import org.togglz.shiro.ShiroUserProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TogglzConfiguration implements TogglzConfig {

    // TODO to be extracted once user roles concept in place
    private static final String ADMIN_USER_ROLE = "ADMIN";

    @Inject
    private HibernateStateRepository hibernateStateRepository;

    @Override
    public Class<? extends Feature> getFeatureClass() {
        return TmsFeatures.class;
    }

    @Override
    public StateRepository getStateRepository() {
        return hibernateStateRepository;
    }

    @Override
    public UserProvider getUserProvider() {
        return new ShiroUserProvider(ADMIN_USER_ROLE);
    }

}
