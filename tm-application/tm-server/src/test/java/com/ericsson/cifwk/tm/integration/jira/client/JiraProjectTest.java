/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.integration.jira.client;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public final class JiraProjectTest {
    private Map<Object, Object> map;
    private Map<Object, Object> map2;
    private Map<Object, Object> map3;

    @Before
    public void setUp() {
        map = Maps.newHashMap();
        map.put("key", "correctKey");
        map.put("name", "correctName");

        map2 = Maps.newHashMap();
        map2.put("key", "correctKey");

        map3 = Maps.newHashMap();
        map3.put("key", "correctKey2");
    }

    @Test
    public void testFromMap() {
        JiraProject project = JiraProject.fromMap(map);

        assertThat(project.getKey(), is("correctKey"));
        assertThat(project.getName(), is("correctName"));
    }

    @Test
    public void testEquals_WhenKeysAreSame_ShouldBeEqual() {
        JiraProject project = JiraProject.fromMap(map);
        JiraProject project2 = JiraProject.fromMap(map2);

        assertEquals(project, project2);
    }

    @Test
    public void testEquals_WhenKeyDiffers_ShouldNotEqual() {
        JiraProject project = JiraProject.fromMap(map);
        JiraProject project2 = JiraProject.fromMap(map3);

        assertNotEquals(project, project2);
    }
}
