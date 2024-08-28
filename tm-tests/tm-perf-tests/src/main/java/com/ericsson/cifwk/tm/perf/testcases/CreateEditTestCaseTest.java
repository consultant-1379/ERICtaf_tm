package com.ericsson.cifwk.tm.perf.testcases;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.tm.fun.ui.login.operator.LoginOperator;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.ProductType;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testcases.CreateEditTestCaseResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;

import static com.ericsson.cifwk.tm.perf.common.CustomAsserts.checkTestStep;

public class CreateEditTestCaseTest extends TafTestBase {

    private Host host;

    @BeforeClass
    public void setUp() {
        host = DataHandler.getHostByName("tm_test");
    }

    @Test
    public void createTestCase() {
        LoginOperator loginOperator = new RestLoginOperator();
        loginOperator.start(host);

        RestTestManagementOperator tmOperator = new RestTestManagementOperator();
        tmOperator.start(host);

        checkTestStep(loginOperator.login());

        InputStream createTestCaseJson = getClass().getClassLoader().getResourceAsStream("data/createTestCase_fullData.json");
        CreateEditTestCaseResult createResult = tmOperator.createTestCase(createTestCaseJson, ProductType.ENM.getProductInfo());
        checkTestStep(createResult);

        InputStream editTestCaseJson = getClass().getClassLoader().getResourceAsStream("data/editTestCase_fullData.json");
        checkTestStep(tmOperator.editTestCase(createResult.getSavedTestCaseInfo(), editTestCaseJson));

        checkTestStep(loginOperator.logout());
    }
}
