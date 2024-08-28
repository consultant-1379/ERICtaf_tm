package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

public class TestCampaignNotAlreadyInDropValidator implements ConstraintValidator<TestCampaignNotAlreadyInDrop, TestCampaignInfo> {

    @Inject
    private TestCampaignRepository testCampaignRepository;

    @Override
    public void initialize(TestCampaignNotAlreadyInDrop testCampaignNotAlreadyInDrop) {
        // no initialization necessary
    }

    @Override
    public boolean isValid(TestCampaignInfo testCampaignInfo, ConstraintValidatorContext constraintValidatorContext) {
        String name = testCampaignInfo.getName();
        Optional<DropInfo> drop = Optional.ofNullable(testCampaignInfo.getDrop());
        if (drop.isPresent()) {
            Long dropId = drop.get().getId();
            Optional<List<TestCampaign>> testCampaignsWithSameName = testCampaignRepository.findByNameAndDrop(name, dropId);
            if (testCampaignsWithSameName.isPresent()) {
                return testCampaignsWithSameName.get().stream().allMatch(item -> item.getId().equals(testCampaignInfo.getId()));
            }
        }

        return true;
    }
}
