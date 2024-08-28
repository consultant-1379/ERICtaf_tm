/*******************************************************************************
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 ******************************************************************************/

package com.ericsson.cifwk.tm.infrastructure.stubs;


import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirementType;
import com.ericsson.cifwk.tm.test.TestRequirements;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.ericsson.cifwk.tm.test.TestRequirements.*;

public class JiraRequirementManagementStub implements RequirementManagement {


    private final TestRequirements testRequirements;

    public JiraRequirementManagementStub() {
        testRequirements = new TestRequirements();
    }

    @Override
    public ExternalRequirement fetchById(String requirementId) {
        if (NON_EXISTING.equals(requirementId)) {
            return null;
        }
        ExternalRequirement externalRequirement = testRequirements.get(requirementId);
        return externalRequirement == null ? getDefaultIssue(requirementId) : externalRequirement;
    }

    @Override
    public Set<ExternalRequirement> fetchUpdated(int hourInterval) {
        Set<ExternalRequirement> result = Sets.newLinkedHashSet();

        result.add(testRequirements.get(EPIC1));
        result.add(testRequirements.get(SUBTASK1));
        result.add(testRequirements.get(STORY1));
        result.add(testRequirements.get(EPIC2));

        return result;
    }

    @Override
    public Set<ExternalRequirement> fetchUpdated(String jql) {
        Set<ExternalRequirement> result = Sets.newLinkedHashSet();

        result.add(testRequirements.get(EPIC1));
        result.add(testRequirements.get(SUBTASK1));
        result.add(testRequirements.get(STORY1));
        result.add(testRequirements.get(EPIC2));

        return result;
    }

    @Override
    public Map<String, ExternalRequirement> fetchById(final Collection<String> requirementIds) {
        HashMap<String, ExternalRequirement> result = Maps.newHashMap();
        for (String issueId : requirementIds) {
            if (NON_EXISTING.equals(issueId)) {
                continue;
            }
            ExternalRequirement externalRequirement = testRequirements.get(issueId);
            if (externalRequirement != null) {
                result.put(issueId, externalRequirement);
            } else {
                result.put(issueId, getDefaultIssue(issueId));
            }
        }

        return result;
    }

    private ExternalRequirement getDefaultIssue(String issueId) {
        return ExternalRequirement.builder(issueId, ExternalRequirementType.EPIC)
                .title("Default Title " + issueId)
                .summary("Default Summary " + issueId)
                .project("Default Project").build();
    }
}
