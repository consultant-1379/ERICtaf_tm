package com.ericsson.cifwk.tm.presentation.dto;

public class CriterionInfo {

    private String field;
    private String operator;
    private String value;
    private String type;

    public CriterionInfo() {
    }

    public CriterionInfo(String field, String operator, String value, String type) {
        this.field = field;
        this.operator = operator;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
