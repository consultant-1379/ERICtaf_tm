package com.ericsson.cifwk.tm.fun.common;

import com.ericsson.cifwk.tm.fun.ui.login.operator.LoginOperator;
import com.ericsson.cifwk.tm.fun.ui.login.operator.RestLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.RestTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases.TestCaseFixturesOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases.TestCaseFixturesOperatorImpl;

import javax.inject.Inject;

/**
 * @author egergle
 */
public class RestOperators {

    @Inject
    private RestLoginOperator loginOperator;

    @Inject
    private RestTestManagementOperator tmOperator;

    @Inject
    private TestCaseFixturesOperatorImpl testCaseFixturesOperator;

    public LoginOperator getLoginOperator() {
        return loginOperator;
    }

    public RestTestManagementOperator getTestManagementOperator() {
        return tmOperator;
    }

    public TestCaseFixturesOperator getTestCaseFixturesOperator() {
        return testCaseFixturesOperator;
    }

}
