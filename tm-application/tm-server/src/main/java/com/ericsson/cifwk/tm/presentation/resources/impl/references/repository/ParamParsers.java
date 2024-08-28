/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references.repository;

import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.filter.ParamFilter;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.util.List;

public final class ParamParsers {

    private ParamParsers() {
    }

    public static <T> List<T> parseAll(final ParamFilter<T> parser, Iterable<String> values) {
        return FluentIterable.from(values)
                .transform(new Function<String, T>() {
                    @Override
                    public T apply(String input) {
                        return parser.parse(input);
                    }
                }).filter(new Predicate<T>() {
                    @Override
                    public boolean apply(T input) {
                        return input != null;
                    }
                }).toList();
    }
}
