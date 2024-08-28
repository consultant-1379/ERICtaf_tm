/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;

@Repository
public class ScopeRepositoryImpl extends BaseRepositoryImpl<Scope, Long> implements ScopeRepository {

    @Override
    public Scope findByName(String name) {
        Search search = new Search(Scope.class);
        search.addFilterEqual("name", name);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Scope findByNameAndProduct(String name, long productId) {
        Search search = new Search(Scope.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("product.id", productId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

}
