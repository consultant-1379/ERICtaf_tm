package com.ericsson.cifwk.tm.integration.jira.client.defect.gateway;

import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.integration.jira.client.AbstractJiraGateway;
import com.ericsson.cifwk.tm.integration.jira.client.JiraConfiguration;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import org.glassfish.jersey.media.multipart.MultiPart;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class DefectGateway extends AbstractJiraGateway {

    @Inject
    public DefectGateway(JiraConfiguration configuration) throws KeyManagementException, NoSuchAlgorithmException {
        super(configuration);
    }

    public Response createDefect(Object defectInfo) {
        Response response = client
                .path("issue")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(defectInfo));

        return Responses.created(response.readEntity(String.class));
    }

    public Response uploadAttachments(JiraDefectAttachmentRequest request) {
        MultiPart multiPart = request.getMultiPart();

        Response response = client
                .path("issue/" + request.getIssueId() + "/attachments")
                .request()
                .header("X-Atlassian-Token", "nocheck")
                .post(Entity.entity(multiPart, multiPart.getMediaType()));

        return response;
    }

}
