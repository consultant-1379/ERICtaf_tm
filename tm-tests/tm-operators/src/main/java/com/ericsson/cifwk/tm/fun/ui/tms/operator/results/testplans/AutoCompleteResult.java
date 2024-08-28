package com.ericsson.cifwk.tm.fun.ui.tms.operator.results.testplans;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;

import java.util.List;

public class AutoCompleteResult implements HasResult {

    private final boolean success;
    private final String errorMsg;
    private List<String> autocompleteItems;

    public AutoCompleteResult(boolean success, List<String> autocompleteItems, String errorMsg) {
        this.success = success;
        this.autocompleteItems = autocompleteItems;
        this.errorMsg = errorMsg;
    }

    public static AutoCompleteResult success(List<String> autocompleteItems) {
        return new AutoCompleteResult(true, autocompleteItems, null);
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getErrorMessage() {
        return errorMsg;
    }

    public List<String> getAutocompleteItems() {
        return autocompleteItems;
    }
}
