/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto;

import java.util.Collections;
import java.util.List;

public class CompletionInfo {

    public static CompletionInfo empty() {
        CompletionInfo info = new CompletionInfo();
        info.setSearch("");
        info.setItems(Collections.<CompletionItemInfo>emptyList());
        return info;
    }

    public static CompletionInfo empty(String search) {
        CompletionInfo info = new CompletionInfo();
        info.setSearch(search);
        info.setItems(Collections.<CompletionItemInfo>emptyList());
        return info;
    }

    private String search;

    private List<CompletionItemInfo> items;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public List<CompletionItemInfo> getItems() {
        return items;
    }

    public void setItems(List<CompletionItemInfo> items) {
        this.items = items;
    }

}
