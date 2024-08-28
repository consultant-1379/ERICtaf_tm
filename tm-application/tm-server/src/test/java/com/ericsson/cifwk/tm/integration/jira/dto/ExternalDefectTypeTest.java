/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.dto;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ExternalDefectTypeTest {

    @Test
    public void testFromName() {
        assertThat(ExternalDefectType.fromName("bug"), equalTo(ExternalDefectType.BUG));
        assertThat(ExternalDefectType.fromName("BUG"), equalTo(ExternalDefectType.BUG));
        assertThat(ExternalDefectType.fromName("Bug"), equalTo(ExternalDefectType.BUG));

        assertThat(ExternalDefectType.fromName("TR"), equalTo(ExternalDefectType.TR));
        assertThat(ExternalDefectType.fromName("tr"), equalTo(ExternalDefectType.TR));
        assertThat(ExternalDefectType.fromName("Tr"), equalTo(ExternalDefectType.TR));

        assertThat(ExternalDefectType.fromName("Bugz"), equalTo(ExternalDefectType.UNKNOWN));
        assertThat(ExternalDefectType.fromName("Trz"), equalTo(ExternalDefectType.UNKNOWN));
        assertThat(ExternalDefectType.fromName(null), equalTo(ExternalDefectType.UNKNOWN));
        assertThat(ExternalDefectType.fromName(""), equalTo(ExternalDefectType.UNKNOWN));
    }
}
