/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProductMapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

public class CreateProductCommand implements Command<ProductInfo> {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMapper productMapper;

    @Override
    public Response apply(ProductInfo input) {
        productRepository.disableFilter();
        Product entity = productMapper.mapDto(input, Product.class);
        Product otherProduct = productRepository.findByName(input.getName());
        if (otherProduct != null) {
            productRepository.enableFilter();
            if (otherProduct.isDeleted()) {
                if (otherProduct.getExternalId().equals(input.getExternalId())) {
                    otherProduct.undelete();
                    ProductInfo dto = productMapper.mapEntity(entity, new ProductInfo());
                    return Responses.created(dto);
                } else {
                    throw new BadRequestException(Responses.badRequest("Deleted Product with the same name but different external id." +
                            "To undo delete please try again using the same name and " + otherProduct.getExternalId() + "as the external id"));
                }
            } else if (!otherProduct.getId().equals(input.getId())) {
                throw new BadRequestException(Responses.badRequest("Another product already exists"));
            }
        }
        Product product = productRepository.findByExternalId(input.getExternalId());
        if (product != null) {
            productRepository.enableFilter();
            if (!product.isDeleted()) {
                throw new BadRequestException(Responses.badRequest("Product already exists"));
            } else {
                if (product.getName().equals(input.getName())) {
                    product.undelete();
                    ProductInfo dto = productMapper.mapEntity(entity, new ProductInfo());
                    return Responses.created(dto);
                } else {
                    throw new BadRequestException(Responses.badRequest("Deleted Product with the same external id but different name." +
                            "To undo delete please try again using the same external id and " + product.getName() + "as the name"));
                }
            }
        }
        productRepository.persist(entity);
        productRepository.enableFilter();
        ProductInfo dto = productMapper.mapEntity(entity, new ProductInfo());
        return Responses.created(dto);
    }

}
