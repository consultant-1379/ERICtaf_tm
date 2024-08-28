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

import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.search.Condition;

public class FilterInfo {

    private TableColumn tableColumn;
    private Condition condition;
    private String value;

    public FilterInfo(TableColumn tableColumn, Condition condition, String value) {
        this.tableColumn = tableColumn;
        this.condition = condition;
        this.value = value;
    }

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tableColumn.getValue()).append(condition.getValue()).append(value);
        return sb.toString();
    }

}
