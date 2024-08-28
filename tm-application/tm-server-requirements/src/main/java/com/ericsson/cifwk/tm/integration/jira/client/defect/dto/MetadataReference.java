package com.ericsson.cifwk.tm.integration.jira.client.defect.dto;

import com.ericsson.cifwk.tm.presentation.dto.ReferenceData;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;

import java.util.List;

public class MetadataReference extends ReferenceData {

    protected String type;
    protected String fieldName;
    protected String label;
    protected boolean required;


    public MetadataReference(String id, List<ReferenceDataItem> items, String type, String fieldName,
                             String label, boolean required) {
        super(id, items);
        this.type = type;
        this.fieldName = fieldName;
        this.label = label;
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getLabel() {
        return label;
    }

    public boolean getRequired() {
        return required;
    }
}
