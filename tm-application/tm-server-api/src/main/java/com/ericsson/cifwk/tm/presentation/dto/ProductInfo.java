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
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;

public class ProductInfo implements Identifiable<Long> {

    private Long id;

    @NotNullField("externalId")
    private String externalId;

    @NotNullField("name")
    private String name;

    @NotNullField("dropCapable")
    private boolean dropCapable;

    public ProductInfo() {
    }

    public ProductInfo(Long id, String externalId, String name) {
        this.id = id;
        this.externalId = externalId;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDropCapable() {
        return dropCapable;
    }

    public void setDropCapable(boolean dropCapable) {
        this.dropCapable = dropCapable;
    }

}
