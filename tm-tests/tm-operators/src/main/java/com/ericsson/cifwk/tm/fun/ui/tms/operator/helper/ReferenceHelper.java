/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper;

import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references.Identifiable;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.base.Preconditions;

public final class ReferenceHelper {

    private ReferenceHelper() {
    }

    public static <T extends Identifiable> T parseEnumById(String id, Class<T> referenceClass) {
        Preconditions.checkArgument(referenceClass.isEnum());
        if (id == null) {
            return null;
        }
        for (T value : referenceClass.getEnumConstants()) {
            if (id.equals(value.getId())) {
                return value;
            }
        }
        return null;
    }

    public static <T extends ReferenceDataItem> T parseEnumByTitle(String title, Class<T> referenceClass) {
        Preconditions.checkArgument(referenceClass.isEnum());
        if (title == null) {
            return null;
        }
        String normalizedTitle = title.replace("_", " "); // To normalize enum constant titles
        for (T value : referenceClass.getEnumConstants()) {
            if (normalizedTitle.equalsIgnoreCase(value.getTitle())) {
                return value;
            }
        }
        return null;
    }

}
