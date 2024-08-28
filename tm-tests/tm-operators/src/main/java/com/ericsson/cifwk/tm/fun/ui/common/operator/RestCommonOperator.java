/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common.operator;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.RequestBuilder;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;
import com.ericsson.cifwk.taf.tools.http.constants.HttpStatus;
import com.ericsson.cifwk.tm.fun.ui.common.HttpToolHolder;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.NotificationType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.ReferenceSerializer;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestExecutionResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.testexecution.references.TestStepResultType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.CaseInsensitiveHashMap;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import groovy.util.ConfigObject;

import javax.inject.Inject;
import javax.inject.Provider;

import static java.lang.String.format;

public abstract class RestCommonOperator {

    public static final String JSON_MIME_TYPE = "application/json";
    public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    protected static final Gson json = new GsonBuilder()
            .setDateFormat(ISO_8601)
            .registerTypeAdapter(NotificationType.class, new ReferenceSerializer())
            .registerTypeAdapter(TestExecutionResultType.class, new ReferenceSerializer())
            .registerTypeAdapter(TestStepResultType.class, new ReferenceSerializer())
            .create();

    protected HttpTool httpTool;

    @Inject
    protected Provider<HttpToolHolder> httpToolHolderProvider;

    protected RequestBuilder newRequest() {
        return httpTool.request()
                .header(HttpHeaders.ACCEPT, JSON_MIME_TYPE)
                .contentType(ContentType.APPLICATION_JSON);
    }

    protected String getErrorMessage(HttpResponse response) {
        ConfigObject configObject = json.fromJson(response.getBody(), ConfigObject.class);
        if (configObject.get("message") == null) {
            throw new RuntimeException("Response body does not have 'message' parameter!");
        }
        return (String) configObject.get("message");
    }

    protected void checkHttpStatus(String requestUrl, HttpResponse response, HttpStatus expectedHttpStatus) {
        HttpStatus actualHttpStatus = response.getResponseCode();
        if (!expectedHttpStatus.equals(actualHttpStatus)) {
            String message = format("HttpStatus for '%s' expected '%s', but was '%s'", requestUrl, expectedHttpStatus, actualHttpStatus);
            throw new RuntimeException(message);
        }
    }

    protected void checkContentType(String requestUrl, HttpResponse response, String expectedContentType) {
        CaseInsensitiveHashMap<String> headers = new CaseInsensitiveHashMap<>();
        headers.putAll(response.getHeaders());
        String actualContentType = headers.get("content-type");
        if (actualContentType == null || !actualContentType.contains(expectedContentType)) {
            String message = format("Content type for '%s' expected '%s', but was '%s'", requestUrl, expectedContentType, actualContentType);
            throw new RuntimeException(message);
        }
    }

}
