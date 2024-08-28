/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration;

import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RequirementManagement {
    Integer MAX_RESULTS = 20;
    Integer EXPIRY_HOURS = 4;

    ExternalRequirement fetchById(String requirementId);

    Map<String, ExternalRequirement> fetchById(Collection<String> requirementIds);

    Set<ExternalRequirement> fetchUpdated(int hourInterval);

    Set<ExternalRequirement> fetchUpdated(String jql);
}
