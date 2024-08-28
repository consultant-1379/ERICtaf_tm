/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.shared;

import com.ericsson.cifwk.tm.domain.model.testdesign.Priority;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestExecutionType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseStatus;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ReferenceHelperTest {

    @Test
    public void testParseEnumById() {
        assertThat(ReferenceHelper.parseEnumByName(null, TestCaseStatus.class), equalTo(null));
        assertThat(ReferenceHelper.parseEnumById(1, TestExecutionType.class), equalTo(TestExecutionType.MANUAL));
        assertThat(ReferenceHelper.parseEnumById(2, TestExecutionType.class), equalTo(TestExecutionType.AUTOMATED));
        assertThat(ReferenceHelper.parseEnumById(3, TestExecutionType.class), equalTo(null));
    }

    @Test
    public void testParseEnumByName() {
        assertThat(ReferenceHelper.parseEnumByName(null, Priority.class), equalTo(null));
        assertThat(ReferenceHelper.parseEnumByName("Normal", Priority.class), equalTo(Priority.NORMAL));
        assertThat(ReferenceHelper.parseEnumByName("NORMAL", Priority.class), equalTo(Priority.NORMAL));
    }

}
