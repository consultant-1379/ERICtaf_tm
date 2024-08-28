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
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class UpdateProductCommand implements Command<ProductInfo> {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMapper productMapper;

    @Override
    public Response apply(ProductInfo input) {
        productRepository.disableFilter();
        Product otherProduct = productRepository.findByName(input.getName());
        if (otherProduct != null) {
            if (otherProduct.isDeleted()) {
                productRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted project of the same name and or external Id  still exists." +
                        " Please try again using a different name and or external Id"));
            } else if (!otherProduct.getId().equals(input.getId())) {
                productRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Another project already exists"));
            }
        }
        Product product = productRepository.findByExternalId(input.getExternalId());
        if (product != null) {
            if (product.isDeleted()) {
                productRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Deleted product of the same External Id still exists." +
                        " Please try again using a different  External Id"));
            } else if (!product.getId().equals(input.getId())) {
                productRepository.enableFilter();
                throw new BadRequestException(Responses.badRequest("Product already exists"));
            }
        }
        Product entity = productRepository.find(input.getId());
        if (entity == null) {
            productRepository.enableFilter();
            throw new NotFoundException(Responses.notFound());
        }
        productRepository.enableFilter();
        productMapper.mapDto(input, entity);
        productRepository.save(entity);
        return Responses.ok();
    }

}
