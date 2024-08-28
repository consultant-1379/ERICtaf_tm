/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Set;

public final class Diff<T> {

    private final Set<T> added;
    private final Set<T> skipped;

    public Diff(Collection<T> added, Collection<T> skipped) {
        this.added = Sets.newLinkedHashSet(added);
        this.skipped = Sets.newLinkedHashSet(skipped);
    }

    public Set<T> getAdded() {
        return added;
    }

    public Set<T> getSkipped() {
        return skipped;
    }

}
