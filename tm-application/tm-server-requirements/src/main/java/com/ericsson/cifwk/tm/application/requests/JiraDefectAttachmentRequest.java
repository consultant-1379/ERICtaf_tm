package com.ericsson.cifwk.tm.application.requests;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

public class JiraDefectAttachmentRequest {

    private String issueId;

    private FormDataMultiPart multiPart;

    public JiraDefectAttachmentRequest(String issueId, FormDataMultiPart multiPart) {
        this.issueId = issueId;
        this.multiPart = multiPart;
    }

    public String getIssueId() {
        return issueId;
    }

    public FormDataMultiPart getMultiPart() {
        return multiPart;
    }
}
