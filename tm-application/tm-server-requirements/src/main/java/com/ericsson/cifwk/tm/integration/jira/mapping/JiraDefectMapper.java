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

import java.util.Map;

public class JiraDefectMapper implements ExternalEntityMapper<JiraIssue, ExternalDefect> {

    private static final String VALUE = "value";

    @Override
    public ExternalDefect map(JiraIssue issue) {
        if (issue == null) return null;

        ExternalDefectType type = ExternalDefectType.fromName(issue.getType().getName());
        switch (type) {
            case BUG:
                return assembleDefect(issue);
            case TR:
                return assembleDefect(issue);
            case SUPPORT:
                return assembleDefect(issue);
            default: //cut out other types
                return null;
        }
    }

    private ExternalDefect assembleDefect(JiraIssue issue) {
        ExternalDefect.Builder builder = ExternalDefect.builder(issue.getId(), ExternalDefectType.BUG);

        builder.title(issue.getId());
        Map<String, Object> delivered = issue.getField(JiraIssue.Field.DELIVERED_IN.getName(), Map.class, null);
        if (delivered != null) {
            builder.deliveredIn(delivered.get(VALUE).toString());
        }
        builder.summary(issue.getField(JiraIssue.Field.SUMMARY, String.class, ""));
        builder.project(issue.getProject().getKey()); // Required in JIRA
        if (issue.getStatus() != null) {
            builder.statusName(issue.getStatus().getName()); // Required in JIRA
        }
        return builder.build();
    }
}
