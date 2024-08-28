package com.ericsson.cifwk.tm.domain.model.shared.search.field;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.google.common.base.Function;

public class NameWithIdQueryField<T extends Enum<T> & NamedWithId<Integer>>
        extends AbstractEnumQueryField<T> {

    public NameWithIdQueryField(String property, Class<T> enumClass) {
        super(property, enumClass);
    }

    @Override
    protected Object normalizeConstant(T constant) {
        return constant.getId();
    }

    @Override
    public QueryField mapProperty(Function<String, String> mapper) {
        return new NameWithIdQueryField<>(mapper.apply(property), enumClass);
    }
}
