/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.repository;

import com.ericsson.cifwk.tm.application.BaseServiceLayerTest;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductRepositoryImplTest extends BaseServiceLayerTest {

    @Inject
    private ProductRepository productRepository;

    @Test
    public void testFindByName() throws Exception {
        Product product1 = fixture().persistProduct("PRODUCT1");
        Product product2 = fixture().persistProduct("PRODUCT2");

        List<Product> allProducts = productRepository.findAll();
        assertEquals(2, allProducts.size());

        assertEquals(product1.getName(), allProducts.get(0).getName());
        assertEquals(product2.getName(), allProducts.get(1).getName());
    }
}
