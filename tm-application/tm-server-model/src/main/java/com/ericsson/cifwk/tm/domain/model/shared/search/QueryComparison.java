package com.ericsson.cifwk.tm.domain.model.shared.search;

public class QueryComparison {

    private final QueryOperator operator;
    private final String value;

    public QueryComparison(QueryOperator operator, String value) {
        this.operator = operator;
        this.value = value;
    }

    public QueryOperator getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }

}
