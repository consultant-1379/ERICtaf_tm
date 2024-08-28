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
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCampaignGroupRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;

@Repository
public class TestCampaignGroupRepositoryImpl extends BaseRepositoryImpl<TestCampaignGroup, Long> implements TestCampaignGroupRepository {

    public static final String NAME = "name";
    public static final String PRODUCT_ID = "product.id";

    @Override
    public TestCampaignGroup findNameAndProduct(String name, Long product_id) {
        Search search = new Search(TestCampaignGroup.class);
        search.addFilterEqual(NAME, name);
        search.addFilterEqual(PRODUCT_ID, product_id);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }
}
