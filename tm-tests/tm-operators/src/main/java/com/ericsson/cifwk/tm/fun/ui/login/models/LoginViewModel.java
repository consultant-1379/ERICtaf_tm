/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.login.models;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;

public class LoginViewModel extends GenericViewModel {

    @UiComponentMapping(".eaLogin-body.eaLogin-login")
    private UiComponent loginHolder;

    @UiComponentMapping(".eaLogin-Holder-title")
    private UiComponent title;

    @UiComponentMapping(".eaLogin-loginUsername")
    private TextBox usernameField;

    @UiComponentMapping(".eaLogin-loginPassword")
    private TextBox passwordField;

    @UiComponentMapping(".eaLogin-formButton")
    private Button submitButton;

    @UiComponentMapping(".eaLogin-messagesBox")
    private Button messagesBox;

    public UiComponent getLoginHolder() {
        return loginHolder;
    }

    public UiComponent getTitle() {
        return title;
    }

    public TextBox getUsernameField() {
        return usernameField;
    }

    public TextBox getPasswordField() {
        return passwordField;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getMessagesBox() {
        return messagesBox;
    }

}
