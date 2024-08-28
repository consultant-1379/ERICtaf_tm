package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Filter;

public interface QueryField {
    String property();

    Filter toFilter(QueryOperator operator, String value);

    QueryField mapProperty(Function<String, String> mapper);
}
