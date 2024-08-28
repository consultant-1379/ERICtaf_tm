package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Filter;

public class StringQueryField implements QueryField {

    private final String property;

    public StringQueryField(String property) {
        this.property = property;
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public Filter toFilter(QueryOperator operator, String value) {
        if (operator == QueryOperator.LIKE_CI) {
            return Filter.ilike(property, SearchHelpers.toLikePattern(value));
        } else {
            return new Filter(property, value, operator.getFilterType());
        }
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new StringQueryField(mapper.apply(property));
    }

}
