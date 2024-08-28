/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BuildConfigurationTest {

    private BuildConfiguration buildConfiguration;

    @Before
    public void setUp() {
        buildConfiguration = new BuildConfiguration();
    }

    @Test
    public void testRemovePlaceholder() throws Exception {
        assertThat(buildConfiguration.removePlaceholder("${x.y}"), equalTo(""));
        assertThat(buildConfiguration.removePlaceholder("a${x.y}b"), equalTo("ab"));
    }
}
