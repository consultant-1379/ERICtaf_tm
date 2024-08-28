/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.netflix.governator.annotations.Configuration;

import javax.annotation.PostConstruct;

public class BuildConfiguration {

    @Configuration("build.version")
    private String version;

    @Configuration("build.date")
    private String date;

    @PostConstruct
    public void normalize() {
        version = removePlaceholder(version);
        date = removePlaceholder(date);
    }

    String removePlaceholder(String text) {
        return text.replaceAll("\\$\\{.*\\}", "");
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }
}
