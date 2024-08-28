package com.ericsson.cifwk.tm.integration.jira.client.defect.gateway;

import com.ericsson.cifwk.tm.integration.jira.client.AbstractJiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraConfiguration;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;

import javax.inject.Inject;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class MetadataGateway extends AbstractJiraGateway {

    private static final String CREATE_ISSUE_METADATA_PATH = "issue/createmeta";

    private static final String BUG_ISSUE_TYPE = "Bug";

    private static final String EXPANSION_KEYS = "projects.issuetypes.fields";

    @Inject
    public MetadataGateway(JiraConfiguration configuration) throws KeyManagementException, NoSuchAlgorithmException {
        super(configuration);
    }

    public DefectMetadata getDefectMetadata(String projectId) {
        return client
                .path(CREATE_ISSUE_METADATA_PATH)
                .queryParam("projectKeys", projectId)
                .queryParam("issuetypeNames", BUG_ISSUE_TYPE)
                .queryParam("expand", EXPANSION_KEYS)
                .request()
                .get(DefectMetadata.class);
    }

}
