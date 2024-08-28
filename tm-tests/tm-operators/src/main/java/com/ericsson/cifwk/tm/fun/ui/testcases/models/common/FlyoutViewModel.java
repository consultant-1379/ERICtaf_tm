/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.fun.ui.testcases.models.common;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class FlyoutViewModel extends GenericViewModel {

    @UiComponentMapping(".eaFlyout")
    private UiComponent flyout;

    @UiComponentMapping(".eaFlyout-panelContents")
    private UiComponent flyoutContents;

    @UiComponentMapping(".eaFlyout-panelCloseIcon")
    private UiComponent flyoutCloseIcon;

    public UiComponent getFlyoutCloseIcon() {
        return flyoutCloseIcon;
    }

    public UiComponent getFlyoutContents() {
        return flyoutContents;
    }

    public UiComponent getFlyout() {
        return flyout;
    }
}
