/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.presentation.dto.DefectInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DefectView;
import com.ericsson.cifwk.tm.test.TestDtoFactory;
import org.junit.Before;
import org.junit.Test;

import static com.ericsson.cifwk.tm.test.fixture.TestEntityFactory.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DefectMapperTest {

    private DefectMapper defectMapper;

    @Before
    public void setUp() {
        defectMapper = new DefectMapper(new ProjectMapper(new ProductMapper()));
    }

    @Test
    public void testMapEntity_WhenDtoIsNull_ShouldReturnNull() {
        assertThat(defectMapper.mapDto(null, new Defect("externalId")), nullValue());
        assertThat(defectMapper.mapDto(null, Defect.class), nullValue());
    }

    @Test
    public void testMapEntity_WithoutProject() {
        Defect defect = createDefect();

        DefectInfo defectInfo = defectMapper.mapEntity(defect, DefectInfo.class);

        validateDefect(defect, defectInfo);
        assertThat(defect.getProject(), nullValue());
    }

    @Test
    public void testMapEntity_WithoutProject_2() {
        Defect defect = createDefect();

        DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo());

        validateDefect(defect, defectInfo);
        assertThat(defect.getProject(), nullValue());
    }

    @Test
    public void testMapEntity_WithProject() {
        Project project = createProject();
        Defect defect = createDefectWithProject(project);

        DefectInfo defectInfo = defectMapper.mapEntity(defect, DefectInfo.class);

        validateDefectInfo(defectInfo, defect);
        validateProjectInfo(defectInfo.getProject(), project);
    }

    @Test
    public void testMapEntity_WithProject_2() {
        Project project = createProject();
        Defect defect = createDefectWithProject(project);

        DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo());

        validateDefectInfo(defectInfo, defect);
        validateProjectInfo(defectInfo.getProject(), project);
    }

    @Test
    public void testMapEntity_WithProject_WhenSimpleView() {
        Project project = createProject();
        Defect defect = createDefectWithProject(project);

        DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo(), DefectView.Simple.class);

        assertThat(defectInfo.getId(), equalTo(defect.getId()));
        assertThat(defectInfo.getExternalId(), equalTo(defect.getExternalId()));
        assertThat(defectInfo.getExternalTitle(), equalTo(defect.getExternalTitle()));
        assertThat(defectInfo.getExternalStatusName(), equalTo(defect.getExternalStatusName()));

        assertThat(defectInfo.getExternalSummary(), equalTo(defect.getExternalSummary()));
        assertThat(defectInfo.getProject(), nullValue());
    }

    @Test
    public void testMapEntity_WithProject_WhenDetailedView() {
        Project project = createProject();
        Defect defect = createDefectWithProject(project);

        DefectInfo defectInfo = defectMapper.mapEntity(defect, new DefectInfo(), DefectView.Detailed.class);

        validateDefectInfo(defectInfo, defect);
        validateProjectInfo(defectInfo.getProject(), project);
    }

    @Test
    public void testMapDto_WithoutProject() {
        DefectInfo defectInfo = TestDtoFactory.getDefectInfo(1L);
        Defect defect = defectMapper.mapDto(defectInfo, Defect.class);

        validateDefect(defect, defectInfo);
        assertThat(defect.getProject(), nullValue());
    }

    @Test
    public void testMapDto_WithoutProject_2() {
        DefectInfo defectInfo = TestDtoFactory.getDefectInfo(1L);
        Defect defect = defectMapper.mapDto(defectInfo, new Defect("externalId"));

        validateDefect(defect, defectInfo);
        assertThat(defect.getProject(), nullValue());
    }

    @Test
    public void testMapDto_WithProject() {
        ProjectInfo projectInfo = TestDtoFactory.getProjectInfo(2);
        DefectInfo defectInfo = TestDtoFactory.getDefectInfo(1L, projectInfo);

        Defect defect = defectMapper.mapDto(defectInfo, Defect.class);

        validateDefect(defect, defectInfo);
        validateProject(defect.getProject(), projectInfo);
    }

    @Test
    public void testMapDto_WithProject_2() {
        ProjectInfo projectInfo = TestDtoFactory.getProjectInfo(2);
        DefectInfo defectInfo = TestDtoFactory.getDefectInfo(1L, projectInfo);

        Defect defect = defectMapper.mapDto(defectInfo, new Defect("id"));

        validateDefect(defect, defectInfo);
        validateProject(defect.getProject(), projectInfo);
    }

    private void validateDefect(Defect defect, DefectInfo defectInfo) {
        assertThat(defect.getId(), equalTo(defectInfo.getId()));
        assertThat(defect.getExternalId(), equalTo(defectInfo.getExternalId()));
        assertThat(defect.getExternalTitle(), equalTo(defectInfo.getExternalTitle()));
        assertThat(defect.getExternalSummary(), equalTo(defectInfo.getExternalSummary()));
        assertThat(defect.getExternalStatusName(), equalTo(defectInfo.getExternalStatusName()));
    }

    private void validateDefectInfo(DefectInfo defectInfo, Defect defect) {
        assertThat(defectInfo.getId(), equalTo(defect.getId()));
        assertThat(defectInfo.getExternalId(), equalTo(defect.getExternalId()));
        assertThat(defectInfo.getExternalTitle(), equalTo(defect.getExternalTitle()));
        assertThat(defectInfo.getExternalSummary(), equalTo(defect.getExternalSummary()));
    }

    private void validateProject(Project project, ProjectInfo projectInfo) {
        assertThat(project.getId(), equalTo(projectInfo.getId()));
        assertThat(project.getExternalId(), equalTo(projectInfo.getExternalId()));
        assertThat(project.getName(), equalTo(projectInfo.getName()));
    }

    private void validateProjectInfo(ProjectInfo projectInfo, Project project) {
        assertThat(projectInfo.getId(), equalTo(project.getId()));
        assertThat(projectInfo.getExternalId(), equalTo(project.getExternalId()));
        assertThat(projectInfo.getName(), equalTo(project.getName()));
    }

    private Defect createDefect() {
        return buildDefect().build();
    }

    private Defect createDefectWithProject(Project project) {
        return buildDefect()
                .withProject(project)
                .build();
    }

    private Project createProject() {
        return buildProject(buildProduct().build()).build();
    }
}
