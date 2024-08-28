package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.domain.model.domain.impl.ProductFeatureRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(ProductFeatureRepositoryImpl.class)
public interface ProductFeatureRepository extends BaseRepository<ProductFeature, Long> {

    List<ProductFeature> findByProduct(Long productId);

    ProductFeature findByProductAndName(Long productId, String name);

    List<ProductFeature> findByIds(List<Long> ids);


}
