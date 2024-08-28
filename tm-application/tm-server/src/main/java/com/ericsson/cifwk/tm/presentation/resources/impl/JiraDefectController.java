package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateJiraDefectCommand;
import com.ericsson.cifwk.tm.application.commands.UploadDefectAttachmentsCommand;
import com.ericsson.cifwk.tm.application.queries.jira.MetadataQuerySet;
import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.resources.JiraDefectResource;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class JiraDefectController implements JiraDefectResource {

    @Inject
    private MetadataQuerySet metadataQuerySet;

    @Inject
    private CommandProcessor commandProcessor;

    @Inject
    private CreateJiraDefectCommand createJiraDefectCommand;

    @Inject
    private UploadDefectAttachmentsCommand uploadDefectAttachmentCommand;

    @Override
    public Response getDefectMetadata(String projectId) {
        return metadataQuerySet.getDefectMetadata(projectId);
    }

    @Override
    public Response getLabels(String query) {
        return metadataQuerySet.getLabels(query);
    }

    @Override
    public Response createDefect(Object defectInfo) {
        return commandProcessor.process(createJiraDefectCommand, defectInfo);
    }

    @Override
    public Response uploadAttachments(String issueId, FormDataMultiPart multiPart) {
        JiraDefectAttachmentRequest request = new JiraDefectAttachmentRequest(issueId, multiPart);
        return commandProcessor.process(uploadDefectAttachmentCommand, request);
    }

}
