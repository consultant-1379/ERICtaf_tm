/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries.csv;

import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestCaseCSVConverterTest {

    private TestCaseCSVConverter testCaseCSVConverter;

    @Before
    public void setUp() {
        testCaseCSVConverter = new TestCaseCSVConverter();
    }

    @Test
    public void testGetDefault() throws Exception {
        String title = "Test-title";
        String testCaseId = "test-1";
        String result = "Test-Pass";
        String comment = "Test-comment";
        String empty = "";
        String priority = "High";
        String type = "Functional";
        String[] scopes = {"Robustness", "Local", "KGB"};
        String[] contexts = {"REST", "UI", "CLI"};
        String component = "sample component";
        String executionType = "Manual";
        String feature = "FM";
        String team = "daisy";
        String suite = "test suite";
        String username = "John Doe";

        String joinedScopes = Joiner.on(", ").join(scopes);
        String joinedContexts = Joiner.on(", ").join(contexts);

        TestCaseInfo testCaseInfo = new TestCaseInfo();
        testCaseInfo.setTestCaseId(testCaseId);
        testCaseInfo.setTitle(title);
        testCaseInfo.setComment(comment);
        testCaseInfo.setPriority(new ReferenceDataItem("", priority));
        testCaseInfo.setType(new ReferenceDataItem("", type));
        for (String scopeName : scopes) {
            testCaseInfo.addGroup(new ReferenceDataItem("", scopeName));
        }
        for (String contextName : contexts) {
            testCaseInfo.addContext(new ReferenceDataItem("", contextName));
        }
        testCaseInfo.addTechnicalComponent(new ReferenceDataItem("", component));
        testCaseInfo.setExecutionType(new ReferenceDataItem("", executionType));
        testCaseInfo.setTestSuite(new ReferenceDataItem(null, suite));
        testCaseInfo.setTestTeam(new ReferenceDataItem(null, team));

        FeatureInfo featureInfo = new FeatureInfo(null, feature);
        testCaseInfo.setFeature(featureInfo);

        TestCampaignItemInfo testPlanItemInfo = new TestCampaignItemInfo();
        testPlanItemInfo.setTestCase(testCaseInfo);
        testPlanItemInfo.setResult(new ReferenceDataItem("", result));

        UserInfo user = new UserInfo();
        user.setUserName(username);
        testPlanItemInfo.setUser(user);

        String[] data = testCaseCSVConverter.convertEntry(testPlanItemInfo);
        assertEquals(testCaseId, data[0]);
        assertEquals(title, data[1]);
        assertEquals(username, data[2]);
        assertEquals(result, data[3]);
        assertEquals(comment, data[4]);
        assertEquals(empty, data[5]);
        assertEquals(priority, data[6]);
        assertEquals(type, data[7]);
        assertEquals(joinedScopes, data[8]);
        assertEquals(feature, data[9]);
        assertEquals(component, data[10]);
        assertEquals(executionType, data[11]);
        assertEquals(joinedContexts, data[12]);
    }
}
