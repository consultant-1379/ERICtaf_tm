package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.googlecode.genericdao.search.Filter;

import java.util.Arrays;
import java.util.List;

public class CompositeQueryField implements QueryField {

    private final List<QueryField> queryFields;

    public CompositeQueryField() {
        queryFields = Lists.newArrayList();
    }

    public CompositeQueryField(QueryField... queryFields) {
        this(Arrays.asList(queryFields));
    }

    public CompositeQueryField(List<QueryField> queryFields) {
        this.queryFields = queryFields;
    }

    public void add(QueryField field) {
        queryFields.add(field);
    }

    @Override
    public String property() {
        return null;
    }

    @Override
    public Filter toFilter(QueryOperator operator, String value) {
        Filter orFilter = Filter.or();
        for (QueryField queryField : queryFields) {
            Filter filter = queryField.toFilter(operator, value);
            if (filter != null) {
                orFilter.add(filter);
            }
        }
        return orFilter;
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        CompositeQueryField compositeQueryField = new CompositeQueryField();
        for (QueryField queryField : queryFields) {
            compositeQueryField.add(queryField.mapProperty(mapper));
        }
        return compositeQueryField;
    }

}
