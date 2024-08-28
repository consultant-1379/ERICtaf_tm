package com.ericsson.cifwk.tm.integration.jira.client.defect.mapping;

import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.MetadataReference;
import com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields.FieldMetadata;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class MetadataMapper {

    private static final String NAME_KEY = "name";
    private static final String VALUE_KEY = "value";
    private static final String ID_KEY = "id";
    private static final String SCHEMA_KEY = "schema";
    private static final String ALLOWED_VALUES_KEY = "allowedValues";
    private static final String TYPE_KEY = "type";
    private static final String FIELD_NAME = "fieldName";
    private static final String PROJECT = "project";
    private static final String REQUIRED = "required";

    public List<ReferenceData> mapFieldMetadata(FieldMetadata fields) {
        List<ReferenceData> mappedFields = Lists.newArrayList();
        for (Map<String, Object> fieldMetadata: fields.getMetadata()) {
            String fieldName = getFieldName(fieldMetadata);
            String referenceId = fields.fieldNameToReferenceId(fieldName);
            String type = getFieldType(fieldMetadata);
            String referenceFieldName = getReferencedFieldName(fieldMetadata);
            boolean required = getRequiredFields(fieldMetadata);
            List<ReferenceDataItem> items = mapAllowedValues(getAllowedValues(fieldMetadata));
            mappedFields.add(new MetadataReference(referenceId, items, type, referenceFieldName, fieldName, required));
        }
        return mappedFields;
    }

    public ReferenceData mapProjectData(Map<String, Object> project) {
        ReferenceData referenceData = new ReferenceData(PROJECT);
        for (Map.Entry<String, Object> data: project.entrySet()) {
            ReferenceDataItem referenceDataItem = new ReferenceDataItem(data.getKey(), data.getValue().toString());
            referenceData.getItems().add(referenceDataItem);
        }

        return referenceData;
    }

    private String getFieldName(Map<String, Object> fieldMetadata) {
        return (String) fieldMetadata.get(NAME_KEY);
    }

    private String getFieldType(Map<String, Object> fieldMetadata) {
        Map<String, String> schema = (Map<String, String>) fieldMetadata.get(SCHEMA_KEY);
        return schema.get(TYPE_KEY);
    }

    private String getReferencedFieldName(Map<String, Object> fieldMetadata) {
        return (String) fieldMetadata.get(FIELD_NAME);
    }

    private boolean getRequiredFields(Map<String, Object> fieldMetadata) {
        return (boolean) fieldMetadata.get(REQUIRED);
    }

    private List<Map<String, Object>> getAllowedValues(Map<String, Object> fieldMetaData) {
        return (List<Map<String, Object>>) fieldMetaData.get(ALLOWED_VALUES_KEY);
    }

    private List<ReferenceDataItem> mapAllowedValues(List<Map<String, Object>> allowedValues) {
        List<ReferenceDataItem> normalized = Lists.newArrayList();
        if (allowedValues == null) {
            return normalized;
        }
        for (Map<String, Object> entry: allowedValues) {
            String name = (String) entry.get(NAME_KEY);
            String value = (String) entry.get(VALUE_KEY);
            String id = (String) entry.get(ID_KEY);
            ReferenceDataItem referenceDataItem;

            if (name == null) {
                referenceDataItem = new ReferenceDataItem(id, value);
            } else {
                referenceDataItem = new ReferenceDataItem(id, name);
            }
            normalized.add(referenceDataItem);
        }
        return normalized;
    }

}
