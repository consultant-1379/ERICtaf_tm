package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.domain.model.shared.search.QueryOperator;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.googlecode.genericdao.search.Filter;

import java.util.List;
import java.util.Map;

import static com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers.failFilter;

public abstract class AbstractEnumQueryField<T extends Enum<T>> implements QueryField {

    protected final String property;
    protected final Class<T> enumClass;
    protected final Map<String, T> constants;

    public AbstractEnumQueryField(String property, Class<T> enumClass) {
        this.property = property;
        this.enumClass = enumClass;
        constants = Maps.newHashMap();
        for (T enumConstant : enumClass.getEnumConstants()) {
            String name = enumConstant.toString().toUpperCase();
            constants.put(name, enumConstant);
        }
    }

    @Override
    public String property() {
        return property;
    }

    @Override
    public Filter toFilter(QueryOperator operator, final String value) {
        if (QueryOperator.LIKE_CI.equals(operator)) {
            return likeFilter(value);
        }
        T constant = constants.get(value.toUpperCase());
        if (constant == null) {
            return illegalValueFilter(operator);
        }
        Object normalValue = normalizeConstant(constant);
        if (QueryOperator.NOT_EQUAL.equals(operator)) {
            return notEqualFilter(normalValue);
        }
        return new Filter(property, normalValue, operator.getFilterType());
    }

    private Filter likeFilter(String value) {
        List<?> matchedConstants = getLikeMatches(value);
        if (matchedConstants.isEmpty()) {
            return failFilter();
        }
        return Filter.in(property, matchedConstants);
    }

    private Filter illegalValueFilter(QueryOperator operator) {
        if (QueryOperator.EQUAL.equals(operator)) {
            // Handle illegal enum value gracefully
            return failFilter();
        } else {
            return null;
        }
    }

    private Filter notEqualFilter(Object normalValue) {
        return Filter.or(Filter.isNull(property), Filter.notEqual(property, normalValue));
    }

    private List<?> getLikeMatches(final String value) {
        return FluentIterable.from(constants.keySet())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean apply(String input) {
                        return input.toUpperCase().contains(value.toUpperCase());
                    }
                })
                .transform(new Function<String, Object>() {
                    @Override
                    public Object apply(String name) {
                        return normalizeConstant(constants.get(name));
                    }
                })
                .toList();
    }

    protected Object normalizeConstant(T constant) {
        return constant;
    }

}
