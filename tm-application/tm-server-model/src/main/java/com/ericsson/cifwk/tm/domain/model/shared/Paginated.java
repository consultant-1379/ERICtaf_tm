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

import java.util.List;

public final class Paginated<T> {

    private final int page;
    private final int perPage;
    private final List<T> results;
    private final long total;

    public Paginated(int page, int perPage, List<T> results, long total) {
        this.page = page;
        this.perPage = perPage;
        this.results = results;
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public List<T> getResults() {
        return results;
    }

    public long getTotal() {
        return total;
    }

}
