package com.ericsson.cifwk.tm.application;

import com.ericsson.cifwk.tm.domain.TestFixture;
import com.ericsson.cifwk.tm.infrastructure.Bootstrap;
import com.ericsson.cifwk.tm.infrastructure.GuiceTestRunner;
import com.ericsson.cifwk.tm.infrastructure.IntegrationStubModule;
import com.ericsson.cifwk.tm.infrastructure.PersistenceHelper;
import com.ericsson.cifwk.tm.infrastructure.PersistenceModule;
import com.ericsson.cifwk.tm.infrastructure.SecurityStub;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Intended to bootstrap Guice container and test service logic.
 */
@RunWith(GuiceTestRunner.class)
@GuiceTestRunner.GuiceModules({
        IntegrationStubModule.class,
        PersistenceModule.class
})
public abstract class BaseServiceLayerTest {

    @Inject
    protected PersistenceHelper persistence;

    private TestFixture fixture;

    @Inject
    SecurityStub security;
    @Inject
    Injector injector;


    @Before
    public final void init() {
        Bootstrap.setInjector(injector);
        persistence.cleanupTables();
        fixture = new TestFixture(persistence);
        security.begin("taf");
    }

    @After
    public final void after() {
        security.end();
        Bootstrap.setInjector(null);
    }


    protected PersistenceHelper persistence() {
        return persistence;
    }

    protected TestFixture fixture() {
        return fixture;
    }

}
