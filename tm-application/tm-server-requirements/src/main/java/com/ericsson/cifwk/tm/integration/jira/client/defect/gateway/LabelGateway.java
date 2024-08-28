package com.ericsson.cifwk.tm.integration.jira.client.defect.gateway;

import com.ericsson.cifwk.tm.integration.jira.client.AbstractJiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraConfiguration;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;

import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class LabelGateway extends AbstractJiraGateway {

    private static final String LABELS_SEARCH = "labels/suggest";


    @Inject
    public LabelGateway(JiraConfiguration configuration) throws KeyManagementException, NoSuchAlgorithmException {
        super(configuration);

        client = getClientBuilder()
                .target(configuration.getUri() + configuration.getApi1MountPoint());
    }

    public LabelsSearchResult getLabels(String query) {
        return client
                .path(LABELS_SEARCH)
                .queryParam("query", query)
                .request()
                .get(LabelsSearchResult.class);
    }

}
