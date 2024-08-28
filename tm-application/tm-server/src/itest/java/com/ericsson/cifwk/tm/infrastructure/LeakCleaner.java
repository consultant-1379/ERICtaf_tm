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

import com.google.common.base.Throwables;
import com.googlecode.genericdao.search.hibernate.HibernateMetadataUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Deals with ill-behaved code that becomes an issue
 * during regular server/injector restarts in integration tests.
 */
final class LeakCleaner {

    private LeakCleaner() {
    }

    public static void clean() {
        clearMetadataUtil();
    }

    /**
     * Removes static references to {@link org.hibernate.SessionFactory}.
     */
    private static void clearMetadataUtil() {
        try {
            Field mapField = HibernateMetadataUtil.class.getDeclaredField("map");
            mapField.setAccessible(true);
            Map map = (Map) mapField.get(null);
            map.clear();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw Throwables.propagate(e);
        }
    }

}
