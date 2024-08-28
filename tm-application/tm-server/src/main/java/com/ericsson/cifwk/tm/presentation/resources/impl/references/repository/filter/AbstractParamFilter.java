/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.filter;

import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.FilterSupplier;
import com.googlecode.genericdao.search.Filter;

import java.util.List;

public abstract class AbstractParamFilter<T> implements ParamFilter<T> {

    protected final String name;
    private final FilterSupplier<T> filterSupplier;

    public AbstractParamFilter(String name, FilterSupplier<T> filterSupplier) {
        this.name = name;
        this.filterSupplier = filterSupplier;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Filter filter(List<T> values) {
        return filterSupplier.supply(values);
    }
}
