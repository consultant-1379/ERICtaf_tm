package com.ericsson.cifwk.tm.domain.model.shared.search;

import com.googlecode.genericdao.search.Filter;

public final class SearchHelpers {

    private SearchHelpers() {
    }

    public static String toLikePattern(String searchPattern) {
        return "%" + searchPattern
                .replace("%", "\\%")
                .replace("_", "\\_")
                .replace("*", "%")
                .replace("?", "_") + "%";
    }

    public static Filter failFilter() {
        return Filter.custom("1 = 2");
    }

}
