package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.testdesign.TestCase;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TestCampaignTestCasesContextValidator implements ConstraintValidator<ContextsMatch, TestCampaignInfo> {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Override
    public void initialize(ContextsMatch contextsMatch) {
        // no initialization required
    }

    @Override
    public boolean isValid(TestCampaignInfo testCampaignInfo, ConstraintValidatorContext constraintValidatorContext) {
        Set<TestCampaignItemInfo> testCampaignItemInfos = testCampaignInfo.getTestCampaignItems();
        if (testCampaignItemInfos.isEmpty()) {
            return true;
        }
        Optional optionalFeatures = Optional.ofNullable(testCampaignInfo.getFeatures());
        if (!optionalFeatures.isPresent()) {
            return false;
        }

        Long[] testCaseIds = testCampaignItemInfos.stream()
                .map(t -> t.getTestCase().getId())
                .toArray(size -> new Long[testCampaignItemInfos.size()]);

        List<TestCase> testCases = Arrays.stream(testCaseRepository.find(testCaseIds))
                .collect(Collectors.toList());

        List<Long> testCaseFeatureIds = testCases.stream()
                .map(t -> t.getCurrentVersion().getProductFeature().getId())
                .distinct()
                .collect(Collectors.toList());
        Set<FeatureInfo> featureInfo = testCampaignInfo.getFeatures();

        List<Long> testCampaignFeatureIds = featureInfo.stream()
                .map(f -> f.getId())
                .collect(Collectors.toList());

        List<Long> testCampaignComponentIds = testCampaignInfo.getComponents().stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());

        List<Long> testCaseComponentIds = testCases.stream()
                .flatMap(t -> t.getCurrentVersion().getTechnicalComponents().stream())
                .map(component -> component.getId())
                .distinct()
                .collect(Collectors.toList());

        if (!testCampaignComponentIds.isEmpty()) {
            return testCampaignFeatureIds.containsAll(testCaseFeatureIds) && testCampaignComponentIds.containsAll(testCaseComponentIds);
        }

        return testCampaignFeatureIds.containsAll(testCaseFeatureIds);
    }
}
