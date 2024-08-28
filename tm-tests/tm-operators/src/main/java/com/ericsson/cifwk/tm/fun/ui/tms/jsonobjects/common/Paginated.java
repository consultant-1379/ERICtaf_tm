package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import java.util.List;

public class Paginated<T> {
    
    private String totalCount;
    private List<T> items;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
