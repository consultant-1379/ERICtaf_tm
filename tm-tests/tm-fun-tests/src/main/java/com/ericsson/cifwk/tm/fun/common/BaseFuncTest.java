package com.ericsson.cifwk.tm.fun.common;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.LoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.DropType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.FeatureType;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.UiTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.admin.UiTestManagementAdminOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.notifications.NotificationFixturesOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases.TestCaseFixturesOperator;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestStepInfo;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author ebuzdmi
 */
public class BaseFuncTest extends TafTestBase {

    @Inject
    private RestOperators restOperators;

    @Inject
    private UiOperators uiOperators;

    protected Host host;
    protected HostHelper hostHelper;

    protected LoginOperator loginOperator;
    protected UiTestManagementOperator tmUiOperator;
    protected RestTestManagementOperator tmRestOperator;
    protected NotificationFixturesOperator notificationFixturesOperator;
    protected TestCaseFixturesOperator testCaseFixturesOperator;
    protected UiTestManagementAdminOperator uiTestManagementAdminOperator;

    @BeforeClass(alwaysRun = true)
    public final void setUp() {
        host = DataHandler.getHostByName(Hosts.TEST_MANAGEMENT_SYSTEM);
        hostHelper = new HostHelper(host);
    }

    @AfterClass()
    public final void tearDown() {
    }

    public void createUiOperators() {
        uiOperators.getLoginOperator().start(host);
        uiOperators.getTestManagementOperator().start(host);
        uiOperators.getTestCaseFixturesOperator().start(host);
        uiOperators.getNotificationOperator().start(host);

        this.loginOperator = uiOperators.getLoginOperator();
        this.uiTestManagementAdminOperator = uiOperators.getUiTestManagementAdminOperator();
        this.tmUiOperator = uiOperators.getTestManagementOperator();
        this.notificationFixturesOperator = uiOperators.getNotificationOperator();

        this.testCaseFixturesOperator = uiOperators.getTestCaseFixturesOperator();
    }

    public RestOperators createRestOperators() {
        restOperators.getLoginOperator().start(host);
        restOperators.getTestManagementOperator().start(host);
        restOperators.getTestCaseFixturesOperator().start(host);

        this.loginOperator = restOperators.getLoginOperator();
        this.tmRestOperator = restOperators.getTestManagementOperator();
        this.testCaseFixturesOperator = restOperators.getTestCaseFixturesOperator();
        return restOperators;
    }

    protected InputStream getFullTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/createTestCase_fullData.json");
    }

    protected InputStream getManualTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/createManualTestCase_fullData.json");
    }

    protected InputStream getEditTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/editTestCase_fullData.json");
    }

    protected InputStream getEditTestCaseChangeProductJson() {
        return getClass().getClassLoader().getResourceAsStream("data/editTestCase2_fullData.json");
    }

    protected InputStream getPartialTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/editTestCase_notFullData.json");
    }

    protected InputStream getCopyTestCaseJson() {
        return getClass().getClassLoader().getResourceAsStream("data/copyTestCaseRESTData.json");
    }

    protected InputStream getDeTestCampaign() {
        return getClass().getClassLoader().getResourceAsStream("data/testPlan_DE.json");
    }

    protected InputStream getEnmTestCampaign() {
        return getClass().getClassLoader().getResourceAsStream("data/testPlan_ENM.json");
    }

    protected InputStream getEnmTestCampaignDrop() {
        return getClass().getClassLoader().getResourceAsStream("data/testPlan_ENM2.json");
    }

    protected InputStream getEnmTestCampaignWithTestCases() {
        return getClass().getClassLoader().getResourceAsStream("data/testPlan_ENM_withTestCases.json");
    }

    protected void checkSavedTestCaseData(TestCaseInfo expected, TestCaseInfo actual, boolean isCopyOperation) {
        if (!isCopyOperation) {
            assertThat(expected.getTitle(), equalTo(actual.getTitle()));
        }
        assertThat(expected.getRequirementIds(), equalTo(actual.getRequirementIds()));
        assertThat(expected.getDescription(), equalTo(actual.getDescription()));
        assertThat(expected.getType().getTitle(), equalTo(actual.getType().getTitle()));
        assertThat(expected.getComponentTitle(), equalTo(actual.getComponentTitle()));
        assertThat(expected.getPriority().getTitle(), equalTo(actual.getPriority().getTitle()));
        assertThat(expected.getGroups().size(), equalTo(expected.getGroups().size()));
        assertThat(expected.getContexts().size(), equalTo(actual.getContexts().size()));
        assertThat(expected.getPrecondition(), equalTo(actual.getPrecondition()));
        assertThat(expected.getExecutionType().getTitle(), equalTo(actual.getExecutionType().getTitle()));
        assertThat(expected.getTestCaseStatus().getTitle(), equalTo(actual.getTestCaseStatus().getTitle()));
        assertThat(expected.getFeature().getName(), equalTo(actual.getFeature().getName()));
        checkTestSteps(expected.getTestSteps(), actual.getTestSteps());
    }

    private void checkTestSteps(List<TestStepInfo> expected, List<TestStepInfo> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertThat(expected.get(i).getName(), equalTo(actual.get(i).getName()));
            assertThat(expected.get(i).getData(), equalTo(actual.get(i).getData()));
            for (int j = 0; j < expected.get(i).getVerifies().size(); j++) {
                assertThat(expected.get(i).getVerifies().get(j).getName(), equalTo(actual.get(i).getVerifies().get(j).getName()));
            }
        }
    }

    protected TestCampaignInfo createTestCampaign(String name, ProductType productType, DropType dropType, Set<FeatureType> featureType) {
        TestCampaignInfo testCampaignInfo = new TestCampaignInfo();
        testCampaignInfo.setName(name);
        testCampaignInfo.setProduct(productType.getProductInfo());
        testCampaignInfo.setDrop(dropType.getDropInfo());

        Set<FeatureInfo> featureCollect = featureType.stream().map(c -> c.getFeatureInfo()).collect(Collectors.toSet());

        testCampaignInfo.setFeatures(featureCollect);
        return testCampaignInfo;
    }
}
