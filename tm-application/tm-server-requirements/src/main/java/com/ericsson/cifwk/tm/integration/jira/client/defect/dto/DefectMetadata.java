package com.ericsson.cifwk.tm.integration.jira.client.defect.dto;

import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields.FieldMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields.GenericFieldMetadata;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectMetadata {

    private List<Project> projects;

    @JsonCreator
    public DefectMetadata(@JsonProperty("projects") List<Project> projects) {
        this.projects = projects;
    }

    public FieldMetadata getFields() {
        if (!projects.isEmpty()) {
            return projects
                .get(0)
                .getIssuetypes()
                .get(0)
                .getFieldMetadata();
        }
        return null;
    }

    public Map<String, Object> getProject() {
        if (!projects.isEmpty()) {
            return projects
                    .get(0)
                    .getProjectDataMap();

        }
        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Project {

        private String projectKey;

        private String projectName;

        private String projectId;

        private List<IssueType> issuetypes;

        private static final String ID = "id";

        private static final String NAME = "name";

        private static final String EXTERNAL_ID = "external_id";

        private  Map<String, Object> projectDataMap = new HashMap();


        @JsonCreator
        public Project(@JsonProperty(value = "key") String projectKey,
                       @JsonProperty(value = "name") String projectName,
                       @JsonProperty(value = "id") String projectId,
                       @JsonProperty(value = "issuetypes") List<IssueType> issuetypes) {

            this.projectKey = projectKey;
            this.projectName = projectName;
            this.issuetypes = issuetypes;
            this.projectId = projectId;

            projectDataMap.put(ID, this.projectId);
            projectDataMap.put(EXTERNAL_ID, this.projectKey);
            projectDataMap.put(NAME, this.projectName);
        }

        public List<IssueType> getIssuetypes() {
            return issuetypes;
        }

        public Map<String, Object> getProjectDataMap() {
            return projectDataMap;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class IssueType {

        private String name;

        private FieldMetadata fieldMetadata;

        public IssueType(@JsonProperty(value = "name") String name,
                         @JsonProperty(value = "fields") GenericFieldMetadata fields) {

            this.name = name;
            this.fieldMetadata = fields;
        }

        public String getName() {
            return name;
        }

        public FieldMetadata getFieldMetadata() {
            return fieldMetadata;
        }
    }

}
