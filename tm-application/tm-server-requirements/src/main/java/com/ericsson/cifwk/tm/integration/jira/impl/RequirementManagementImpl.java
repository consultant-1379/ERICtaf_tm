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

import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.integration.jira.client.JiraException;
import com.ericsson.cifwk.tm.integration.jira.client.JiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraIssue;
import com.ericsson.cifwk.tm.integration.jira.client.JiraSearchHelper;
import com.ericsson.cifwk.tm.integration.jira.client.SearchResult;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalRequirement;
import com.ericsson.cifwk.tm.integration.jira.dto.ValidRequirementTypes;
import com.ericsson.cifwk.tm.integration.jira.mapping.JiraRequirementMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.lang.String.format;

@Integration
public class RequirementManagementImpl implements RequirementManagement {

    private static final Pattern JIRA_ID = Pattern.compile("[A-Z]+-[0-9]+");
    private final JiraSearchHelper jiraSearchHelper;
    private final JiraGateway jiraGateway;
    private final JiraRequirementMapper mapper;

    @Inject
    public RequirementManagementImpl(JiraGateway jiraGateway, JiraRequirementMapper mapper) {
        this.jiraGateway = jiraGateway;
        this.jiraSearchHelper = new JiraSearchHelper(jiraGateway);
        this.mapper = mapper;
    }

    @Override
    public ExternalRequirement fetchById(String externalId) {
        if (externalId == null) return null;

        try {
            JiraIssue issue = jiraGateway.getIssue(externalId);
            return mapper.map(issue);
        } catch (JiraException e) {
            jiraSearchHelper.handle(e);
        }

        return null;
    }

    @Override
    public Map<String, ExternalRequirement> fetchById(
            Collection<String> externalIds) {

        Map<String, ExternalRequirement> result = Maps.newHashMap();

        FluentIterable<String> validExternalIds = FluentIterable
                .from(externalIds)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean apply(String externalId) {
                        return validJiraIssueId(externalId);
                    }
                });

        if (!validExternalIds.isEmpty()) {
            for (List<String> missingIssuesChunk : Iterables.partition(validExternalIds, MAX_RESULTS)) {
                populateFromJiraBatch(result, missingIssuesChunk);
            }
        }

        return result;
    }

    @Override
    public Set<ExternalRequirement> fetchUpdated(int hourInterval) {
        String jql = format("updated>=-%dh and (%s)", hourInterval, ValidRequirementTypes.toJqlString());
        return jiraSearchHelper.fetchUpdated(jql, mapper);
    }

    @Override
    public Set<ExternalRequirement> fetchUpdated(String jql) {
        String jqlFormatted = format(jql + " and (%s)", ValidRequirementTypes.toJqlString());
        return jiraSearchHelper.fetchUpdated(jqlFormatted, mapper);
    }

    private void populateFromJiraBatch(Map<String, ExternalRequirement> resultBag, List<String> missingIssues) {
        String jql = toJqlString(missingIssues);

        try {
            SearchResult searchResult = jiraGateway.searchIssues(jql, 1, MAX_RESULTS);

            for (JiraIssue req : searchResult.getIssues()) {
                resultBag.put(req.getId(), mapper.map(req));
            }
        } catch (JiraException e) {
            jiraSearchHelper.handle(e);
        } catch (BadRequestException e) { // Exception if missing external Id not exists in JIRA
            searchByOne(resultBag, missingIssues);
        }
    }

    private void searchByOne(Map<String, ExternalRequirement> resultBag, List<String> missingIssues) {
        for (String missingIssue : missingIssues) {
            try {
                JiraIssue req = jiraGateway.getIssue(missingIssue);
                String type = req.getType().getName();
                if (ValidRequirementTypes.isValid(type)) {
                    resultBag.put(req.getId(), mapper.map(req));
                }
            } catch (JiraException e) {
                jiraSearchHelper.handle(e);
            }
        }
    }

    private String toJqlString(Collection<String> missingIssues) {
        return format("id=%s and (%s)", Joiner.on(" or id=").join(missingIssues), ValidRequirementTypes.toJqlString());
    }

    protected boolean validJiraIssueId(String issueId) {
        return (issueId != null) && JIRA_ID.matcher(issueId).matches();
    }
}
