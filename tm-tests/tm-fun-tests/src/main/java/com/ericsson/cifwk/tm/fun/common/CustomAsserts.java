/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.common;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasResult;
import com.ericsson.cifwk.tm.fun.ui.tms.operator.results.HasWarning;

import static com.ericsson.cifwk.taf.assertions.TafAsserts.*;

public class CustomAsserts {

    private CustomAsserts() {
    }

    public static void checkTestStep(HasResult result) {
        if (!(result instanceof HasWarning)) {
            assertNull(result.getErrorMessage());
        }
        assertTrue(result.isSuccess());
    }

    public static void checkTestStepFailed(HasResult result) {
        assertFalse(result.isSuccess());
        assertNotNull(result.getErrorMessage());
    }

}
