package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;

import javax.validation.constraints.NotNull;


public class FeatureInfo implements Identifiable<Long> ,GenericProductInfo {

    private Long id;

    @NotNullField("name")
    private String name;

    @NotNullField("product")
    private ProductInfo product;

    public FeatureInfo() {
    }

    public FeatureInfo(Long id,  String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }
}
