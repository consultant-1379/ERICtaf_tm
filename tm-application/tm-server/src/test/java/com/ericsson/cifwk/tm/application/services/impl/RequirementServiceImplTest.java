/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.domain.model.requirements.ExternalRequirementType;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.test.TestRequirements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ericsson.cifwk.tm.test.TestRequirements.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequirementServiceImplTest {

    @Mock
    private RequirementRepository requirementRepository;

    @Mock
    private RequirementManagement requirementManagement;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private RequirementServiceImpl service;


    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        TestRequirements testRequirements = new TestRequirements();
        doReturn(testRequirements.get(EPIC1)).when(requirementManagement).fetchById(EPIC1);
        doReturn(testRequirements.get(EPIC2)).when(requirementManagement).fetchById(EPIC2);
        doReturn(testRequirements.get(STORY1)).when(requirementManagement).fetchById(STORY1);
        doReturn(testRequirements.get(STORY2)).when(requirementManagement).fetchById(STORY2);
        doReturn(testRequirements.get(SUBTASK1)).when(requirementManagement).fetchById(SUBTASK1);
    }

    @Test
    public void testUpdateFromJira() {
        when(projectRepository.findByExternalId(PROJECT_ID)).thenReturn(new Project(PROJECT_ID));

        Requirement requirement = new Requirement(SUBTASK1);

        service.syncWithExternalSystem(requirement);

        assertThat(requirement.getExternalSummary(), equalTo(SUBTASK1_SUMMARY));
        assertThat(requirement.getExternalLabel(), equalTo(SUBTASK1));
        assertThat(requirement.getExternalType(), equalTo(ExternalRequirementType.SUBTASK.toString()));

        Requirement lvl1Parent = requirement.getParent();
        assertThat(lvl1Parent.getExternalSummary(), equalTo(STORY1_SUMMARY));
        assertThat(lvl1Parent.getExternalLabel(), equalTo(STORY1));
        assertThat(lvl1Parent.getExternalType(), equalTo(ExternalRequirementType.STORY.toString()));

        Requirement lvl2Parent = lvl1Parent.getParent();
        assertThat(lvl2Parent.getExternalSummary(), equalTo(EPIC1_SUMMARY));
        assertThat(lvl2Parent.getExternalLabel(), equalTo(EPIC1_TITLE));
        assertThat(lvl2Parent.getExternalType(), equalTo(ExternalRequirementType.EPIC.toString()));
        assertThat(lvl2Parent.getParent(), nullValue());
    }

    @Test
    public void testUpdateParent() {
        when(projectRepository.findByExternalId(PROJECT_ID)).thenReturn(new Project(PROJECT_ID));

        Requirement requirement = new Requirement(STORY1);
        Requirement requirementParent = new Requirement(EPIC1);
        requirementParent.setExternalSummary("Olde summary");
        requirementParent.setExternalLabel("Olde title");
        requirementParent.addChild(requirement);

        service.syncWithExternalSystem(requirement);

        assertThat(requirement.getExternalSummary(), equalTo(STORY1_SUMMARY));
        assertThat(requirement.getExternalLabel(), equalTo(STORY1));
        assertThat(requirement.getExternalType(), equalTo(ExternalRequirementType.STORY.toString()));

        Requirement lvl1Parent = requirement.getParent();
        assertThat(lvl1Parent, sameInstance(requirementParent));
        assertThat(lvl1Parent.getExternalSummary(), equalTo(EPIC1_SUMMARY));
        assertThat(lvl1Parent.getExternalLabel(), equalTo(EPIC1_TITLE));
        assertThat(lvl1Parent.getExternalType(), equalTo(ExternalRequirementType.EPIC.toString()));
        assertThat(lvl1Parent.getParent(), nullValue());
    }

    @Test
    public void testParentChanged() {
        when(projectRepository.findByExternalId(PROJECT_ID)).thenReturn(new Project(PROJECT_ID));

        Requirement requirement = new Requirement(STORY1);
        Requirement requirementParent = new Requirement("NOT-EXISTING");
        requirementParent.addChild(requirement);

        service.syncWithExternalSystem(requirement);

        assertThat(requirement.getExternalSummary(), equalTo(STORY1_SUMMARY));
        assertThat(requirement.getExternalLabel(), equalTo(STORY1));
        assertThat(requirement.getExternalType(), equalTo(ExternalRequirementType.STORY.toString()));

        Requirement lvl1Parent = requirement.getParent();
        assertThat(lvl1Parent, not(sameInstance(requirementParent)));
        assertThat(lvl1Parent.getExternalSummary(), equalTo(EPIC1_SUMMARY));
        assertThat(lvl1Parent.getExternalLabel(), equalTo(EPIC1_TITLE));
        assertThat(lvl1Parent.getExternalType(), equalTo(ExternalRequirementType.EPIC.toString()));
        assertThat(lvl1Parent.getParent(), nullValue());

        assertThat(requirementParent.getChildren(), empty());
    }

}
