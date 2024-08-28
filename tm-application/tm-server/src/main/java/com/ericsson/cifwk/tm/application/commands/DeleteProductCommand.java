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
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class DeleteProductCommand implements Command<Long> {

    @Inject
    private ProductRepository productRepository;

    @Override
    public Response apply(Long input) {
        Product product = productRepository.find(input);
        if (product == null || product.isDeleted()) {
            throw new NotFoundException(Responses.notFound());
        }
        product.delete();
        product.getFeatures().stream().forEach(item -> {
                item.delete();
                item.getComponents().stream().forEach(component -> component.delete());
            });

        return Responses.noContent();
    }

}
