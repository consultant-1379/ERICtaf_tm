/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersion;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseVersionRepository;
import com.googlecode.genericdao.search.Search;

import java.util.List;

@Repository
public class TestCaseVersionRepositoryImpl extends BaseRepositoryImpl<TestCaseVersion, Long>
        implements TestCaseVersionRepository {

    @Override
    public List<TestCaseVersion> findByTestCase(String testCaseId) {
        Search search = new Search(TestCaseVersion.class);
        filterByAnyId(testCaseId, search);
        search.addSortDesc("majorVersion");
        search.addSortDesc("minorVersion");
        return this.search(search);
    }

    @Override
    public TestCaseVersion findByTestCaseAndSequence(long id, long majorVersion, long minorVersion) {
        Search search = new Search(TestCaseVersion.class);
        search.addFilterEqual("testCase.id", id);
        search.addFilterEqual("majorVersion", majorVersion);
        search.addFilterEqual("minorVersion", minorVersion);
        return searchUnique(search);
    }

    @Override
    public TestCaseVersion findByTestCaseAndSequence(String testCaseId, long majorVersion, long minorVersion) {
        Search search = new Search(TestCaseVersion.class);
        filterByAnyId(testCaseId, search);
        search.addFilterEqual("majorVersion", majorVersion);
        search.addFilterEqual("minorVersion", minorVersion);
        return searchUnique(search);
    }

    private void filterByAnyId(String testCaseId, Search search) {
        try {
            long id = Long.parseLong(testCaseId);
            search.addFilterEqual("testCase.id", id);
        } catch (NumberFormatException e) {
            search.addFilterEqual("testCase.testCaseId", testCaseId);
        }
    }

}
