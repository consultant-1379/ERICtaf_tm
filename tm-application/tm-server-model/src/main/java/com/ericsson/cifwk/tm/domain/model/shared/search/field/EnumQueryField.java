package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.google.common.base.Function;

public class EnumQueryField<T extends Enum<T>> extends AbstractEnumQueryField<T> {

    public EnumQueryField(String property, Class<T> enumClass) {
        super(property, enumClass);
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new EnumQueryField<>(mapper.apply(property), enumClass);
    }

}
