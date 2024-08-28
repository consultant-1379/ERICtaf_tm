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

public class ExternalDefect {

    private final String id;
    private final ExternalDefectType type;
    private final String project;
    private final String title;
    private final String summary;
    private final String statusName;
    private final String deliveredIn;

    public ExternalDefect(Builder builder) {
        id = builder.id;
        project = builder.project;
        title = builder.title;
        summary = builder.summary;
        type = builder.type;
        statusName = builder.statusName;
        deliveredIn = builder.deliveredIn;
    }

    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getStatusName() {
        return statusName;
    }

    public ExternalDefectType getType() {
        return type;
    }

    public String getDeliveredIn() {
        return deliveredIn;
    }


    public static Builder builder(String id, ExternalDefectType type) {
        return new Builder(id, type);
    }

    public static final class Builder {

        private final String id;
        private final ExternalDefectType type;
        private String title;
        private String summary;
        private String project;
        private String statusName;
        private String deliveredIn;

        private Builder(String id, ExternalDefectType type) {
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

        public Builder project(String project) {
            this.project = project;
            return this;
        }

        public Builder statusName(String statusName) {
            this.statusName = statusName;
            return this;
        }

        public Builder deliveredIn(String deliveredIn) {
            this.deliveredIn = deliveredIn;
            return this;
        }

        public ExternalDefect build() {
            return new ExternalDefect(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternalDefect that = (ExternalDefect) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExternalDefect{" +
                "id='" + id + '\'' +
                ", project='" + project + '\'' +
                ", title='" + title + '\'' +
                ", statusName='" + statusName + '\'' +
                ", deliveredIn='" + deliveredIn + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
