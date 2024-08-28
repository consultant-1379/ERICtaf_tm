/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.impl;

import com.ericsson.cifwk.tm.integration.jira.client.JiraGateway;
import com.ericsson.cifwk.tm.integration.jira.mapping.JiraRequirementMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


public class RequirementManagementImplTest {

    private RequirementManagementImpl requirementManagement;
    private JiraGateway jiraGateway;
    private JiraRequirementMapper mapper;

    @Before
    public void setUp() {
        jiraGateway = mock(JiraGateway.class);
        mapper = mock(JiraRequirementMapper.class);
        requirementManagement = new RequirementManagementImpl(jiraGateway, mapper);
    }

    @Test
    public void testValidJiraId() {
        assertTrue(requirementManagement.validJiraIssueId("CIP-123"));
        assertTrue(requirementManagement.validJiraIssueId("PROJECT-123456"));
        assertFalse(requirementManagement.validJiraIssueId(null));
        assertFalse(requirementManagement.validJiraIssueId(""));
        assertFalse(requirementManagement.validJiraIssueId("sad"));
        assertFalse(requirementManagement.validJiraIssueId("CIP-123,"));
        assertFalse(requirementManagement.validJiraIssueId("CIP-123 is not"));
    }


}
