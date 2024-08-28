package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.presentation.dto.TestExecutionInfo;

import javax.inject.Inject;

public class TestExecutionIsoValidation {

    @Inject
    private TestCampaignRepository testCampaignRepository;

    public boolean isValid(TestExecutionInfo testExecutionInfo) {
        Long testCampaignId = testExecutionInfo.getTestPlan();
        TestCampaign testCampaign = testCampaignRepository.find(testCampaignId);
        Product product = testCampaign.getFeatures().stream().findFirst().get().getProduct();
        Drop drop = testCampaign.getDrop();

        if (product.isDropCabable() && !drop.isDefaultDrop() && testExecutionInfo.getIso() == null
                && !product.getName().matches("Ericsson Orchestrator")) {
            return false;
        }
        return true;
    }
}
