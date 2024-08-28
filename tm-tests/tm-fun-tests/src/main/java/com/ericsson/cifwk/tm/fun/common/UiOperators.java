package com.ericsson.cifwk.tm.fun.common;

import com.ericsson.cifwk.tm.fun.ui.login.operator.LoginOperator;
import com.ericsson.cifwk.tm.fun.ui.login.operator.UiLoginOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.UiTestManagementOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.admin.UiTestManagementAdminOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.notifications.NotificationFixturesOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.notifications.NotificationFixturesOperatorImpl;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases.TestCaseFixturesOperator;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.testCases.TestCaseFixturesOperatorImpl;

import javax.inject.Inject;

/**
 * @author egergle
 */
public class UiOperators {

    @Inject
    private UiLoginOperator loginOperator;

    @Inject
    private UiTestManagementOperator tmOperator;

    @Inject
    private NotificationFixturesOperatorImpl notificationOperator;

    @Inject
    private TestCaseFixturesOperatorImpl testCaseFixturesOperator;

    @Inject
    private UiTestManagementAdminOperator uiTestManagementAdminOperator;

    public LoginOperator getLoginOperator() {
        return loginOperator;
    }

    public NotificationFixturesOperator getNotificationOperator() {
        return notificationOperator;
    }

    public UiTestManagementOperator getTestManagementOperator() {
        return tmOperator;
    }

    public TestCaseFixturesOperator getTestCaseFixturesOperator() {
        return testCaseFixturesOperator;
    }

    public UiTestManagementAdminOperator getUiTestManagementAdminOperator() {
        return uiTestManagementAdminOperator;
    }

}
