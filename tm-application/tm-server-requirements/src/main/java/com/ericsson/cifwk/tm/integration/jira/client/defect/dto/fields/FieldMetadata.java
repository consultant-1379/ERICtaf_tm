package com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields;

import java.util.List;
import java.util.Map;

public interface FieldMetadata {

    List<Map<String, Object>> getMetadata();

    String fieldNameToReferenceId(String name);

}
