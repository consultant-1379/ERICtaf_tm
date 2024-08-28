/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common;

import com.ericsson.cifwk.taf.annotations.VUserScoped;
import com.ericsson.cifwk.taf.ui.Browser;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@VUserScoped
public class BrowserTabHolder {

    private static final Logger logger = LoggerFactory.getLogger(BrowserTabHolder.class);

    private Browser browser;
    private BrowserTab browserTab;

    public void init() {
        logger.info("Opening new browser " + System.identityHashCode(this));
        browser = UI.newBrowser();
    }

    public void open(String uri) {
        browserTab = browser.open(uri);
    }

    public BrowserTab getBrowserTab() {
        logger.info("Using browser " + System.identityHashCode(this));
        if (browserTab == null) {
            throw new IllegalStateException();
        }
        return browserTab;
    }

}
