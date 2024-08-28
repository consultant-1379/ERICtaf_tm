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

import java.util.List;

public class TableResultsViewModel extends GenericViewModel {

    public static final String TMS_TEST_CASES_LIST_RESULTS_ID = "#TMS_TestCaseSearch_ListResults";
    public static final String TMS_TEST_CASES_TABLE_ID = "#TMS_TestCaseSearch_ListResultsTable";

    @UiComponentMapping(TMS_TEST_CASES_LIST_RESULTS_ID)
    private UiComponent listResultsBlock;

    @UiComponentMapping(TMS_TEST_CASES_TABLE_ID)
    private UiComponent table;

    @UiComponentMapping(TMS_TEST_CASES_TABLE_ID + " > tbody")
    private UiComponent tableBody;

    @UiComponentMapping(TMS_TEST_CASES_TABLE_ID + " > tbody > tr.ebTableRow")
    private List<UiComponent> tableRows;

    @UiComponentMapping("td")
    private List<UiComponent> cells;

    public UiComponent getListResultsBlock() {
        return listResultsBlock;
    }

    public UiComponent getTable() {
        return table;
    }

    public UiComponent getTableBody() {
        return tableBody;
    }

    public List<UiComponent> getTableRows() {
        return tableRows;
    }

    public List<UiComponent> getCells() {
        return cells;
    }
}
