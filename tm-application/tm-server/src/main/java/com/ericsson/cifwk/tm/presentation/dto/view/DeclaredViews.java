/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.dto.view;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class DeclaredViews<T> {

    private final Map<String, Class<? extends DtoView<T>>> views;

    public DeclaredViews(Map<String, Class<? extends DtoView<T>>> views) {
        this.views = views;
    }

    @SuppressWarnings("unchecked")
    public static <T> DeclaredViews<T> of(Class<?> viewClass) {
        Class<?>[] declaredClasses = viewClass.getDeclaredClasses();
        ImmutableMap.Builder<String, Class<? extends DtoView>> viewsBuilder = ImmutableMap.builder();
        for (Class<?> declaredClass : declaredClasses) {
            if (DtoView.class.isAssignableFrom(declaredClass)) {
                String name = declaredClass.getSimpleName().toLowerCase();
                viewsBuilder.put(name, (Class<? extends DtoView>) declaredClass);
            }
        }
        return new DeclaredViews(viewsBuilder.build());
    }

    public Class<? extends DtoView<T>> find(String name) {
        if (name == null) {
            return null;
        }
        return views.get(name.replace("-", "").toLowerCase());
    }

}
