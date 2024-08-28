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

import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.tm.fun.common.BaseFuncTest;
import com.ericsson.cifwk.tm.fun.common.CustomAsserts;
import org.testng.annotations.Test;

public class AuthenticationTest extends BaseFuncTest {

    @Test
    @TestId(id="CIP-4701_Func_1", title="Success Login/Logout")
    public void successLoginLogout() {
        createUiOperators();
        CustomAsserts.checkTestStep(loginOperator.login());
        CustomAsserts.checkTestStep(loginOperator.logout());
    }

    @Test
    @TestId(id="CIP-4701_Func_2", title="Try to Login with a nonexistent user")
    public void errorLogin() {
        createUiOperators();
        User user = hostHelper.getUserByName(UserType.WEB, "unknown");
        CustomAsserts.checkTestStepFailed(loginOperator.loginWithUser(user));
    }

    @Test
    @TestId(id="CIP-4701_Func_3", title="Try to Login with empty fields")
    public void emptyFieldsLogin() {
        createUiOperators();
        CustomAsserts.checkTestStepFailed(loginOperator.loginWithUser(null));
    }

}
