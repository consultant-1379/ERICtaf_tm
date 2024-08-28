package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper;

public abstract class TreeVisitor<V, R> {
    public void preVisitChildren() {
    }

    public void preVisitNode() {
    }

    public R visitNode(V value) {
        return null;
    }

    public void postVisitNode() {
    }

    public void postVisitChildren() {
    }
}
