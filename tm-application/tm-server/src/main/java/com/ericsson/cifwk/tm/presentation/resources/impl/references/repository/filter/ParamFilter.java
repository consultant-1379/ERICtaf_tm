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

import com.googlecode.genericdao.search.Filter;

import java.util.List;

public interface ParamFilter<T> {

    /**
     * Name of parameter.
     */
    String name();

    T parse(String value);

    Filter filter(List<T> values);

}
