/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class TechnicalComponentRepositoryImpl
        extends BaseRepositoryImpl<TechnicalComponent, Long>
        implements TechnicalComponentRepository {

    @Override
    public int countForProduct(Long productId) {
        Search search = new Search(TechnicalComponent.class);
        search.addFilterEqual("feature.product.id", productId);
        return count(search);
    }

    @Override
    public TechnicalComponent findByName(String name, Long productId) {
        Search search = new Search(TechnicalComponent.class);
        search.addFilterEqual("name", name);
        if (productId != null) {
            search.addFilterEqual("feature.product.id", productId);
        } else {
            search.addFilterNull("feature.product");
        }
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<TechnicalComponent> findByFeatureIds(List<Long> featureIds) {
        Search search = new Search(TechnicalComponent.class);
        search.addFilterIn("feature.id", featureIds);
        return this.search(search);
    }

    @Override
    public TechnicalComponent findByFeatureAndName(Long featureId, String name) {
        Search search = new Search(TechnicalComponent.class);
        search.addFilterEqual("feature.id", featureId);
        search.addFilterEqual("name", name);
        return this.searchUnique(search);
    }

    @Override
    public List<TechnicalComponent> findByIds(List<Long> ids) {
        Search search = new Search(TechnicalComponent.class);
        search.addFilterIn("id", ids);
        return this.search(search);
    }

}
