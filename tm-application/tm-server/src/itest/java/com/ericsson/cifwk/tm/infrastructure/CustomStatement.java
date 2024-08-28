/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure;

import org.junit.runners.model.Statement;

public class CustomStatement extends Statement {

    private final Runnable runnable;
    private final Statement next;

    public CustomStatement(Statement next, Runnable runnable) {
        this.next = next;
        this.runnable = runnable;
    }

    @Override
    public void evaluate() throws Throwable {
        next.evaluate();
        runnable.run();
    }
}
