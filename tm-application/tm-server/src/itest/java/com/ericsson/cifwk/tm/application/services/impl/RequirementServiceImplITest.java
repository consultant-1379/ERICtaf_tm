/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.application.services.RequirementService;
import com.ericsson.cifwk.tm.domain.model.requirements.ExternalRequirementType;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.test.TestRequirements.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class RequirementServiceImplITest extends BaseServiceLayerTest {

    @Inject
    private RequirementService requirementService;

    @Inject
    private RequirementRepository requirementRepository;

    @Before
    public void setUp() {
        persistence.cleanupTables();
    }

    @Test
    public void testUpdateAllFromJira() {
        Requirement epic1 = getRequirement(EPIC1);
        Requirement story1 = getRequirement(STORY1);
        epic1.addChild(story1);
        Requirement subtask1 = getRequirement(SUBTASK1);
        story1.addChild(subtask1);
        Requirement epic2 = getRequirement(EPIC2);

        persistence.persistInTransaction(subtask1, epic2);

        requirementService.fetchAndSaveUpdatedRequirements();

        assertSameAsJiraStub();
    }

    private void assertSameAsJiraStub() {
        Requirement subtask1 = requirementRepository.findByExternalId(SUBTASK1);
        Requirement story1 = subtask1.getParent();
        Requirement epic1 = story1.getParent();
        Requirement epic2 = requirementRepository.findByExternalId(EPIC2);

        assertThat(subtask1.getExternalId(), equalTo(SUBTASK1));
        assertThat(subtask1.getExternalLabel(), equalTo(SUBTASK1));
        assertThat(subtask1.getExternalSummary(), equalTo(SUBTASK1_SUMMARY));
        assertThat(subtask1.getExternalType(), equalTo(ExternalRequirementType.SUBTASK.toString()));

        assertThat(story1.getExternalId(), equalTo(STORY1));
        assertThat(story1.getExternalLabel(), equalTo(STORY1));
        assertThat(story1.getExternalSummary(), equalTo(STORY1_SUMMARY));
        assertThat(story1.getExternalType(), equalTo(ExternalRequirementType.STORY.toString()));

        assertThat(epic1.getExternalId(), equalTo(EPIC1));
        assertThat(epic1.getExternalLabel(), equalTo(EPIC1_TITLE));
        assertThat(epic1.getExternalSummary(), equalTo(EPIC1_SUMMARY));
        assertThat(epic1.getExternalType(), equalTo(ExternalRequirementType.EPIC.toString()));

        assertThat(epic2.getExternalId(), equalTo(EPIC2));
        assertThat(epic2.getExternalLabel(), equalTo(EPIC2));
        assertThat(epic2.getExternalSummary(), equalTo(EPIC2_SUMMARY));
        assertThat(epic2.getExternalType(), equalTo(ExternalRequirementType.EPIC.toString()));
        assertThat(epic2.getChildren(), hasSize(0));
    }

    private Requirement getRequirement(String id) {
        return new Requirement(id);
    }

}
