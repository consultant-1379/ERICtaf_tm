package com.ericsson.cifwk.tm.application.queries.jira;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.integration.DefectManagement;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.DefectMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields.FieldMetadata;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.labels.LabelsSearchResult;
import com.ericsson.cifwk.tm.integration.jira.client.defect.mapping.LabelMapper;
import com.ericsson.cifwk.tm.integration.jira.client.defect.mapping.MetadataMapper;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@QuerySet
public class MetadataQuerySet {

    @Inject
    private MetadataMapper metadataMapper;

    @Inject
    private DefectManagement defectManagement;

    @Inject
    private LabelMapper labelMapper;

    public Response getDefectMetadata(String projectId) {
        DefectMetadata metadata = defectManagement.getDefectMetadata(projectId);
        FieldMetadata fieldMetadata = metadata.getFields();
        Map<String, Object> project = metadata.getProject();
        if (fieldMetadata == null) {
            return Responses.nullable(fieldMetadata);
        } else {
            List<ReferenceData> mapped = metadataMapper.mapFieldMetadata(fieldMetadata);
            mapped.add(metadataMapper.mapProjectData(project));
            return Responses.ok(mapped);
        }
    }

    public Response getLabels(String query) {
        LabelsSearchResult result = defectManagement.getLabels(query);
        CompletionInfo mapped = labelMapper.mapLabels(result);
        return Responses.ok(mapped);
    }

}
