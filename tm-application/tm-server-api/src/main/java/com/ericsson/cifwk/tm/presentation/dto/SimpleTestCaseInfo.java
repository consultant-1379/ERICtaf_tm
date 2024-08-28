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
import com.ericsson.cifwk.tm.presentation.dto.view.TestCaseView;
import com.ericsson.cifwk.tm.presentation.validation.FixedSizeField;
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;
import com.ericsson.cifwk.tm.presentation.validation.RequirementId;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.LinkedHashSet;
import java.util.Set;

public class SimpleTestCaseInfo implements Identifiable<Long> {

    private Long id;

    @NotEmptyField("testCaseId")
    private String testCaseId;

    @NotEmptyField("title")
    @FixedSizeField("title")
    private String title;

    private String description;

    private Long versionId;

    @JsonView(TestCaseView.Detailed.class)
    private String comment;

    private String version;

    @RequirementId
    @NotEmptyField("requirementIds")
    private LinkedHashSet<String> requirementIds = new LinkedHashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getRequirementIds() {
        return requirementIds;
    }

    public void setRequirementIds(LinkedHashSet<String> requirementIds) {
        this.requirementIds = requirementIds;
    }

    public String getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
