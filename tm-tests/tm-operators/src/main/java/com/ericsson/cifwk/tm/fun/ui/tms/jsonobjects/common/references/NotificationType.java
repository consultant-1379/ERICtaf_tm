/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references;

import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;

public enum NotificationType {
    INFO(new ReferenceDataItem("1", "Info")),
    SUCCESS(new ReferenceDataItem("2", "Success")),
    WARNING(new ReferenceDataItem("3", "Warning")),
    INVALID(new ReferenceDataItem("4", "Invalid")),
    ERROR(new ReferenceDataItem("5", "Error"));

    private final ReferenceDataItem referenceDataItem;

    NotificationType(ReferenceDataItem referenceDataItem) {
        this.referenceDataItem = referenceDataItem;
    }
    
    public ReferenceDataItem getReferenceDataItem() {
        return referenceDataItem;
    }
}

