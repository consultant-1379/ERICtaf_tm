/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration;

import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;

import javax.ws.rs.core.Response;
import java.util.Set;

public interface DefectManagement {
    Integer EXPIRY_HOURS = 4;

    Set<ExternalDefect> fetchUpdated(int hourInterval);

    ExternalDefect fetchById(String externalId);

    LabelsSearchResult getLabels(String query);

    Response createDefect(Object defectInfo);

    Response uploadAttachments(JiraDefectAttachmentRequest request);

    DefectMetadata getDefectMetadata(String projectId);

    Set<ExternalDefect> fetchUpdated(String jql);
}
