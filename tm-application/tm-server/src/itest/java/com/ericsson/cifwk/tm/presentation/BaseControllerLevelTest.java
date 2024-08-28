package com.ericsson.cifwk.tm.presentation;

import com.ericsson.cifwk.tm.domain.TestFixture;
import com.ericsson.cifwk.tm.infrastructure.ApplicationResource;
import com.ericsson.cifwk.tm.test.Timing;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.junit.Before;
import org.junit.ClassRule;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Intended to start embedded container and test via HTTP.
 */
public abstract class BaseControllerLevelTest {

    @ClassRule
    public static ApplicationResource app = new ApplicationResource();

    private TestFixture fixture;

    @Before
    public final void startEmbeddedApp() {
        app.cleanUp();
        app.client().login();
        fixture = new TestFixture(app.persistence());
    }

    protected TestFixture fixture() {
        return fixture;
    }

    protected InboundEvent readEventInput(final EventInput eventInput) {
        try {
            InboundEvent inboundEvent = Timing.timeout(new Callable<InboundEvent>() {
                @Override
                public InboundEvent call() throws Exception {
                    return eventInput.read();
                }
            }, 1, TimeUnit.SECONDS);
            Preconditions.checkNotNull(inboundEvent);
            return inboundEvent;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
