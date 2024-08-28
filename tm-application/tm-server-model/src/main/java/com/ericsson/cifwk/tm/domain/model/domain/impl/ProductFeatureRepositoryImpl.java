package com.ericsson.cifwk.tm.domain.model.domain.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import java.util.List;

@Repository
public class ProductFeatureRepositoryImpl extends BaseRepositoryImpl<ProductFeature, Long> implements ProductFeatureRepository {

    @Override
    public List<ProductFeature> findByProduct(Long productId) {
        Search search = new Search(ProductFeature.class);
        search.addFilterEqual("product.id", productId);
        return this.search(search);
    }

    @Override
    public ProductFeature findByProductAndName(Long productId, String name) {
        Search search = new Search(ProductFeature.class);
        search.addFilterEqual("product.id", productId);
        search.addFilterEqual("name", name);
        return this.searchUnique(search);
    }

    @Override
    public List<ProductFeature> findByIds(List<Long> ids) {
        Search search = new Search(ProductFeature.class);
        search.addFilterIn("id", ids);
        return this.search(search);
    }
}
