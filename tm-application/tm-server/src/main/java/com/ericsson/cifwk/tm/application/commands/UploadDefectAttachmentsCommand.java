package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.integration.DefectManagement;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class UploadDefectAttachmentsCommand implements Command<JiraDefectAttachmentRequest> {

    @Inject
    private DefectManagement defectManagement;

    @Override
    public Response apply(JiraDefectAttachmentRequest input) {
        return defectManagement.uploadAttachments(input);
    }

}
