/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common.operator;

import com.ericsson.cifwk.taf.annotations.Attachment;
import com.ericsson.cifwk.taf.configuration.TafConfigurationProvider;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.core.GenericPredicate;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.WaitTimedOutException;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.cifwk.tm.fun.ui.common.BrowserTabHolder;
import com.ericsson.cifwk.tm.fun.ui.common.Navigation;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.group.TestCampaignGroupRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcampaigns.list.TestCampaignsRegionViewModel;
import com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list.TestCaseSearchViewModel;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.inject.Provider;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public abstract class UiCommonOperator {

    protected static final String CONTEXT_PATH = TafConfigurationProvider.provide().getString("tm_context.path");

    protected static final String REPORTING_DIR_PATH = TafConfigurationProvider.provide().getString("taf.reporting.dir");

    public static final int WAIT_ULTRA_SHORT_TIMEOUT = 100;
    public static final int WAIT_SHORT_TIMEOUT = 5000;
    public static final int WAIT_TIMEOUT = 10000;
    public static final int WAIT_INT_TIMEOUT = 20000;
    public static final int WAIT_LONG_TIMEOUT = 35000;

    protected String screenShotDirPath;

    protected abstract Logger getLogger();

    @Inject
    protected Provider<BrowserTabHolder> browserTabHolderProvider;

    @Inject
    protected Navigation navigation;

    protected BrowserTab getBrowserTab() {
        BrowserTabHolder browserTabHolder = browserTabHolderProvider.get();
        return browserTabHolder.getBrowserTab();
    }

    protected String getAppUrl(Host host) {
        String hostAddress = host.getIp();
        String port = host.getPort().get(Ports.HTTP);

        return "http://" + hostAddress + ":" + port + CONTEXT_PATH;
    }

    protected void createScreenshot(String fileName, String errorMessage) {
        createScreenshot(fileName, errorMessage, null);
    }

    protected void createScreenshot(String fileName, String message, Exception e) {
        Date currentDate = new Date();
        String fullPath = screenShotDirPath + currentDate.getTime() + "_" + fileName + ".png";
        takeScreenshot(fullPath);

        if (e != null) {
            getLogger().error(message + " ::: ScreenShot taken and saved into '" + fullPath + "'.", e);
        } else {
            getLogger().warn(message + " ::: ScreenShot taken and saved into '" + fullPath + "'.");
        }
    }

    @Attachment(type = "image/png", value = "{1}.png")
    public byte[] takeScreenshot(String fullPath) {
        getBrowserTab().takeScreenshot(fullPath);
        try {
            return IOUtils.toByteArray(new FileInputStream(fullPath));
        } catch (IOException e) {
            String messageTemplate = "Exception reading file '%s': %s";
            String errorMessage = String.format(messageTemplate, fullPath, e.getMessage());
            return errorMessage.getBytes();
        }
    }

    protected synchronized String createScreenshotDir() {
        Path reportingDirPath = Paths.get(REPORTING_DIR_PATH + "screenshots");
        if (Files.isDirectory(reportingDirPath)) {
            return reportingDirPath.toString() + "/";
        }
        try {
            Path dirPath = Files.createDirectory(reportingDirPath);
            return dirPath.toString() + "/";
        } catch (FileAlreadyExistsException e) {
            getLogger().warn("FileAlreadyExistsException caught returning directory path");
            return reportingDirPath.toString() + "/";
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    protected void waitForComponent(UiComponent component, int waitTime, String methodName, String errorMessage) {
        try {
            getBrowserTab().waitUntilComponentIsDisplayed(component, waitTime);
        } catch (WaitTimedOutException e) {
            createScreenshot(methodName, errorMessage, e);
        }
    }

    protected TestCaseSearchViewModel waitForTestCaseSearchView() {
        TestCaseSearchViewModel mainScreenView = getBrowserTab().getView(TestCaseSearchViewModel.class);
        waitForComponent(mainScreenView.getResultsPanel(), WAIT_LONG_TIMEOUT, "waitForTestCaseSearchView", "Main Page was not opened on time.");
        return mainScreenView;
    }

    protected TestCampaignsRegionViewModel waitForTestCampaignRegionView() {
        TestCampaignsRegionViewModel mainScreenView = getBrowserTab().getView(TestCampaignsRegionViewModel.class);
        waitForComponent(mainScreenView.getTestCampaignsRegion(), WAIT_LONG_TIMEOUT, "waitForTestCaseSearchView", "Main Page was not opened on time.");
        return mainScreenView;
    }

    protected TestCampaignGroupRegionViewModel waitForTestCampaignGroupView() {
        TestCampaignGroupRegionViewModel view = getBrowserTab().getView(TestCampaignGroupRegionViewModel.class);
        waitForComponent(view.getTestCampaignGroupRegion(), WAIT_LONG_TIMEOUT, "waitForTestCampaignGroupView", "Test campaign group page was not opened on time.");
        return view;
    }

    protected Iterable<String> getClassList(UiComponent component) {
        String cssClasses = component.getProperty("class");
        return Splitter.on(' ')
                .trimResults()
                .omitEmptyStrings()
                .split(Strings.nullToEmpty(cssClasses));
    }

    protected void hideTooltip(UiComponent tooltip, UiComponent userLink) {
        userLink.createUiActions().mouseOver(userLink).perform();
        try {
            getBrowserTab().waitUntilComponentIsDisplayed(tooltip, WAIT_TIMEOUT);
            getBrowserTab().waitUntilComponentIsHidden(tooltip, WAIT_TIMEOUT);
        } catch (WaitTimedOutException e) { //NOSONAR
            getLogger().warn("Caught WaitTimedOutException", e);
            //do nothing and continue

        }
    }

    protected void waitForTableUpdate(final ViewModel viewModel, final String tableIdSelector) {
        try {
            getBrowserTab().waitUntil(new GenericPredicate() {
                @Override
                public boolean apply() {
                    List<UiComponent> viewComponents = viewModel.getViewComponents(tableIdSelector, UiComponent.class);
                    int size = viewComponents.size();
                    if (size != 1) {
                        return false;
                    }

                    UiComponent tableComponent = viewComponents.iterator().next();
                    return "true".equals(tableComponent.getProperty("data-loaded"));
                }
            }, WAIT_TIMEOUT);
        } catch (Exception e) {
            createScreenshot("waitForTableUpdate", "Table with ID "
                    + tableIdSelector + " is not shown or data is not loaded after " + WAIT_TIMEOUT + " ms.");
        }
    }
}
