package com.ericsson.cifwk.tm.domain.model.shared.search;

import static com.googlecode.genericdao.search.Filter.*;

public enum QueryOperator {

    EQUAL("=", OP_EQUAL),
    NOT_EQUAL("!=", OP_NOT_EQUAL),
    LIKE_CI("~", OP_ILIKE);

    private final String syntax;
    private final int filterType;

    QueryOperator(String syntax, int filterType) {
        this.syntax = syntax;
        this.filterType = filterType;
    }

    public static QueryOperator parse(String syntax) {
        for (QueryOperator operator : values()) {
            if (operator.getSyntax().equals(syntax)) {
                return operator;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getSyntax();
    }

    public String getSyntax() {
        return syntax;
    }

    public int getFilterType() {
        return filterType;
    }

    public boolean hasSyntax(String syntax) {
        return this.syntax.equals(syntax);
    }

}
