package com.ericsson.cifwk.tm.integration.jira.client;


import java.util.Map;

public class JiraIssueStatus {
    private String externalId;
    private String name;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static JiraIssueStatus fromMap(Map map) {
        JiraIssueStatus status = new JiraIssueStatus();

        status.setName((String) map.get("name"));
        status.setExternalId((String) map.get("id"));
        return status;
    }

    @Override
    public String toString() {
        return "JiraIssueStatus{" +
                "name='" + name + '\'' +
                ", externalId=" + externalId +
                '}';
    }
}

