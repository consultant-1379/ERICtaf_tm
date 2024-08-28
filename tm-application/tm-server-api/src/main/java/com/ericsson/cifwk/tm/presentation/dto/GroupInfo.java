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

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;

public class GroupInfo implements Identifiable<Long>,GenericProductInfo {

    private Long id;

    @NotNullField("enabled")
    private boolean enabled;

    @NotNullField("name")
    private String name;

    @NotNullField("product")
    private ProductInfo product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
