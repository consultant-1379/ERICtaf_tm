/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.dto;

public class ExternalRequirement {

    private final String id;
    private final String project;
    private final ExternalRequirementType type;
    private final String title;
    private final String summary;
    private final ExternalRequirement parent;
    private final String statusName;
    private final String deliveredIn;

    public ExternalRequirement(Builder builder) {
        id = builder.id;
        type = builder.type;
        title = builder.title;
        summary = builder.summary;
        parent = builder.parent;
        project = builder.project;
        statusName = builder.statusName;
        deliveredIn = builder.deliveredIn;
    }

    public String getId() {
        return id;
    }

    public ExternalRequirementType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public ExternalRequirement getParent() {
        return parent;
    }

    public String getProject() {
        return project;
    }

    public String getStatusName() {
        return statusName;
    }

    public String getDeliveredIn() {
        return deliveredIn;
    }

    public static Builder builder(String id, ExternalRequirementType type) {
        return new Builder(id, type);
    }

    public static final class Builder {

        private final String id;
        private final ExternalRequirementType type;
        private String title;
        private String summary;
        private ExternalRequirement parent;
        private String project;
        private String statusName;
        private String deliveredIn;

        private Builder(String id, ExternalRequirementType type) {
            this.id = id;
            this.type = type;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder parent(ExternalRequirement parent) {
            this.parent = parent;
            return this;
        }

        public Builder project(String project) {
            this.project = project;
            return this;
        }

        public Builder statusName(String statusName) {
            this.statusName = statusName;
            return this;
        }

        public Builder delivered(String delivered) {
            this.deliveredIn = delivered;
            return this;
        }

        public ExternalRequirement build() {
            return new ExternalRequirement(this);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalRequirement that = (ExternalRequirement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExternalRequirement{" +
                "parent=" + parent +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", project='" + project + '\'' +
                ", statusName='" + statusName + '\'' +
                ", deliveredIn='" + deliveredIn + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
