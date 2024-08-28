/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.common;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import java.util.concurrent.Callable;

public class TimingHelper {

    private TimingHelper() {
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static <T> T retry(Callable<T> callable, int retryCount, int timeoutSeconds) {
        Preconditions.checkArgument(retryCount >= 0, "Retry count was %s but expected non-negative", retryCount);
        try {
            return callable.call();
        } catch (Exception e) {
            if (retryCount > 0) {
                sleep(timeoutSeconds);
                return retry(callable, retryCount - 1, timeoutSeconds);
            } else {
                throw Throwables.propagate(e);
            }
        }
    }

}
