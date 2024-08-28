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

public enum SortOrderInfo {
    ASC("asc"),
    DESC("desc");

    private final String order;

    private SortOrderInfo(String order) {
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public boolean isDescending() {
        return this == DESC;
    }

    public static SortOrderInfo parse(String order) {
        for (SortOrderInfo info : values()) {
            String itemOrder = info.getOrder();
            if (itemOrder.equalsIgnoreCase(order)) {
                return info;
            }
        }
        return ASC;
    }
}
