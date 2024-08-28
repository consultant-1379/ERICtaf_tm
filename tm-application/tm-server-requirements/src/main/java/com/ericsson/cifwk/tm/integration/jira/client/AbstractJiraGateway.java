package com.ericsson.cifwk.tm.integration.jira.client;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static com.ericsson.cifwk.tm.integration.utils.SecurityUtils.getSslContext;
import static com.ericsson.cifwk.tm.integration.utils.SecurityUtils.getTrustAllHostNameVerifier;

public abstract class AbstractJiraGateway {

    protected WebTarget client;

    private HttpAuthenticationFeature authenticationFeature;

    public AbstractJiraGateway(JiraConfiguration configuration) throws NoSuchAlgorithmException,
            KeyManagementException {

        authenticationFeature =
                HttpAuthenticationFeature.basic(configuration.getUsername(), configuration.getPassword());

        client = ClientBuilder.newBuilder()
                .register(authenticationFeature)
                .register(JiraObjectMapperProvider.class)
                .register(JacksonFeature.class)
                .register(MultiPartFeature.class)
                .sslContext(getSslContext())
                .hostnameVerifier(getTrustAllHostNameVerifier())
                .build()
                .target(configuration.getUri() + configuration.getApi2MountPoint());

        client.property(ClientProperties.FOLLOW_REDIRECTS, true);
    }

    public Client getClientBuilder() {
        return ClientBuilder.newBuilder()
                .register(authenticationFeature)
                .register(JacksonFeature.class)
                .sslContext(getSslContext())
                .hostnameVerifier(getTrustAllHostNameVerifier())
                .build();
    }

}
