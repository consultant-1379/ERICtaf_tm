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
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroupRepository;
import com.googlecode.genericdao.search.Search;

import java.util.List;

@Repository
public class ReviewGroupRepositoryImpl extends BaseRepositoryImpl<ReviewGroup, Long>
        implements ReviewGroupRepository {

    @Override
    public ReviewGroup findByGroupName(String name) {
        Search search = new Search(ReviewGroup.class);
        search.addFilterEqual("name", name);
        return this.searchUnique(search);
    }

    @Override
    public List<ReviewGroup> findMatchingGroup(String name, int limit) {
        Search search = new Search(ReviewGroup.class);
        search.addFilterILike("name", SearchHelpers.toLikePattern(name));
        search.setMaxResults(limit);
        return this.search(search);
    }
}
