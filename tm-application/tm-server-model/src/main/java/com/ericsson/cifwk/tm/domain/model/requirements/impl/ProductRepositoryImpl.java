/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;

@Repository
public class ProductRepositoryImpl extends BaseRepositoryImpl<Product, Long> implements ProductRepository {

    @Override
    public Product getDefault() {
        Search search = new Search(Product.class);
        search.addFilterEqual("externalId", Product.DEFAULT_EXTERNAL_ID);
        return searchUnique(search); // Must exist
    }

    @Override
    public Product findByName(String name) {
        if (name == null) {
            return null;
        }

        Search search = new Search(Product.class);
        search.addFilterEqual("name", name);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Product findByExternalId(String externalId) {
        if (externalId == null) {
            return null;
        }

        Search search = new Search(Product.class);
        search.addFilterEqual("externalId", externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

}
