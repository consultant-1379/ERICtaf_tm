package com.ericsson.cifwk.tm.common;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class ClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactory.class);

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

    public static WebTarget newWebtarget(String url) {
        Client client = ClientBuilder.newBuilder()
                .register(JacksonFeature.class)
                .sslContext(getSslContext())
                .hostnameVerifier(getTrustAllHostNameVerifier())
                .build();

        client.property(ClientProperties.FOLLOW_REDIRECTS, true);

        return client.target(url);
    }

    public static SSLContext getSslContext() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, certs, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("SSL Context error");
        }
        return sslContext;
    }

    public static TrustAllHostNameVerifier getTrustAllHostNameVerifier() {
        return new TrustAllHostNameVerifier();
    }

    private static TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            },
    };

    private static class TrustAllHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

}
