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

import com.ericsson.cifwk.tm.integration.jira.mapping.ExternalEntityMapper;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JiraSearchHelper {
    private static final Integer MAX_RESULTS = 20;
    private final Logger logger = LoggerFactory.getLogger(JiraSearchHelper.class);
    private final JiraGateway jiraGateway;


    public JiraSearchHelper(JiraGateway jiraGateway) {
        this.jiraGateway = jiraGateway;
    }

    public <T> Set<T> fetchUpdated(String jql, ExternalEntityMapper<JiraIssue, T> mapper) {
        return fetchUpdated(jql, null, mapper);
    }

    public <T> Set<T> fetchUpdated(String jql, List<String> fields, ExternalEntityMapper<JiraIssue, T> mapper) {
        Set<T> results = Sets.newHashSet();

        SearchResult searchResult = null;

        try {
            //first page
            searchResult = searchIssues(jql, fields, 1);
        } catch (JiraException e) {
            handle(e);
        }

        if (searchResult == null) return results;

        for (JiraIssue jiraIssue : searchResult.getIssues()) {
            addJiraMapping(mapper, results, jiraIssue);
        }

        //other pages
        int pageCount = searchResult.getPageCount();

        for (int i = 2; i < pageCount; i++) {
            try {
                searchResult = searchIssues(jql, fields, i);

                for (JiraIssue jiraIssue : searchResult.getIssues()) {
                    addJiraMapping(mapper, results, jiraIssue);
                }
            } catch (JiraException e) {
                handle(e);
            }
        }

        return results;
    }

    private <T> void addJiraMapping(ExternalEntityMapper<JiraIssue, T> mapper, Set<T> results, JiraIssue jiraIssue) {
        Optional<T> resultMapping = Optional.ofNullable(mapper.map(jiraIssue));
        if (resultMapping.isPresent()) {
            results.add(resultMapping.get());
        }
    }

    private SearchResult searchIssues(String jql, List<String> fields, int page) throws JiraException {
        if (fields != null) {
            return jiraGateway.searchIssues(jql, fields, page, MAX_RESULTS);
        } else {
            return jiraGateway.searchIssues(jql, page, MAX_RESULTS);
        }
    }

    public void handle(JiraException e) {
        if (e.getCause() != null) {
            logger.error("Exception during JIRA call", e);
        } else {
            logger.warn(e.getMessage());
        }
    }

}
