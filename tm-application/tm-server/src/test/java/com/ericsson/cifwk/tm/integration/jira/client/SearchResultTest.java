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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SearchResultTest {

    private SearchResult searchResult;

    @Before
    public void setUp() {
        searchResult = new SearchResult();
    }

    @Test
    public void testGetPageCount_1() {
        searchResult.setTotalResults(100);
        searchResult.setResultsPerPage(20);
        assertThat(searchResult.getPageCount(), is(5));
    }

    @Test
    public void testGetPageCount_2() {
        searchResult.setTotalResults(101);
        searchResult.setResultsPerPage(20);
        assertThat(searchResult.getPageCount(), is(6));
    }

    @Test
    public void testGetPageCount_3() {
        searchResult.setTotalResults(95);
        searchResult.setResultsPerPage(20);
        assertThat(searchResult.getPageCount(), is(5));
    }

    @Test
    public void testGetPageCount_4() {
        searchResult.setTotalResults(0);
        searchResult.setResultsPerPage(0);
        assertThat(searchResult.getPageCount(), is(0));
    }

    @Test
    public void testGetPageCount_5() {
        searchResult.setTotalResults(0);
        searchResult.setResultsPerPage(20);
        assertThat(searchResult.getPageCount(), is(0));
    }

    @Test
    public void testGetPageCount_6() {
        searchResult.setTotalResults(1);
        searchResult.setResultsPerPage(20);
        assertThat(searchResult.getPageCount(), is(1));
    }
}
