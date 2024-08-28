package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DropFeatureAndComponentsValidator implements ConstraintValidator<ValidFeatureAndComponents, TestCampaignInfo> {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private ProductFeatureRepository featureRepository;


    @Override
    public void initialize(ValidFeatureAndComponents validFeaturesAndComponents) {
        // no initialization necessary
    }

    @Override
    public boolean isValid(TestCampaignInfo dto, ConstraintValidatorContext constraintValidatorContext) {
        ProductInfo productInfo = dto.getProduct();
        DropInfo dropInfo = dto.getDrop();
        Set<FeatureInfo> featureInfos = dto.getFeatures();
        if (productInfo == null || featureInfos == null) { //must have product and features
            return false;
        } else {
            Product product = productRepository.find(productInfo.getId());
            if (product == null) {
                return false;
            }
            if (!isValidDrop(dropInfo, product)) {
                return false;
            }

            if (!isValidFeatures(dto, featureInfos, product)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidDrop(DropInfo dropInfo, Product product) {
        if (product.isDropCabable()) {
            if (dropInfo == null) {
                return false;
            }
            Drop drop = dropRepository.find(dropInfo.getId());
            if (drop == null || !dropBelongsToProduct(product, drop)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidFeatures(TestCampaignInfo dto, Set<FeatureInfo> featureInfos, Product product) {
        List<ProductFeature> features = Lists.newArrayList();
        for (FeatureInfo featureInfo : featureInfos) {
            ProductFeature feature = featureRepository.find(featureInfo.getId());
            if (feature == null || !featureBelongsToProduct(product, feature)) {
                return false;
            }
            features.add(feature);
        }
        List<Long> componentIds = getComponentIds(dto.getComponents());

        List<Long> featureComponentIds = features.stream()
                .flatMap(m -> m.getComponents().stream())
                .map(c -> c.getId())
                .collect(Collectors.toList());

        return componentsHaveConnectedFeatures(featureComponentIds, componentIds);

    }

    private boolean dropBelongsToProduct(Product product, Drop drop) {
        return product.getDrops().contains(drop);
    }

    private boolean featureBelongsToProduct(Product product, ProductFeature feature) {
        return product.getFeatures().contains(feature);
    }

    private boolean componentsHaveConnectedFeatures(List<Long> featureComponentIds, List<Long> componentIds) {
        if (componentIds.isEmpty()) {
            return true;
        }
        return featureComponentIds.containsAll(componentIds);
    }

    private List<Long> getComponentIds(Set<TechnicalComponentInfo> componentInfos) {
        return componentInfos.stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());
    }

}
