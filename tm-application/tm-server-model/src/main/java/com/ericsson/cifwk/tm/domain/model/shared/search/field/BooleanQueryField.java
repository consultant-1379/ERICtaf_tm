package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Filter;

public class BooleanQueryField implements QueryField {

    private final String property;

    public BooleanQueryField(String property) {
        this.property = property;
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public Filter toFilter(QueryOperator operator, String value) {
        boolean convertedValue = Boolean.parseBoolean(value);
        if (operator == QueryOperator.LIKE_CI) {
            return new Filter(property, convertedValue, QueryOperator.EQUAL.getFilterType());
        } else {
            return new Filter(property, convertedValue, operator.getFilterType());
        }
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new BooleanQueryField(mapper.apply(property));
    }

}
