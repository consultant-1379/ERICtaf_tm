/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.mapping;

import com.ericsson.cifwk.tm.integration.jira.client.JiraIssue;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class JiraRequirementMapperTest {

    private JiraRequirementMapper mapper;
    private JiraIssue issue;

    @Before
    public void setUp() {
        mapper = new JiraRequirementMapper(new JiraFieldNames(), null);
        issue = mock(JiraIssue.class, RETURNS_DEEP_STUBS);
        when(issue.getProject().getKey()).thenReturn("projectId");
        when(issue.getField("customfield_12601", String.class, "issueId")).thenReturn("epicName");
        when(issue.getField("customfield_12600", String.class, "epicLink")).thenReturn(null);
        when(issue.getField("customfield_10123", String.class, "storyPoints")).thenReturn("storyPoints");
        when(issue.getField(JiraIssue.Field.SUMMARY, String.class, "")).thenReturn("summary");
        when(issue.getField(JiraIssue.Field.DESCRIPTION, String.class, "")).thenReturn("Description");
        when(issue.getField("parent", Map.class, null)).thenReturn(null);
        when(issue.getField("issuelinks", Collection.class, null)).thenReturn(null);
        when(issue.getId()).thenReturn("issueId");
        when(issue.getStatus().getName()).thenReturn("ExternalStatusName");
    }

    @Test
    public void testMap_WhenIssueIsNull_ShouldReturnNull() {
        ExternalRequirement requirement = mapper.map(null);
        assertThat(requirement, nullValue());
    }

    @Test
    public void testMap_EpicMap() {
        when(issue.getType().getName()).thenReturn("Epic");

        ExternalRequirement requirement = mapper.map(issue);

        assertThat(requirement.getId(), equalTo("issueId"));
        assertThat(requirement.getTitle(), equalTo("epicName"));
        assertThat(requirement.getProject(), equalTo("projectId"));
        assertThat(requirement.getSummary(), equalTo("summary"));
        assertThat(requirement.getStatusName(), equalTo("ExternalStatusName"));
    }

    @Test
    public void testMap_StoryMap() {
        when(issue.getType().getName()).thenReturn("Story");

        ExternalRequirement requirement = mapper.map(issue);

        assertThat(requirement.getId(), equalTo("issueId"));
        assertThat(requirement.getTitle(), equalTo("issueId"));
        assertThat(requirement.getProject(), equalTo("projectId"));
        assertThat(requirement.getSummary(), equalTo("summary"));
        assertThat(requirement.getStatusName(), equalTo("ExternalStatusName"));
    }

    @Test
    public void testMap_Subtask() {
        when(issue.getType().getName()).thenReturn("Sub-task");

        ExternalRequirement requirement = mapper.map(issue);

        assertThat(requirement.getId(), equalTo("issueId"));
        assertThat(requirement.getTitle(), equalTo("issueId"));
        assertThat(requirement.getProject(), equalTo("projectId"));
        assertThat(requirement.getSummary(), equalTo("summary"));
        assertThat(requirement.getStatusName(), equalTo("ExternalStatusName"));
    }

    @Test
    public void testMap_Unknown() {
        when(issue.getType().getName()).thenReturn("WAT");

        ExternalRequirement requirement = mapper.map(issue);

        assertThat(requirement, nullValue());
    }
}
