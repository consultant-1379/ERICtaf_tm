/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.ericsson.cifwk.tm.application.commands.CreateProductCommand;
import com.ericsson.cifwk.tm.application.commands.DeleteProductCommand;
import com.ericsson.cifwk.tm.application.commands.UpdateProductCommand;
import com.ericsson.cifwk.tm.application.queries.ProductQuerySet;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.resources.ProductResource;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;

@Controller
public class ProductController implements ProductResource {

    @Inject
    private ProductQuerySet productQuerySet;

    @Inject
    private CreateProductCommand createProductCommand;

    @Inject
    private UpdateProductCommand updateProductCommand;

    @Inject
    private DeleteProductCommand deleteProductCommand;

    @Inject
    private CommandProcessor commandProcessor;

    @Override
    public Response getProducts() {
        return productQuerySet.getProducts();
    }

    @Override
    public Response getProduct(String productId) {
        return productQuerySet.getProduct(productId);
    }

    @Override
    public Response getDrops(Long productId) {
        return productQuerySet.getDropsByProduct(productId);
    }

    @Override
    public Response getFeatures(Long productId) {
        return productQuerySet.getFeaturesByProduct(productId);
    }

    @Override
    public Response getComponents(List<Long> featureIds) {
        return productQuerySet.getComponentsByProductAndFeature(featureIds);
    }

    @Override
    public Response create(ProductInfo productInfo) {
        return commandProcessor.process(createProductCommand, productInfo);
    }

    @Override
    public Response update(Long id, ProductInfo productInfo) {
        Preconditions.checkArgument(
                id.equals(productInfo.getId()),
                "Product ids in URL path and request body do not match"
        );
        return commandProcessor.process(updateProductCommand, productInfo);
    }

    @Override
    public Response delete(Long id) {
        return commandProcessor.process(deleteProductCommand, id);
    }
}
