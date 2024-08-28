/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.dto.view.DefectView;
import com.ericsson.cifwk.tm.presentation.validation.ExistingProject;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.Valid;

public class DefectInfo implements Identifiable<Long> {

    private Long id;

    @NotNullField("externalId")
    private String externalId;

    private String externalTitle;

    private String externalStatusName;

    @JsonView(DefectView.Detailed.class)
    private String externalSummary;

    @Valid
    @ExistingProject
    @JsonView(DefectView.Detailed.class)
    private ProjectInfo project;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalTitle() {
        return externalTitle;
    }

    public void setExternalTitle(String externalTitle) {
        this.externalTitle = externalTitle;
    }

    public String getExternalSummary() {
        return externalSummary;
    }

    public void setExternalSummary(String externalSummary) {
        this.externalSummary = externalSummary;
    }

    public ProjectInfo getProject() {
        return project;
    }

    public void setProject(ProjectInfo project) {
        this.project = project;
    }

    public String getExternalStatusName() {
        return externalStatusName;
    }

    public void setExternalStatusName(String externalStatusName) {
        this.externalStatusName = externalStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefectInfo that = (DefectInfo) o;

        if (!externalId.equals(that.externalId)) return false;
        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + externalId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DefectInfo{" +
                "id=" + id +
                ", externalId='" + externalId + '\'' +
                ", externalTitle='" + externalTitle + '\'' +
                ", externalSummary='" + externalSummary + '\'' +
                ", project=" + project +
                ", externalStatusName=" + externalStatusName +
                '}';
    }
}
