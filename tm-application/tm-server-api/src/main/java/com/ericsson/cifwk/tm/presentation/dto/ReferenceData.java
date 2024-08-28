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

import com.ericsson.cifwk.tm.common.Identifiable;

import java.util.ArrayList;
import java.util.List;

public class ReferenceData implements Identifiable<String> {

    protected String id;
    protected List<ReferenceDataItem> items;

    public ReferenceData() {
    }

    public ReferenceData(String id) {
        this.id = id;
        this.items = new ArrayList<>();
    }

    public ReferenceData(String id, List<ReferenceDataItem> items) {
        this.id = id;
        this.items = items;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ReferenceDataItem> getItems() {
        return items;
    }

    public void setItems(List<ReferenceDataItem> items) {
        this.items = items;
    }

}
