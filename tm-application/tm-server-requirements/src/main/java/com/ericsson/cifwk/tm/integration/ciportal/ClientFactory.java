package com.ericsson.cifwk.tm.integration.ciportal;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static com.ericsson.cifwk.tm.integration.utils.SecurityUtils.getSslContext;
import static com.ericsson.cifwk.tm.integration.utils.SecurityUtils.getTrustAllHostNameVerifier;

public final class ClientFactory {

    private ClientFactory() {
    }

    public static Client newClient() {
        Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .sslContext(getSslContext())
                .hostnameVerifier(getTrustAllHostNameVerifier())
                .build();

        client.property(ClientProperties.FOLLOW_REDIRECTS, true);

        return client;
    }

}
