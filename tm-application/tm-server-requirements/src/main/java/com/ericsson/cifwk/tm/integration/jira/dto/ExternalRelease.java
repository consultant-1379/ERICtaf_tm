package com.ericsson.cifwk.tm.integration.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalRelease {

    private final String id;
    private final String name;
    private final String description;
    private final String projectId;

    public ExternalRelease(Builder builder) {
        id = builder.id;
        name = builder.name;
        projectId = builder.projectId;
        description = builder.description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProjectId() {
        return projectId;
    }

    public static Builder builder(String id, String name, String projectId) {
        return new Builder(id, name, projectId);
    }

    public static final class Builder {

        private final String id;
        private String name;
        private String projectId;
        private String description;

        private Builder(String id, String name, String projectId) {
            this.id = id;
            this.name = name;
            this.projectId = projectId;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ExternalRelease build() {
            return new ExternalRelease(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalRelease that = (ExternalRelease) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (projectId != null ? projectId.hashCode() : 0);
        return result;
    }
}
