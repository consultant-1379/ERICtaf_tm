package com.ericsson.cifwk.tm.fun.ui.tms.models.userinbox;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

import java.util.List;

public class AssignmentsRegionViewModel extends GenericViewModel {

    public static final String TMS_ASSIGNMENTS_TABLE_ID = "#TMS_UserInbox_AssignmentsTable";
    public static final String TMS_ASSIGNMENTS_HOLDER_ID = "#TMS_UserInbox_Assignments";

    @UiComponentMapping(TMS_ASSIGNMENTS_HOLDER_ID)
    private UiComponent assignmentsHolder;

    @UiComponentMapping(TMS_ASSIGNMENTS_TABLE_ID)
    private UiComponent table;

    @UiComponentMapping(TMS_ASSIGNMENTS_TABLE_ID + " > tbody > tr.ebTableRow")
    private List<UiComponent> tableRows;

    public UiComponent getAssignmentsHolder() {
        return assignmentsHolder;
    }

    public UiComponent getTable() {
        return table;
    }

    public List<UiComponent> getTableRows() {
        return tableRows;
    }
}
