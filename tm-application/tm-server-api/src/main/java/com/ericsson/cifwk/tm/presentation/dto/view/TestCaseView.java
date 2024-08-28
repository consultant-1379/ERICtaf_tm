/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto.view;

import com.ericsson.cifwk.tm.presentation.dto.TestCaseInfo;

public final class TestCaseView {

    private TestCaseView() {
    }

    public static final class Simple implements DtoView<TestCaseInfo> {
    }

    public static final class SimpleRequirements implements DtoView<TestCaseInfo> {
    }

    public static final class Detailed implements DtoView<TestCaseInfo> {
    }

    public static final class Version implements DtoView<TestCaseInfo> {
    }
}
