/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search;

public enum Field {

    ANY("Any field", "any"),
    COMPONENT("Component", "component"),
    GROUP("Group", "group"),
    PRIORITY("Priority", "priority"),
    PROJECT_ID("Project ID", "projectId"),
    PROJECT_NAME("Project Name", "projectName"),
    REQUIREMENT_NAME("Requirement Title", "requirementTitle"),
    TYPE("Type", "type");

    private final String title;
    private final String value;

    private Field(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
