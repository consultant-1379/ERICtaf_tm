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

import java.util.Map;

public class JiraIssueType {
    private String name;
    private String description;
    private boolean subtask;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }

    static JiraIssueType fromMap(Map map) {
        JiraIssueType type = new JiraIssueType();

        type.setName((String) map.get("name"));
        type.setDescription((String) map.get("description"));
        type.setSubtask(Boolean.TRUE.equals(map.get("subtask")));

        return type;
    }

    @Override
    public String toString() {
        return "JiraIssueType{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subtask=" + subtask +
                '}';
    }
}
