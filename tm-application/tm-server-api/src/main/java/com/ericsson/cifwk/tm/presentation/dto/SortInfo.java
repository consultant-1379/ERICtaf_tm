/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

public class SortInfo {

    private String field;
    private boolean descending;

    public SortInfo(String field, SortOrderInfo order) {
        this(field, order.isDescending());
    }

    public SortInfo(String field, boolean descending) {
        this.field = field;
        this.descending = descending;
    }

    public String getField() {
        return field;
    }

    public boolean isDescending() {
        return descending;
    }
}
