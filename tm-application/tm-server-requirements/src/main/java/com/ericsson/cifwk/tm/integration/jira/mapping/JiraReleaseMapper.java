package com.ericsson.cifwk.tm.integration.jira.mapping;

import com.ericsson.cifwk.tm.integration.jira.client.JiraRelease;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRelease;

public class JiraReleaseMapper implements ExternalEntityMapper<JiraRelease, ExternalRelease> {

    @Override
    public ExternalRelease map(JiraRelease entity) {
        String id = entity.getId();
        String name = entity.getName();
        String projectId = entity.getProjectId();
        return ExternalRelease.builder(id, name, projectId)
                .description(entity.getDescription())
                .build();
    }

}
