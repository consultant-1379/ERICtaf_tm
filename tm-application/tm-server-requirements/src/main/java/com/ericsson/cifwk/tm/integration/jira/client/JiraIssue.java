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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {

    @JsonProperty(value = "key")
    private String id;

    @JsonProperty(value = "fields")
    private Map<String, Object> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public void setFields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public <T> T getField(String name, Class<T> type, T defaultValue) {
        Object o = fields != null ? fields.get(name) : null;
        if (type.isInstance(o)) {
            return type.cast(o);
        }
        return defaultValue;
    }

    public <T> T getField(Field field, Class<T> type, T defaultValue) {
        return getField(field.getName(), type, defaultValue);
    }

    public JiraProject getProject() {
        Map project = getField(Field.PROJECT, Map.class, null);
        if (project != null) {
            return JiraProject.fromMap(project);
        }
        return null;
    }

    public JiraIssueType getType() {
        Map type = getField(Field.TYPE, Map.class, null);
        if (type != null) {
            return JiraIssueType.fromMap(type);
        }
        return null;
    }

    public JiraIssueStatus getStatus() {
        Map status = getField(Field.STATUS, Map.class, null);
        if (status != null) {
            return JiraIssueStatus.fromMap(status);
        }
        return null;
    }

    static SearchResult search(WebTarget client, String jql, int pageNumber, int resultsPerPage) throws IOException {
        return search(client, jql, null, pageNumber, resultsPerPage);
    }

    static SearchResult search(WebTarget client, String jql, List<String> fields, int pageNumber, int resultsPerPage)
            throws IOException {
        WebTarget target = client
                .path("search")
                .queryParam("jql", jql)
                .queryParam("startAt", (pageNumber - 1) * resultsPerPage)
                .queryParam("maxResults", resultsPerPage);

        if (fields != null && !fields.isEmpty()) {
            target = target.queryParam("fields", Joiner.on(",").join(fields));
        }

        return target.request().get(SearchResult.class);
    }

    static JiraIssue get(WebTarget client, String requirementId) throws IOException {
        return client
                .path("issue/" + requirementId)
                .request().get(JiraIssue.class);
    }

    public enum Field {
        CREATED("created"),
        UPDATED("updated"),
        DESCRIPTION("description"),
        PROJECT("project"),
        STATUS("status"),
        SUMMARY("summary"),
        TYPE("issuetype"),
        DELIVERED_IN("customfield_12202");

        private final String name;

        Field(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JiraIssue jiraIssue = (JiraIssue) o;

        if (id != null ? !id.equals(jiraIssue.id) : jiraIssue.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "JiraIssue{" +
                "id='" + id + '\'' +
                '}';
    }
}
