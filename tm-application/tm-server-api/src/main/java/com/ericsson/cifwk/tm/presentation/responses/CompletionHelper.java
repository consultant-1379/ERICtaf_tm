/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.responses;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.CompletionItemInfo;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public final class CompletionHelper {

    private CompletionHelper() {
    }

    public static <T> CompletionInfo completion(
            String search,
            List<T> results,
            Function<T, String> mapper) {
        return completion(search, results, mapper, mapper);
    }

    public static <T> CompletionInfo completion(
            String search,
            List<T> results,
            Function<T, String> keyMapper,
            Function<T, String> valueMapper) {
        Map<T, T> pairs = Maps.toMap(results, Functions.<T>identity());
        return completion(search, pairs, keyMapper, valueMapper);
    }

    public static <K, V> CompletionInfo completion(
            String search,
            Map<K, V> results,
            Function<K, String> keyMapper,
            Function<V, String> valueMapper) {
        List<CompletionItemInfo> completions = Lists.newArrayList();
        for (Map.Entry<K, V> entry : results.entrySet()) {
            CompletionItemInfo completionItemInfo = new CompletionItemInfo();
            completionItemInfo.setValue(keyMapper.apply(entry.getKey()));
            completionItemInfo.setName(valueMapper.apply(entry.getValue()));
            completions.add(completionItemInfo);
        }
        CompletionInfo completionInfo = new CompletionInfo();
        completionInfo.setSearch(search);
        completionInfo.setItems(completions);
        return completionInfo;
    }

    public static <T extends NamedWithId> CompletionInfo completion(
            String search,
            List<T> references) {
        return completion(
                search,
                references,
                new Function<T, String>() {
                    @Override
                    public String apply(T input) {
                        return input.getId().toString();
                    }
                }, new Function<T, String>() {
                    @Override
                    public String apply(T input) {
                        return input.getName();
                    }
                }
        );
    }

}
