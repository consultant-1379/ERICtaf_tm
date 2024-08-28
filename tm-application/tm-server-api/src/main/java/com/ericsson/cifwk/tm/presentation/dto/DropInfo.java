package com.ericsson.cifwk.tm.presentation.dto;

import com.ericsson.cifwk.tm.common.Identifiable;
import com.ericsson.cifwk.tm.presentation.validation.NotNullField;
import com.google.common.base.Objects;


public class DropInfo implements Identifiable<Long> ,GenericProductInfo{

    private Long id;

    @NotNullField("name")
    private String name;

    @NotNullField("product")
    private ProductInfo product;

    private String productName;

    private boolean defaultDrop;

    public DropInfo() {
    }

    public DropInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public DropInfo(String productName, String name) {
        this(null, productName, name);
    }

    public DropInfo(Long id, String productName, String name) {
        this.id = id;
        this.productName = productName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isDefaultDrop() {
        return defaultDrop;
    }

    public void setDefaultDrop(boolean defaultDrop) {
        this.defaultDrop = defaultDrop;
    }

    @Override
    public String toString() {
        return "DropInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DropInfo dropInfo = (DropInfo) o;
        return Objects.equal(productName, dropInfo.productName) &&
                Objects.equal(name, dropInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName, name);
    }
}
