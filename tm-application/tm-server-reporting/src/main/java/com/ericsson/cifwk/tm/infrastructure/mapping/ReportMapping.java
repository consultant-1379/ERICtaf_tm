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

import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import java.util.List;
import java.util.Set;

public final class ReportMapping {

    private ReportMapping() {
    }

    public static List<String> mapTitles(Set<ReferenceDataItem> referenceDataItems) {
        return FluentIterable.from(referenceDataItems)
                .transform(new Function<ReferenceDataItem, String>() {
                    @Override
                    public String apply(ReferenceDataItem input) {
                        return input.getTitle();
                    }
                })
                .toList();
    }

    public static String getTitle(ReferenceDataItem referenceDataItem) {
        if (referenceDataItem == null) {
            return "";
        } else {
            return referenceDataItem.getTitle();
        }
    }
}
