/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common;

import com.ericsson.cifwk.taf.annotations.VUserScoped;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@VUserScoped
public class HttpToolHolder {

    private static final Logger logger = LoggerFactory.getLogger(HttpToolHolder.class);

    private HttpTool httpTool;

    public void init(Host host) {
        logger.info("Creating new HttpTool" + System.identityHashCode(this));
        httpTool = HttpToolBuilder.newBuilder(host)
                .useHttpsIfProvided(true)
                .trustSslCertificates(true)
                .followRedirect(true)
                .build();
    }

    public HttpTool getHttpTool() {
        logger.info("Using HttpTool " + System.identityHashCode(this));
        if (httpTool == null) {
            throw new IllegalStateException();
        }
        return httpTool;
    }

}
