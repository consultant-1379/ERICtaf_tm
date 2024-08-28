/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.mapping;

import com.google.common.base.Preconditions;
import com.netflix.governator.annotations.Configuration;

import javax.annotation.PostConstruct;

public class JiraFieldNames {

    @Configuration("services.jira.fields.epicName")
    private String epicName = "customfield_12601";

    @Configuration("services.jira.fields.epicLink")
    private String epicLink = "customfield_12600";

    @Configuration("services.jira.fields.storyPoints")
    private String storyPoints = "customfield_10123";

    @Configuration("services.jira.fields.deliveredInSprint")
    private String deliveredInSprint = "customfield_12202";

    @PostConstruct
    public void validate() {
        Preconditions.checkNotNull(epicName);
        Preconditions.checkNotNull(epicLink);
        Preconditions.checkNotNull(storyPoints);
        Preconditions.checkNotNull(deliveredInSprint);
    }

    public String getEpicName() {
        return epicName;
    }

    public String getEpicLink() {
        return epicLink;
    }

    public String getStoryPoints() {
        return storyPoints;
    }

    public String getDeliveredInSprint() {
        return deliveredInSprint;
    }
}
