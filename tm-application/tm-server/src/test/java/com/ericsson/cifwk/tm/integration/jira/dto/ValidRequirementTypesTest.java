package com.ericsson.cifwk.tm.integration.jira.dto;
/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ValidRequirementTypesTest {
    @Test
    public void testToJqlString() throws Exception {
        assertThat(ValidRequirementTypes.toJqlString(),
                equalTo("issuetype = Story or issuetype = Epic or issuetype = MR or issuetype = Improvement"));
    }

    @Test
    public void testIsValid() throws Exception {
        assertThat(ValidRequirementTypes.isValid("Epic"), equalTo(true));
        assertThat(ValidRequirementTypes.isValid("EPIC"), equalTo(true));
        assertThat(ValidRequirementTypes.isValid("Story"), equalTo(true));
        assertThat(ValidRequirementTypes.isValid("MR"), equalTo(true));
        assertThat(ValidRequirementTypes.isValid("Sub-task"), equalTo(false));
    }
}
