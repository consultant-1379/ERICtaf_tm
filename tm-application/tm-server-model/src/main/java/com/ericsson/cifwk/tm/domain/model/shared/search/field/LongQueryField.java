/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.google.common.base.Function;
import com.google.common.primitives.Longs;
import com.googlecode.genericdao.search.Filter;

public class LongQueryField implements QueryField {

    private final String property;

    public LongQueryField(String property) {
        this.property = property;
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public Filter toFilter(QueryOperator operator, String value) {
        Long parsedValue = Longs.tryParse(value);
        if (parsedValue == null) {
            return null;
        }
        if (operator == QueryOperator.LIKE_CI) {
            return new Filter(property, parsedValue, QueryOperator.EQUAL.getFilterType());
        }
        return new Filter(property, parsedValue, operator.getFilterType());
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new StringQueryField(mapper.apply(property));
    }
}
