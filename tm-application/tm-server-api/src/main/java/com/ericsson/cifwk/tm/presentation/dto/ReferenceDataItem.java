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
import com.ericsson.cifwk.tm.presentation.validation.NotEmptyField;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ReferenceDataItem implements Identifiable<String> {

    @NotEmptyField("id")
    protected String id;

    protected String title;

    public ReferenceDataItem() {
    }

    public ReferenceDataItem(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReferenceDataItem that = (ReferenceDataItem) o;
        return new EqualsBuilder().append(id, that.id).append(title, that.title).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(title).toHashCode();
    }

    @Override
    public String toString() {
        return "ReferenceDataItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
