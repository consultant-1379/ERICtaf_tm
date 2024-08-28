package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.presentation.dto.CopyTestCampaignsRequest;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

public class CopyTestCampaignsRequestValidator implements ConstraintValidator<ValidCopyTestCampaignsRequest, CopyTestCampaignsRequest> {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private ProductFeatureRepository featureRepository;

    @Override
    public void initialize(ValidCopyTestCampaignsRequest validCopyTestCampaignsRequest) {
        // no initialization necessary
    }

    @Override
    public boolean isValid(CopyTestCampaignsRequest request, ConstraintValidatorContext constraintValidatorContext) {
        Product product = productRepository.find(request.getProductId());
        if (product == null) {
            return false;
        }
        Drop fromDrop = dropRepository.find(request.getFromDropId());
        if (fromDrop == null || !dropBelongsToProduct(product, fromDrop)) {
            return false;
        }
        List<ProductFeature> features = featureRepository.findByIds(request.getFeatureIds());
        if (!featuresBelongsToProducts(product, features)) {
            return false;
        }

        List<Long> featureComponentIds = features.stream()
                .flatMap(m -> m.getComponents().stream())
                .map(c -> c.getId())
                .collect(Collectors.toList());

        if (!componentsBelongToFeatures(featureComponentIds, request.getComponentIds())) {
            return false;
        }
        Drop copyToDrop = dropRepository.find(request.getCopyToDropId());
        if (copyToDrop == null || !dropBelongsToProduct(product, copyToDrop)) {
            return false;
        }
        if (fromDrop.equals(copyToDrop)) {
            return false;
        }
        return true;
    }

    private boolean featuresBelongsToProducts(Product product, List<ProductFeature> features) {
        for (ProductFeature feature :features) {
            if (feature == null || !featureBelongsToProduct(product, feature)) {
                return false;
            }
        }
        return true;
    }

    private boolean dropBelongsToProduct(Product product, Drop drop) {
        return product.getDrops().contains(drop);
    }

    private boolean featureBelongsToProduct(Product product, ProductFeature feature) {
        return product.getFeatures().contains(feature);
    }

    private boolean componentsBelongToFeatures(List<Long> featureComponents, List<Long> components) {
        if (components.isEmpty()) {
            return true;
        }
        return featureComponents.containsAll(components);
    }
}
