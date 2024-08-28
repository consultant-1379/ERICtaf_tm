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

import com.ericsson.cifwk.tm.integration.jira.client.JiraException;
import com.ericsson.cifwk.tm.integration.jira.client.JiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraIssue;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirementType;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JiraRequirementMapper implements ExternalEntityMapper<JiraIssue, ExternalRequirement> {
    private final Logger logger = LoggerFactory.getLogger(JiraRequirementMapper.class);

    private final JiraFieldNames fieldNames;
    private final JiraGateway jiraGateway;
    private static final String VALUE = "value";
    private static final String INWARD_ISSUE = "inwardIssue";
    private static final String IS_CHILD_OF = "Is a Child of";

    @Inject
    public JiraRequirementMapper(JiraFieldNames fieldNames, JiraGateway jiraGateway) {
        this.fieldNames = fieldNames;
        this.jiraGateway = jiraGateway;
    }

    @Override
    public ExternalRequirement map(JiraIssue issue) {
        if (issue == null) return null;

        ExternalRequirementType type = ExternalRequirementType.fromName(issue.getType().getName());
        switch (type) {
            case MR:
                return traceMR(issue);
            case EPIC:
                return traceEpic(issue);
            case STORY:
                return traceCommonOfType(issue, ExternalRequirementType.STORY);
            case IMPROVEMENT:
                return traceCommonOfType(issue, ExternalRequirementType.IMPROVEMENT);
            case SUBTASK:
                return traceSubtask(issue);
            default: //cut out Bug type
                return null;
        }
    }

    private ExternalRequirement traceEpic(JiraIssue issue) {
        ExternalRequirement.Builder builder = ExternalRequirement.builder(issue.getId(), ExternalRequirementType.EPIC);
        String title = getCustomField(issue, String.class, fieldNames.getEpicName(), issue.getId());
        builder.title(title);
        String delivered = getCustomField(issue, String.class, fieldNames.getDeliveredInSprint(), null);
        builder.delivered(delivered);
        builder.project(issue.getProject().getKey()); // Required in JIRA
        builder.summary(issue.getField(JiraIssue.Field.SUMMARY, String.class, ""));
        builder.statusName(issue.getStatus().getName()); // Required in JIRA
        Collection<Map<String, Map<String, Object>>> parent = issue.getField("issuelinks", Collection.class, null);

        for (String id : getParentLink(parent)) {
            if (addMR(issue, builder, id)) {
                break;
            }
        }
        return builder.build();
    }

    private boolean addMR(JiraIssue issue, ExternalRequirement.Builder builder, String id) {
        ExternalRequirement externalRequirement = fetchExternalRequirement(id);
        if (externalRequirement != null) {
            if (externalRequirement.getProject().equals(issue.getProject().getKey())) {
                builder.parent(externalRequirement);
                return true;
            }
        }
        return false;
    }

    private List<String> getParentLink(Collection<Map<String, Map<String, Object>>> parent) {
        List<String> inwardIssue = Lists.newArrayList();
        if (parent != null) {
            for (Map<String, Map<String, Object>> issueLink : parent) {
                if (issueLink.get(INWARD_ISSUE) != null
                        && issueLink.get("type").get("inward").equals(IS_CHILD_OF)
                        && ((Map<String, Map<String, String>>)issueLink.get(INWARD_ISSUE)
                        .get("fields")).get("issuetype").get("name")
                        .equals(ExternalRequirementType.MR.getName())) {
                    inwardIssue.add(issueLink.get(INWARD_ISSUE).get("key").toString());
                }
            }
        }
        return inwardIssue;
    }

    private ExternalRequirement traceCommonOfType(JiraIssue issue, ExternalRequirementType type) {
        ExternalRequirement.Builder builder = ExternalRequirement.builder(issue.getId(), type);
        builder.title(issue.getId());
        Map<String, Object> delivered = getCustomFieldValue(issue, fieldNames.getDeliveredInSprint());
        if (delivered != null) {
            builder.delivered(delivered.get(VALUE).toString());
        }
        builder.summary(issue.getField(JiraIssue.Field.SUMMARY, String.class, ""));
        builder.project(issue.getProject().getKey()); // Required in JIRA
        builder.statusName(issue.getStatus().getName()); // Required in JIRA

        String epicId = getCustomField(issue, String.class, fieldNames.getEpicLink());
        if (epicId != null) {
            builder.parent(fetchExternalRequirement(epicId));
        }

        return builder.build();
    }

    private ExternalRequirement traceSubtask(JiraIssue issue) {
        ExternalRequirement.Builder builder = ExternalRequirement.builder(issue.getId(),
                ExternalRequirementType.SUBTASK);

        builder.title(issue.getId());
        builder.summary(issue.getField(JiraIssue.Field.SUMMARY, String.class, ""));
        builder.statusName(issue.getStatus().getName()); // Required in JIRA
        Map parent = issue.getField("parent", Map.class, null);
        if (parent != null) {
            String parentId = parent.get("key").toString();
            builder.parent(fetchExternalRequirement(parentId));
        }
        builder.project(issue.getProject().getKey());
        return builder.build();
    }

    private ExternalRequirement traceMR(JiraIssue issue) {
        ExternalRequirement.Builder builder = ExternalRequirement.builder(issue.getId(), ExternalRequirementType.MR);
        String title = issue.getId();
        builder.title(title);
        String delivered = getCustomField(issue, String.class, fieldNames.getDeliveredInSprint(), null);
        builder.delivered(delivered);
        builder.project(issue.getProject().getKey()); // Required in JIRA
        builder.summary(issue.getField(JiraIssue.Field.SUMMARY, String.class, ""));
        builder.statusName(issue.getStatus().getName()); // Required in JIRA
        return builder.build();
    }

    private ExternalRequirement fetchExternalRequirement(String externalId) {
        try {
            JiraIssue issue = jiraGateway.getIssue(externalId);
            return map(issue);
        } catch (JiraException e) {
            logger.error("Exception during Jira call: ", e);
        } catch (NotFoundException e) {
            logger.info("JIRA issue %s not found", externalId);
        }
        return null;
    }

    private <T> T getCustomField(JiraIssue issue, Class<T> type, String name) {
        return getCustomField(issue, type, name, null);
    }

    private Map<String, Object> getCustomFieldValue(JiraIssue issue, String name) {
        return getCustomField(issue, Map.class, name, null);
    }

    private <T> T getCustomField(JiraIssue jiraRequirement, Class<T> type, String name, T defaultValue) {
        Object field = jiraRequirement.getField(name, type, defaultValue);
        if (type.isInstance(field)) {
            return type.cast(field);
        }
        return defaultValue;
    }
}
