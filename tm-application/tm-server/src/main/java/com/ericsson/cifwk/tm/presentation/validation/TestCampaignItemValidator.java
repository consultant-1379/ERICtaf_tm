/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.validation;

import com.ericsson.cifwk.tm.infrastructure.mapping.TestCaseVersionMapper;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class TestCampaignItemValidator implements ConstraintValidator<TestCampaignItem, TestCampaignItemInfo> {

    @Inject
    private TestCaseVersionMapper testCaseVersionMapper;

    @Override
    public void initialize(TestCampaignItem constraintAnnotation) {
        // empty init
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(TestCampaignItemInfo testCampaignItemInfo, ConstraintValidatorContext context) {
        Long testCasePk = testCampaignItemInfo.getTestCase().getId();
        String testCaseId = testCampaignItemInfo.getTestCase().getTestCaseId();
        Map<String, Long> versionMap = testCaseVersionMapper.getVersion(testCampaignItemInfo.getTestCase().getVersion());
        Long testCaseSequenceNumber = versionMap.get(TestCaseVersionMapper.MAJOR);

        return (testCaseSequenceNumber != null) && (testCasePk != null || testCaseId != null);
    }

}
