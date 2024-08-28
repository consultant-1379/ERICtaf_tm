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

import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.annotations.Integration;
import com.ericsson.cifwk.tm.integration.jira.client.JiraException;
import com.ericsson.cifwk.tm.integration.jira.client.JiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraIssue;
import com.ericsson.cifwk.tm.integration.jira.client.JiraSearchHelper;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;
import com.ericsson.cifwk.tm.integration.jira.client.defect.gateway.DefectGateway;
import com.ericsson.cifwk.tm.integration.jira.client.defect.gateway.LabelGateway;
import com.ericsson.cifwk.tm.integration.jira.client.defect.gateway.MetadataGateway;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.ericsson.cifwk.tm.integration.jira.mapping.JiraDefectMapper;
import com.ericsson.cifwk.tm.integration.jira.mapping.JiraFieldNames;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Integration
public class DefectManagementImpl implements DefectManagement {

    private final JiraGateway jiraGateway;
    private final JiraDefectMapper mapper;
    private final JiraSearchHelper jiraSearchHelper;
    private final DefectGateway defectGatway;
    private final LabelGateway labelGateway;
    private final MetadataGateway metaDetaGateway;

    @Inject
    private JiraFieldNames jiraFieldNames;

    @Inject
    public DefectManagementImpl(JiraGateway jiraGateway, JiraDefectMapper mapper, DefectGateway defectGatway,
                                LabelGateway labelGateway, MetadataGateway metaDetaGateway) {
        this.jiraGateway = jiraGateway;
        this.jiraSearchHelper = new JiraSearchHelper(jiraGateway);
        this.mapper = mapper;
        this.defectGatway = defectGatway;
        this.labelGateway = labelGateway;
        this.metaDetaGateway = metaDetaGateway;
    }

    @Override
    public ExternalDefect fetchById(String externalId) {
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
    public Set<ExternalDefect> fetchUpdated(int hourInterval) {
        String jql = String.format("updated>=-%dh and (issuetype = Bug or issuetype = TR or issuetype = SUPPORT)", hourInterval);
        return jiraSearchHelper.fetchUpdated(jql, getDefectFields(), mapper);
    }

    @Override
    public Response createDefect(Object defectInfo) {
        return this.defectGatway.createDefect(defectInfo);
    }

    @Override
    public Response uploadAttachments(JiraDefectAttachmentRequest request) {
        return this.defectGatway.uploadAttachments(request);
    }

    @Override
    public DefectMetadata getDefectMetadata(String projectId) {
        return this.metaDetaGateway.getDefectMetadata(projectId);
    }

    @Override
    public Set<ExternalDefect> fetchUpdated(String jql) {
        String formattedJql = String.format(jql + " and (issuetype = Bug or issuetype = TR or issuetype = SUPPORT)");
        return jiraSearchHelper.fetchUpdated(formattedJql, getDefectFields(), mapper);
    }

    @Override
    public LabelsSearchResult getLabels(String query) {
        return this.labelGateway.getLabels(query);
    }

    private List<String> getDefectFields() {
        return Lists.newArrayList(
                "summary",
                "project",
                "description",
                "issuetype",
                jiraFieldNames.getDeliveredInSprint() // Delivered in
        );
    }
}
