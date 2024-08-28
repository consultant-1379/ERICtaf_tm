package com.ericsson.cifwk.tm.test;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 *
 */
public final class ResponseAsserts {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseAsserts.class);

    private ResponseAsserts() {
    }

    public static void assertStatus(Response response, Response.Status status) {
        try {
            assertThat(response.getStatus(), equalTo(status.getStatusCode()));
        } catch (AssertionError e) {
            tryPrintBody(response);
            throw Throwables.propagate(e);
        }
    }

    private static void tryPrintBody(Response response) {
        try {
            String body = response.readEntity(String.class);
            LOGGER.error(body);
        } catch (Exception e) {
            // ignore
        }
    }

}
