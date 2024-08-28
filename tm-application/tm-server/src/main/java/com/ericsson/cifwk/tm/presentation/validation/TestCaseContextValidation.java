package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TestCaseContextValidation {

    @Inject
    private ProductFeatureRepository featureRepository;

    @Inject
    private TechnicalComponentRepository componentRepository;

    public boolean isValid(TestCaseInfo testCaseInfo) {
        FeatureInfo featureInfo = testCaseInfo.getFeature();
        Set<ReferenceDataItem> componentInfos = testCaseInfo.getTechnicalComponents();

        if (featureInfo == null || componentInfos == null) {
            return false;
        } else {
            ProductFeature feature = featureRepository.find(featureInfo.getId());
            if (feature == null) {
                return false;
            }

            Long[] componentIds = getComponentIds(componentInfos);
            TechnicalComponent[] components = componentRepository.find(componentIds);
            return componentsBelongToFeature(feature, components);
        }
    }

    private boolean componentsBelongToFeature(ProductFeature feature, TechnicalComponent[] components) {
        Set<TechnicalComponent> componentSet = Arrays.stream(components).collect(Collectors.toSet());
        return feature.getComponents().containsAll(componentSet);
    }

    private Long[] getComponentIds(Set<ReferenceDataItem> componentInfos) {
        return componentInfos.stream().map(c -> Long.parseLong(c.getId())).toArray(Long[]::new);
    }
}
