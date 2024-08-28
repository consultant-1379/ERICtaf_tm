/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.testcases.models.testcases.search;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class TestCaseSearchWidgetViewModel extends GenericViewModel {

    public static final String CHECK_BOX = ".ebCheckbox";
    public static final String SEARCH_RESULTS_TABLE="#TMS_TestCasesSearch_GenericTestCaseSearch";

    @UiComponentMapping(SEARCH_RESULTS_TABLE)
    private UiComponent testCaseTable;

    @UiComponentMapping("#TMS_TestCasesSearch_addButton")
    private Button addSelectedButton;

    public UiComponent getTestCaseTable() {
        return testCaseTable;
    }

    public Button getAddSelectedButton() {
        return addSelectedButton;
    }

}
