/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

public final class Sanitization {

    private Sanitization() {
    }

    public static final String COLLECTION_SEPARATOR = ",";

    public static Set<String> normalizeCommaSeparated(Collection<String> elements) {
        String allElements = Joiner.on(COLLECTION_SEPARATOR).join(elements);
        return Sets.newHashSet(
                Splitter.on(COLLECTION_SEPARATOR)
                        .trimResults()
                        .omitEmptyStrings()
                        .split(allElements));
    }

    public static <T> Iterable<T> splitCommaSeparated(String commaSeparated, Function<String, T> transformer) {
        Iterable<String> strings = splitCommaSeparated(commaSeparated);
        return FluentIterable.from(strings)
                .transform(transformer)
                .filter(new Predicate<T>() {
                    @Override
                    public boolean apply(T input) {
                        return input != null;
                    }
                });
    }

    public static Iterable<String> splitCommaSeparated(String commaSeparated) {
        return Splitter.on(COLLECTION_SEPARATOR)
                .trimResults()
                .omitEmptyStrings()
                .split(Strings.nullToEmpty(commaSeparated));
    }


    public static Iterable<Long> splitCommaSeparatedIds(String commaSeparated) {
        return splitCommaSeparated(commaSeparated, new Function<String, Long>() {
            @Override
            public Long apply(String input) {
                try {
                    return Long.parseLong(input);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });
    }

}
