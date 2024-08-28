/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.models.testcases.list;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class AdvancedSearchBlockViewModel extends GenericViewModel {

    public static final String TEST_CASES_SEARCH_FULL_BLOCK = ".eaTM-TestCasesSearch";

    public static final String FIELD_SELECT_BOX = "#TMS_TestCaseSearch_Criterion-fieldSelectBox-";
    public static final String CONDITION_SELECT_BOX = "#TMS_TestCaseSearch_Criterion-conditionSelectBox-";

    public static final String CRITERION_ADD_BLOCK = ".eaTM-Criterions-addBlock";
    public static final String CRITERION_INPUT = ".eaTM-Criterion-valueInput";
    public static final String CRITERION_LIST_BLOCK = ".eaTM-Criterions-list";
    public static final String CRITERION_LIST = ".eaTM-Criterion";
    public static final String SEARCH_BUTTON = ".eaTM-Search-button";

    @UiComponentMapping("#TMS_TestCaseSearch_ContentRegion-searchPanel")
    private UiComponent searchPanel;

    @UiComponentMapping(".eaTM-TestCasesSearch")
    private UiComponent commonSearchPanel;

    public UiComponent getSearchPanel() {
        return searchPanel;
    }

    public UiComponent getCommonSearchPanel() {
        return commonSearchPanel;
    }
}
