package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common;

import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;

public enum ProductType {

    ENM(getProduct(2L, "ENM", "ENM")),
    OSSRC(getProduct(3L, "OSS-RC", "OSS-RC")),
    ASSURE(getProduct(5L, "Assure", "Assure")),
    DE(getProduct(6L, "DE", "DE"));

    private ProductInfo productInfo;

    ProductType(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public static ProductInfo getProduct(Long id, String externalId, String name){
        return new ProductInfo(id, externalId, name);
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }
}
