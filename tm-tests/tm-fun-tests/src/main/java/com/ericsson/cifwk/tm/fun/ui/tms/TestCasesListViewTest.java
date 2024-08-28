/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.DropType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.FeatureType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.Paginated;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.TechnicalComponentType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.FilterInfo;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table.TestCasesColumn;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.Result;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.TestCasesResult;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.google.common.collect.Sets;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ericsson.cifwk.tm.fun.common.testcampaigns.TestCampaignsListTestHelper.getTestCampaignFilterSelection;
import static com.ericsson.cifwk.tm.fun.ui.common.TimingHelper.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.testng.Assert.assertEquals;


public class TestCasesListViewTest extends BaseFuncTest {

    @Test
    @TestId(id = "CIP-4168_Func_1", title = "Show Test Cases table with results")
    public void showTestCases() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        TestCasesResult result = tmUiOperator.getTestCasesByFilter(Lists.<FilterInfo>newArrayList());
        CustomAsserts.checkTestStep(result);
        assertEquals(false, result.getTestCases().isEmpty());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-4168_Func_2", title = "Show Test Cases by Search")
    public void showTestCasesByFilterSearch() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();
        filters.add(new FilterInfo(TestCasesColumn.TEST_CASE_ID, Condition.EQUALS, "CIP-3517"));

        TestCasesResult result = tmUiOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);
        List<TestCaseInfo> testInfos = result.getTestCases();
        assertEquals(false, testInfos.isEmpty());
        assertEquals(1, testInfos.size());

        TestCaseInfo testInfo = testInfos.get(0);
        assertEquals("CIP-3517", testInfo.getTestCaseId());
        assertEquals("test dm", testInfo.getTitle());
        assertEquals("test dm", testInfo.getDescription());

        Set<String> requirements = testInfo.getRequirementIds();
        assertEquals(1, requirements.size());
        assertEquals("CIP-4960", requirements.iterator().next());

        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id = "CIP-24093_Func_3", title = "Search works when product changed for test campaign search and navigate back to test case search")
    public void showTestCasesSearchWorksWhenTestCampaignSearchProductChanged() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(tmUiOperator.selectUserProfileProduct(ProductType.ENM.getProductInfo()));

        List<FilterInfo> filters = Lists.newArrayList();

        TestCasesResult result = tmUiOperator.getTestCasesByFilter(filters);
        CustomAsserts.checkTestStep(result);
        List<TestCaseInfo> testInfos = result.getTestCases();
        assertEquals(false, testInfos.isEmpty());

        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestPlans());

        ProductInfo product = ProductType.OSSRC.getProductInfo();
        Set<FeatureInfo> featureInfos = Sets.newHashSet(FeatureType.OSS_CN.getFeatureInfo());
        Optional<DropInfo> drop = Optional.of(DropType.OSS_2.getDropInfo());
        Optional<Set<TechnicalComponentInfo>> technicalComponents = Optional.of(Sets.newHashSet(TechnicalComponentType.OSS_CN.getTechnicalComponentInfo()));
        TestCampaignInfo filterSelection = getTestCampaignFilterSelection(product, drop, featureInfos, technicalComponents);

        List<FilterInfo> tableFilters = Lists.newArrayList();
        Result<Paginated<TestCampaignInfo>> testCampaignSearchResult = tmUiOperator.filterTestPlans(filterSelection, tableFilters);
        CustomAsserts.checkTestStep(result);
        List<TestCampaignInfo> items = testCampaignSearchResult.getValue().getItems();
        assertThat(items, hasSize(1));

        CustomAsserts.checkTestStep(tmUiOperator.navigateToTestCases());
        result = tmUiOperator.getTestCasesByFilter(Lists.newArrayList());
        CustomAsserts.checkTestStep(result);
        testInfos = result.getTestCases();

        assertEquals(true, testInfos.isEmpty());
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

}
