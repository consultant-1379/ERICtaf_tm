package com.ericsson.cifwk.tm.presentation.dto;

import java.util.ArrayList;
import java.util.List;

public class QueryInfo {

    private List<CriterionInfo> criteria = new ArrayList<>();
    private SortInfo sortBy = null;

    public QueryInfo() {
    }

    public List<CriterionInfo> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<CriterionInfo> criteria) {
        this.criteria = criteria;
    }

    public SortInfo getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortInfo sortBy) {
        this.sortBy = sortBy;
    }
}
