package com.ericsson.cifwk.tm.fun.ui.common;

import com.ericsson.cifwk.taf.ui.core.UiComponent;

import java.util.List;

/**
 * Created by egergle on 08/08/2016.
 */
public class TableHelper {

    public static final String TABLE_ROWS = ".elTablelib-Table-body tr";

    private TableHelper() {
    }

    public static UiComponent findTableRow(UiComponent table, String rowId) {
        List<UiComponent> rowList = table.getDescendantsBySelector("[data-id=\"" + rowId + "\"]");
        if (rowList.isEmpty() || rowList.size() > 1) {
            return null;
        }
        return rowList.get(0);
    }
}
