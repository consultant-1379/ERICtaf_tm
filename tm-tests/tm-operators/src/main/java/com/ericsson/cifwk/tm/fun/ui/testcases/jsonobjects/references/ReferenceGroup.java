package com.ericsson.cifwk.tm.fun.ui.testcases.jsonobjects.references;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.references.Reference;

import java.util.List;

public class ReferenceGroup {
    private String id;
    private List<Reference> items = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Reference> getItems() {
        return items;
    }

    public void setItems(List<Reference> items) {
        this.items = items;
    }

    public void addItem(String id, String title) {
        this.items.add(new Reference(id, title));
    }
}
