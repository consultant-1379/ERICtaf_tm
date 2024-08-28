package com.ericsson.cifwk.tm.integration.jira.client.defect.mapping;

import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.Label;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.responses.CompletionHelper;
import com.google.common.base.Function;

import java.util.List;

public class LabelMapper {

    public CompletionInfo mapLabels(LabelsSearchResult result) {
        String search = result.getSearch();
        List<Label> labels = result.getLabels();
        if (labels == null) {
            return CompletionInfo.empty(search);
        }
        return CompletionHelper.completion(search, labels, new Function<Label, String>() {
            @Override
            public String apply(Label label) {
                return label.getValue();
            }
        });
    }

}
