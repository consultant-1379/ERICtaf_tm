/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.domain.model.requirements.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.requirements.Defect;
import com.ericsson.cifwk.tm.domain.model.requirements.DefectRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class DefectRepositoryImpl extends BaseRepositoryImpl<Defect, Long> implements DefectRepository {

    public static final String EXTERNAL_ID = "externalId";

    @Override
    public Defect findByExternalId(String externalId) {
        Search search = new Search(Defect.class);
        search.addFilterEqual(EXTERNAL_ID, externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Defect> findAllByExternalId(Set<String> externalIds) {
        Search search = new Search(Defect.class);
        search.addFilterIn(EXTERNAL_ID, externalIds);
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Defect> findMatchingExternalId(String externalId, int limit) {
        Search search = new Search(Defect.class);
        search.addFilterILike(EXTERNAL_ID, SearchHelpers.toLikePattern(externalId));
        search.addSortAsc(EXTERNAL_ID);
        search.setMaxResults(limit);
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
