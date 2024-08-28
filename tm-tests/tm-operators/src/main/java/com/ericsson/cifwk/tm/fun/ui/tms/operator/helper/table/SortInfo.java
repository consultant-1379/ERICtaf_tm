/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.table;

public class SortInfo {

    private TableColumn tableColumn;
    private boolean desc;

    public SortInfo(TableColumn tableColumn, boolean desc) {
        this.tableColumn = tableColumn;
        this.desc = desc;
    }

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    public boolean getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("orderBy=").append(tableColumn.getValue());
        sb.append("&orderMode=").append(desc ? "desc" : "asc");
        return sb.toString();
    }

}
