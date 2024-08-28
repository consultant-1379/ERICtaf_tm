/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.login.operator;

import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.TestStep;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentPredicates;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.ericsson.cifwk.taf.ui.sdk.Link;
import com.ericsson.cifwk.tm.fun.ui.common.BrowserTabHolder;
import com.ericsson.cifwk.tm.fun.ui.common.DynamicUser;
import com.ericsson.cifwk.tm.fun.ui.common.operator.UiCommonOperator;
import com.ericsson.cifwk.tm.fun.ui.login.models.LoginViewModel;
import com.ericsson.cifwk.tm.fun.ui.login.results.AuthenticationResult;
import com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.UserCredentials;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.DialogViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.common.TopBarViewModel;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operator
public class UiLoginOperator extends UiCommonOperator implements LoginOperator {

    private static final Logger logger = LoggerFactory.getLogger(UiLoginOperator.class);

    private Host host;
    private BrowserTabHolder browserTabHolder;

    @Override
    public void start(Host host) {
        this.host = host;

        String appUrl = getAppUrl(this.host);
        navigation.setup(appUrl);

        browserTabHolder = browserTabHolderProvider.get();
        browserTabHolder.init();
        browserTabHolder.open(appUrl);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    @TestStep(id = "loginWithUser", description = "login with user {0}")
    public AuthenticationResult loginWithUser(User user) {
        return loginInternal(user);
    }

    @Override
    @TestStep(id = "login", description = "login with 'taf' user")
    public AuthenticationResult loginWithDefaultUser() {
        List<User> users = host.getUsers(UserType.WEB);
        Optional<User> user = Iterables.tryFind(users, new Predicate<User>() {
            @Override
            public boolean apply(User input) {
                return "taf".equals(input.getUsername());
            }
        });
        Preconditions.checkArgument(user.isPresent(), "User 'taf' for login is not found!");
        return loginInternal(user.get());
    }

    @Override
    @TestStep(id = "login", description = "login with dynamic user")
    public AuthenticationResult login() {
        User dynamicUser = DynamicUser.create();
        return loginInternal(dynamicUser);
    }

    @Override
    @TestStep(id = "loginAsAdmin", description = "login with admin user")
    public AuthenticationResult loginAsAdmin() {
        User dynamicUser = DynamicUser.createAdmin();
        return loginInternal(dynamicUser);
    }

    private AuthenticationResult loginInternal(User user) {
        LoginViewModel loginViewModel = waitForLoginPage();
        UserCredentials userCredentials = new UserCredentials(user);

        loginViewModel.getUsernameField().setText(userCredentials.getUsername());
        loginViewModel.getPasswordField().setText(userCredentials.getPassword());

        loginViewModel.getSubmitButton().click();

        try {
            String error = waitForLoginError(loginViewModel);
            createScreenshot("loginWithUser_waitForLoginError", "Error message was appeared when tried to login.");
            if (user == null) {
                return AuthenticationResult.error(error, "No User Information");
            }
            return AuthenticationResult.error(error, user.getUsername());
        } catch (WaitTimedOutException ignored) {
            // Ignore
        }

        getBrowserTab().open(getAppUrl(host) + "/#tm/testCases");
        waitForTestCaseSearchView();
        return AuthenticationResult.success(true, user.getUsername());
    }

    @Override
    @TestStep(id = "logout")
    public AuthenticationResult logout() {
        TopBarViewModel topBarViewModel = getBrowserTab().getView(TopBarViewModel.class);
        Link userLink = topBarViewModel.getUserLink();
        UiComponent tooltip = topBarViewModel.getToolTip();
        getBrowserTab().waitUntilComponentIsDisplayed(userLink, WAIT_TIMEOUT);
        hideTooltip(tooltip, userLink);
        userLink.click();

        hideTooltip(tooltip, userLink);
        UiComponent logout = topBarViewModel.getLogoutLink();
        logout.focus();
        logout.click();

        DialogViewModel dialogScreenView = getBrowserTab().getView(DialogViewModel.class);
        getBrowserTab().waitUntilComponentIsDisplayed(dialogScreenView.getDialog(), WAIT_TIMEOUT);
        dialogScreenView.getSubmitBlueButton().click();

        try {
            waitForLoginPage();
        } catch (WaitTimedOutException e) {
            createScreenshot("logout_waitForLoginPage", "Logout was not proceeded.", e);
            return AuthenticationResult.error(e.getMessage(), null);
        }
        return AuthenticationResult.success(false, null);
    }

    private String waitForLoginError(LoginViewModel loginViewModel) {
        UiComponent messagesBox = loginViewModel.getMessagesBox();
        getBrowserTab().waitUntil(messagesBox, UiComponentPredicates.HAS_TEXT, WAIT_SHORT_TIMEOUT);
        return messagesBox.getText();
    }

    private LoginViewModel waitForLoginPage() {
        LoginViewModel loginViewModel = getBrowserTab().getView(LoginViewModel.class);
        waitForComponent(loginViewModel.getLoginHolder(), WAIT_LONG_TIMEOUT, "waitForLoginPage", "Login Page was not opened on time.");
        return loginViewModel;
    }

}
