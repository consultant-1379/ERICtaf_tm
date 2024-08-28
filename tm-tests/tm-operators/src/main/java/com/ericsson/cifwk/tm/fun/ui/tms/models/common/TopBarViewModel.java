/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.fun.ui.tms.models.common;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;
import com.ericsson.cifwk.taf.ui.sdk.Link;

public class TopBarViewModel extends GenericViewModel {

    @UiComponentMapping(".eaContainer-SystemBarHolder")
    private UiComponent systemBarHolder;

    @UiComponentMapping(".eaTmsUserButton")
    private Link userLink;

    @UiComponentMapping(".eaTmsUserButton-inboxOption-anchor")
    private Link inboxLink;

    @UiComponentMapping(".eaTmsUserButton-adminOption-anchor")
    private Link adminLink;

    @UiComponentMapping(".eaTmsUserButton-logout-anchor")
    private Link logoutLink;

    @UiComponentMapping(".ebTooltip-contentText")
    private UiComponent toolTip;

    public UiComponent getSystemBarHolder() {
        return systemBarHolder;
    }

    public Link getUserLink() {
        return userLink;
    }

    public Link getInboxLink() {
        return inboxLink;
    }

    public Link getLogoutLink() {
        return logoutLink;
    }

    public UiComponent getToolTip() {
        return toolTip;
    }

    public Link getAdminLink() {
        return adminLink;
    }
}
