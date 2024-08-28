/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.integration.jira.impl;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.infrastructure.GuiceTestRunner;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(GuiceTestRunner.class)
public class RequirementManagementImplITest extends BaseServiceLayerTest {

    @Inject
    RequirementManagement requirementManagement;

    @Test
    public void getJiraIssue() {
        ExternalRequirement externalRequirement = requirementManagement.fetchById("NON-EXISTING-123");
        assertThat(externalRequirement, nullValue());

        List<String> issues = Arrays.asList("CIP-753", "CIP-4167", "CIP-4168");
        for (String issueId : issues) {
            externalRequirement = requirementManagement.fetchById(issueId);
            assertThat(externalRequirement, notNullValue());
            assertThat(externalRequirement.getProject(), equalTo("CIP"));
        }
    }

    @Test
    public void getJiraIssues() {
        List<String> issues = Arrays.asList("CIP-753", "CIP-4167", "CIP-4168", "CIP-4325");
        Map<String, ExternalRequirement> jiraIssues = requirementManagement.fetchById(issues);
        for (Map.Entry<String, ExternalRequirement> entry : jiraIssues.entrySet()) {
            assertThat(entry.getValue(), notNullValue());
        }
    }

}
