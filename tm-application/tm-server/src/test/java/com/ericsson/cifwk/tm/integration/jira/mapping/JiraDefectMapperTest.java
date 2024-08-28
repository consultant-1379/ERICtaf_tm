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
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefectType;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class JiraDefectMapperTest {

    private JiraDefectMapper mapper;


    private JiraIssue issue;


    @Before
    public void setUp() {
        mapper = new JiraDefectMapper();
        issue = mock(JiraIssue.class, RETURNS_DEEP_STUBS);
    }

    @Test
    public void testToExternalDefect_WhenTypeIsBug_ShouldReturnExternalDefect() {
        when(issue.getId()).thenReturn("issueId");
        when(issue.getField(JiraIssue.Field.SUMMARY, String.class, "")).thenReturn("summaryId");
        when(issue.getProject().getKey()).thenReturn("projectId");
        when(issue.getType().getName()).thenReturn("Bug");
        when(issue.getStatus().getName()).thenReturn("ExternalStatusName");
        Map<String, Object> deliveredIn = new HashMap<>();
        deliveredIn.put("value", "15.4");
        when(issue.getField(JiraIssue.Field.DELIVERED_IN.getName(), Map.class, null)).thenReturn(deliveredIn);

        ExternalDefect externalDefect = mapper.map(issue);

        assertThat(externalDefect.getId(), equalTo("issueId"));
        assertThat(externalDefect.getSummary(), equalTo("summaryId"));
        assertThat(externalDefect.getProject(), equalTo("projectId"));
        assertThat(externalDefect.getTitle(), equalTo("issueId"));
        assertThat(externalDefect.getType(), is(ExternalDefectType.BUG));
        assertThat(externalDefect.getStatusName(), equalTo("ExternalStatusName"));
        assertThat(externalDefect.getDeliveredIn(), equalTo("15.4"));
    }

    @Test
    public void testToExternalDefect_WhenTypeIsNotBug_ShouldReturnNull() {
        when(issue.getId()).thenReturn("issueId");
        when(issue.getField(JiraIssue.Field.SUMMARY, String.class, "")).thenReturn("summaryId");
        when(issue.getProject().getKey()).thenReturn("projectId");
        when(issue.getType().getName()).thenReturn("Sub-task");

        ExternalDefect externalDefect = mapper.map(issue);

        assertThat(externalDefect, nullValue());
    }

}
