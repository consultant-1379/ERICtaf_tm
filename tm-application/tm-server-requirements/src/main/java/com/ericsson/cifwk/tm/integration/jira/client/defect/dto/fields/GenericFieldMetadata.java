package com.ericsson.cifwk.tm.integration.jira.client.defect.dto.fields;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericFieldMetadata implements FieldMetadata {

    private List<Map<String, Object>> fieldList = Lists.newArrayList();

    private static final String NAME = "name";
    private static final String FIELD_NAME = "fieldName";
    private static final String REQUIRED = "required";
    private static final String CUSTOM_FIELD = "customfield";

    private static Map<String, String> fieldNameReferenceIdMap = createFieldNameReferenceIdMap();

    @Override
    public String fieldNameToReferenceId(String name) {
        return fieldNameReferenceIdMap.get(name);
    }

    @JsonAnySetter
    public void setAny(String propertyName, Map<String, Object> properties) {
        String name = properties.get(NAME).toString();
        if (fieldNameReferenceIdMap.containsKey(name)) {
            properties.put(FIELD_NAME, propertyName);
            fieldList.add(properties);
        }

        if ((boolean) properties.get(REQUIRED) && propertyName.contains(CUSTOM_FIELD) &&
                !fieldNameReferenceIdMap.containsKey(name)) {
            properties.put(FIELD_NAME, propertyName);
            if (!fieldNameReferenceIdMap.containsKey(name)) {
                fieldNameReferenceIdMap.put(name, name);
            }
            fieldList.add(properties);
        }
    }

    @Override
    public List<Map<String, Object>> getMetadata() {
        return fieldList;
    }

    private static Map<String, String> createFieldNameReferenceIdMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Component/s", "components");
        map.put("Fix Version/s", "fixVersions");
        map.put("Team Name", "teamName");
        map.put("Found In Release", "foundInRelease");
        map.put("Found in Sprint", "foundInSprint");
        map.put("Delivered in Sprint", "deliveredInSprint");
        map.put("Description", "description");

        return map;
    }

}
