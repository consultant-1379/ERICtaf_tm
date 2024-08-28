/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.client;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class JiraIssueTypeTest {

    private Map<String, Object> map;

    @Before
    public void setUp() {
        map = Maps.newHashMap();
        map.put("name", "correctName");
        map.put("description", "correctDescription");
        map.put("subtask", true);
    }

    @Test
    public void testFromMap() {
        JiraIssueType jiraIssueType = JiraIssueType.fromMap(map);

        assertThat(jiraIssueType.getName(), equalTo("correctName"));
        assertThat(jiraIssueType.getDescription(), equalTo("correctDescription"));
        assertThat(jiraIssueType.isSubtask(), is(true));
    }

    @Test
    public void testToString() {
        JiraIssueType jiraIssueType = JiraIssueType.fromMap(Maps.newHashMap());
        assertThat(jiraIssueType.toString().length(), is(not(0)));
    }
}
