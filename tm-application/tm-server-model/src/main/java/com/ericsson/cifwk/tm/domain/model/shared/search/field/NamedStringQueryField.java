package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Filter;

public class NamedStringQueryField implements QueryField {

    private static final String NAME_PROPERTY = "name";
    private static final String VALUE_PROPERTY = "value";

    private final String property;
    private final String fieldName;

    public NamedStringQueryField(String property, String fieldName) {
        this.property = property;
        this.fieldName = fieldName;
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public Filter toFilter(QueryOperator operator, String value) {
        String preparedValue;
        if (operator == QueryOperator.LIKE_CI) {
            preparedValue = SearchHelpers.toLikePattern(value);
        } else {
            preparedValue = value;
        }
        int filterType = operator.getFilterType();
        return Filter.and(
                Filter.equal(property + "." + NAME_PROPERTY, fieldName),
                new Filter(property + "." + VALUE_PROPERTY, preparedValue, filterType)
        );
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new NamedStringQueryField(mapper.apply(property), fieldName);
    }

}
