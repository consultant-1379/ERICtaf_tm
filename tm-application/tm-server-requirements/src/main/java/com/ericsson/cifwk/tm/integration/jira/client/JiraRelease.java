package com.ericsson.cifwk.tm.integration.jira.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraRelease {

    private static final GenericType<List<JiraRelease>> RELEASE_LIST = new GenericType<List<JiraRelease>>() {
    };

    private static final GenericType<JiraRelease> RELEASE = new GenericType<JiraRelease>() {
    };

    private static final String PROJECT_PATH_PART = "project/";
    private static final String VERSIONS_PATH_PART = "/versions";


    private String id;
    private String description;
    private String name;
    private String projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public static List<JiraRelease> search(WebTarget client, String projectIdOrKey) throws IOException {
        StringBuilder pathSB = new StringBuilder()
                .append(PROJECT_PATH_PART)
                .append(projectIdOrKey)
                .append(VERSIONS_PATH_PART);

        return client
                .path(pathSB.toString())
                .request().get(RELEASE_LIST);
    }

    public static JiraRelease get(WebTarget client, String projectIdOrKey, String versionId) throws IOException {
        StringBuilder pathSB = new StringBuilder()
                .append(PROJECT_PATH_PART)
                .append(projectIdOrKey)
                .append(VERSIONS_PATH_PART)
                .append("/")
                .append(versionId);

        return client
                .path(pathSB.toString())
                .request().get(RELEASE);
    }


}
