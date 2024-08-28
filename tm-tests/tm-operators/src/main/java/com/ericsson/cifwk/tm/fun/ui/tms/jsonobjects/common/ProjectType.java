/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;

public enum ProjectType {

    ALL_PROJECTS(getProject(0L, "", "All Projects")),
    ASSURE_PROJECT(getProject(1L, "EQEV", "ASSURE")),
    CIP_PROJECT(getProject(2L, "CIP", "CI_FrameworkTeam_PDUOSS")),
    DURA_CI_PROJECT(getProject(3L, "DURACI", "DURA CI"));

    private ProjectInfo projectInfo;

    ProjectType(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
    }

    public static ProjectInfo getProject(Long id, String externalId, String name){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setId(id);
        projectInfo.setExternalId(externalId);
        projectInfo.setName(name);
        return projectInfo;

    }

    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }
}
