/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure;

import com.ericsson.cifwk.tm.infrastructure.inject.servlet.ApplicationConfigurator;
import com.ericsson.cifwk.tm.presentation.dto.UserCredentials;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RestClient {

    private static final Logger LOGGER = Logger.getLogger(EmbeddedServer.class.getName());

    private WebTarget target;

    public RestClient(int httpPort) {
        ApplicationConfigurator configurator = new ApplicationConfigurator();
        Client client = configurator
                .registerSharedFeatures(ClientBuilder.newBuilder())
                .build();

        // logging HTTP requests/responses on demand
        if ("true".equals(System.getProperty("itest.http.log-entity"))) {
            client.register(new LoggingFilter(LOGGER, true));
        }
        client.register(new CookieFilter());

        target = client.target("http://localhost:" + httpPort + "/");
    }

    public WebTarget path(String path) {
        return target.path(path);
    }

    public Response login() {
        return loginRequest().post(Entity.entity(new UserCredentials("taf", "taf"), MediaType.APPLICATION_JSON));
    }

    public Response loginAsUser(String username, String password) {
        UserCredentials userCredentials = new UserCredentials(username, password);
        return loginRequest().post(Entity.entity(userCredentials, MediaType.APPLICATION_JSON));
    }

    public Response logout() {
        return loginRequest().delete();
    }

    private Invocation.Builder loginRequest() {
        return path("/tm-server/api/login").request();
    }

    @Provider
    private static class CookieFilter implements ClientRequestFilter, ClientResponseFilter{

        private Map<String, NewCookie> cookiesMap = new HashMap<String, NewCookie>();

        @Override
         public void filter(ClientRequestContext requestContext) throws IOException {
            // improvement: filter out expired cookies
            requestContext.getHeaders().put("Cookie", new ArrayList<Object>(cookiesMap.values()));
        }

        @Override
        public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
            if (responseContext.getCookies() != null) {
                cookiesMap.putAll(responseContext.getCookies());
            }
        }
    }
}
