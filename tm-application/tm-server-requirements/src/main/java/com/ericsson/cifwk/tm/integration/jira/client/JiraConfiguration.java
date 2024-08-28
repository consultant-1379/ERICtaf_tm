/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.client;

import com.google.common.base.Preconditions;
import com.netflix.governator.annotations.Configuration;

import javax.annotation.PostConstruct;

public class JiraConfiguration {

    @Configuration("services.jira.username")
    private String username;

    @Configuration("services.jira.password")
    private String password;

    @Configuration("services.jira.uri")
    private String uri;

    @Configuration("services.jira.api1")
    private String api1MountPoint;

    @Configuration("services.jira.api2")
    private String api2MountPoint;

    @PostConstruct
    public void validate() {
        Preconditions.checkNotNull(username);
        Preconditions.checkNotNull(password);
        Preconditions.checkNotNull(uri);
        Preconditions.checkNotNull(api1MountPoint);
        Preconditions.checkNotNull(api2MountPoint);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUri() {
        return uri;
    }

    public String getApi1MountPoint() {
        return api1MountPoint;
    }

    public String getApi2MountPoint() {
        return api2MountPoint;
    }

}
