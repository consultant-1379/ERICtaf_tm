package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.ProductMapper;
import com.ericsson.cifwk.tm.infrastructure.mapping.TechnicalComponentMapper;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.ericsson.cifwk.tm.utils.VersionComparator;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author egergle
 */
@QuerySet
public final class ProductQuerySet {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProductMapper productMapper;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private DropMapper dropMapper;

    @Inject
    private ProductFeatureRepository featureRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Inject
    private TechnicalComponentRepository componentRepository;

    @Inject
    private TechnicalComponentMapper componentMapper;

    public Response getProduct(String productId) {
        Product product = productRepository.find(Long.parseLong(productId));
        ProductInfo productInfo = productMapper.mapEntity(product, ProductInfo.class);
        return Responses.nullable(productInfo);
    }

    public Response getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductInfo> dtos = products.stream()
                .map(p -> productMapper.mapEntity(p, ProductInfo.class))
                .collect(Collectors.toList());

        return Responses.nullable(dtos);
    }

    public Response getDropsByProduct(Long productId) {
        List<Drop> drops = dropRepository.findByProduct(productId);
        drops.sort(new VersionComparator());

        List<DropInfo> dtos = drops.stream()
                .map(d -> dropMapper.mapEntity(d, DropInfo.class))
                .collect(Collectors.toList());

        return Responses.nullable(dtos);
    }

    public Response getFeaturesByProduct(Long productId) {
        List<ProductFeature> features = featureRepository.findByProduct(productId);
        List<FeatureInfo> dtos = features.stream()
                .map(f -> featureMapper.mapEntity(f, FeatureInfo.class))
                .sorted((FeatureInfo o1, FeatureInfo o2)-> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());

        return Responses.nullable(dtos);
    }

    public Response getComponentsByProductAndFeature(List<Long> featureIds) {
        List<TechnicalComponent> components = componentRepository.findByFeatureIds(featureIds);
        List<TechnicalComponentInfo> dtos = components.stream()
                .map(c -> componentMapper.mapEntity(c))
                .sorted((TechnicalComponentInfo o1, TechnicalComponentInfo o2)-> o1.getName().compareTo(o2.getName()))
                .collect(Collectors.toList());

        return Responses.nullable(dtos);
    }
}

