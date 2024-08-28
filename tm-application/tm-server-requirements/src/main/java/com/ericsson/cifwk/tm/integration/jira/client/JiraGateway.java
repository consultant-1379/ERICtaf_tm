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

import com.ericsson.cifwk.tm.integration.annotations.Integration;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Integration
public class JiraGateway extends AbstractJiraGateway {

    @Inject
    public JiraGateway(JiraConfiguration configuration) throws KeyManagementException, NoSuchAlgorithmException {
        super(configuration);
    }

    public SearchResult searchIssues(String jql, int pageNumber, int resultsPerPage) throws JiraException {
        return searchIssues(jql, null, pageNumber, resultsPerPage);
    }

    public SearchResult searchIssues(String jql, List<String> fields, int pageNumber, int resultsPerPage)
            throws JiraException {

        try {
            return JiraIssue.search(client, jql, fields, pageNumber, resultsPerPage);
        } catch (IOException | ProcessingException e) {
            throw new JiraException("Exception searching JIRA", e);
        }
    }

    public JiraIssue getIssue(String requirementId) throws JiraException {
        try {
            return JiraIssue.get(client, requirementId);
        } catch (NotFoundException e) {
            throw new JiraException("JIRA issue not found: " + requirementId);
        } catch (IOException | ProcessingException | WebApplicationException e) {
            throw new JiraException("Failed to search for JIRA issues with ID: " + requirementId, e);
        }
    }

    public List<JiraRelease> searchReleases(String projectIdOrKey)
            throws JiraException {

        try {
            return JiraRelease.search(client, projectIdOrKey);
        } catch (IOException | ProcessingException e) {
            throw new JiraException("Failed to search for JIRA releases with: " + projectIdOrKey, e);
        }
    }

    public JiraRelease getRelease(String projectId, String versionId) throws JiraException {
        try {
            return JiraRelease.get(client, projectId, versionId);
        } catch (NotFoundException e) {
            throw new JiraException("JIRA issue not found: versionId=" + versionId + " projectId=" + projectId);
        } catch (IOException | ProcessingException | WebApplicationException e) {
            throw new JiraException("Failed to search for JIRA issues with ID: versionId=" + versionId
                    + " projectId=" + projectId, e);
        }
    }


}
