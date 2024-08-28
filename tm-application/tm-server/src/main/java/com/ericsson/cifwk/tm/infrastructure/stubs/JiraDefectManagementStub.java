/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.stubs;

import com.ericsson.cifwk.tm.application.requests.JiraDefectAttachmentRequest;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.Label;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefect;
import com.ericsson.cifwk.tm.integration.jira.dto.ExternalDefectType;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JiraDefectManagementStub implements DefectManagement {

    public static final String BUG_1 = "WWCT-42";
    public static final String BUG_2 = "TOR-193";
    public static final String BUG_3 = "TORRV-3879";
    public static final String BUG_4 = "TORRV-3873";
    public static final String BUG_5 = "TORRV-3868";

    public static final String BUG_1_DESCRIPTION = "ARNE Imports Failing throwing Null Pointer Exception";
    public static final String BUG_2_DESCRIPTION =
            "DDC not collection all relevant stats on atrcxb2597-2598ossfs Deployment";
    public static final String BUG_3_DESCRIPTION = "Modify Sync test due to model/cli command changes.";
    public static final String BUG_4_DESCRIPTION = "Changes to model cause DV4 tests to fail.";
    public static final String BUG_5_DESCRIPTION =
            "Bug: User Create/Delete has changed, causing all tests to fail on DS4";

    private Map<String, ExternalDefect> defects = Maps.newHashMap();

    public JiraDefectManagementStub() {
        defects.put(BUG_1, ExternalDefect.builder(BUG_1, ExternalDefectType.BUG)
                .title(BUG_1)
                .summary(BUG_1_DESCRIPTION)
                .build());

        defects.put(BUG_2, ExternalDefect.builder(BUG_2, ExternalDefectType.BUG)
                .title(BUG_2)
                .summary(BUG_2_DESCRIPTION)
                .build());

        defects.put(BUG_3, ExternalDefect.builder(BUG_3, ExternalDefectType.BUG)
                .title(BUG_3)
                .summary(BUG_3_DESCRIPTION)
                .build());

        defects.put(BUG_4, ExternalDefect.builder(BUG_4, ExternalDefectType.BUG)
                .title(BUG_4)
                .summary(BUG_4_DESCRIPTION)
                .build());

        defects.put(BUG_5, ExternalDefect.builder(BUG_5, ExternalDefectType.BUG)
                .title(BUG_5)
                .summary(BUG_5_DESCRIPTION)
                .build());
    }

    @Override
    public LabelsSearchResult getLabels(String query) {
        LabelsSearchResult labelsSearchResult = new LabelsSearchResult();
        List<Label> labels = Lists.newArrayList();

        Label label1 = new Label();
        label1.setValue("15B");
        labels.add(label1);

        Label label2 = new Label();
        label2.setValue("16A");
        labels.add(label2);

        Label label3 = new Label();
        label3.setValue("16B");
        labels.add(label3);

        labelsSearchResult.setLabels(labels);

        return labelsSearchResult;
    }

    @Override
    public Response createDefect(Object defectInfo) {
        Map<String, String> defect = new HashMap<>();
        defect.put("key", "DURACI-3078");
        return Responses.created(defect);
    }

    @Override
    public Response uploadAttachments(JiraDefectAttachmentRequest request) {
        Map<String, String> obj = new HashMap<>();
        obj.put("IssueId", request.getIssueId());
        return Responses.created(obj);
    }

    @Override
    public DefectMetadata getDefectMetadata(String projectId) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            InputStream metaData = getClass().getClassLoader().getResourceAsStream("jira/jiraMetaData.json");
            DefectMetadata defectData = mapper.readValue(metaData, DefectMetadata.class);
            return defectData;

        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public Set<ExternalDefect> fetchUpdated(String jql) {
        return Sets.newHashSet(defects.values());
    }

    @Override
    public Set<ExternalDefect> fetchUpdated(int hourInterval) {
        return Sets.newHashSet(defects.values());
    }

    @Override
    public ExternalDefect fetchById(String externalId) {
        return defects.get(externalId);
    }
}
