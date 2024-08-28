package com.ericsson.cifwk.tm.fun.common.testcampaigns;

import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by egergle on 04/01/2017.
 */
public class TestCampaignsListTestHelper {

    private TestCampaignsListTestHelper () {
        //empty
    }

    public static TestCampaignInfo getTestCampaignFilterSelection(ProductInfo product, Optional<DropInfo> drop,
                                                            Set<FeatureInfo> features, Optional<Set<TechnicalComponentInfo>> components) {

        TestCampaignInfo filterSelection = new TestCampaignInfo();
        filterSelection.setProduct(product);
        if (drop.isPresent()) {
            filterSelection.setDrop(drop.get());
        }
        filterSelection.setFeatures(features);
        if (components.isPresent()) {
            filterSelection.setComponents(components.get());
        }

        return filterSelection;
    }

    public static void setTestCampaignAttributes(TestCampaignInfo testCampaignInfo) {
        testCampaignInfo.setDescription(UUID.randomUUID().toString());
        testCampaignInfo.setEnvironment(UUID.randomUUID().toString());

        testCampaignInfo.setTestCampaignItems(new HashSet<TestCampaignItemInfo>());

        TestCampaignItemInfo assignmentInfo1 = new TestCampaignItemInfo();
        assignmentInfo1.getTestCase().setId(10L);
        assignmentInfo1.getTestCase().setTestCaseId("CIP-2638_Func_1");
        assignmentInfo1.getTestCase().setVersion("1.1");

        TestCampaignItemInfo assignmentInfo2 = new TestCampaignItemInfo();
        assignmentInfo2.getTestCase().setId(11L);
        assignmentInfo2.getTestCase().setTestCaseId("CIP-2638_Func_2");
        assignmentInfo2.getTestCase().setVersion("1.1");

        testCampaignInfo.getTestCampaignItems().add(assignmentInfo1);
        testCampaignInfo.getTestCampaignItems().add(assignmentInfo2);
    }

    public static void checkThatTestPlanDetailsWereSavedCorrectly(TestCampaignInfo expected, TestCampaignInfo actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getEnvironment(), actual.getEnvironment());
        assertEquals(expected.getProduct().getName(), actual.getProduct().getName());
        if (expected.getDrop() != null) {
            assertEquals(expected.getDrop().getName(), actual.getDrop().getName());
        }
        Set<String> expectedFeatureNames = expected.getFeatures().stream().map(f -> f.getName()).collect(toSet());
        Set<String> actualFeatureNames = actual.getFeatures().stream().map(f -> f.getName()).collect(toSet());
        assertTrue(expectedFeatureNames.containsAll(actualFeatureNames));

        Set<String> expectedComponentNames = expected.getComponents().stream().map(c -> c.getName()).collect(toSet());
        Set<String> actualComponentNames = actual.getComponents().stream().map(c -> c.getName()).collect(toSet());
        assertTrue(expectedComponentNames.containsAll(actualComponentNames));
    }
}
