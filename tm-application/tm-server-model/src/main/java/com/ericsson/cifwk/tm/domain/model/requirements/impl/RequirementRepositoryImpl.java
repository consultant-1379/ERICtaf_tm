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
import com.ericsson.cifwk.tm.domain.cacheable.CacheableSearch;
import com.ericsson.cifwk.tm.domain.model.requirements.Requirement;
import com.ericsson.cifwk.tm.domain.model.requirements.RequirementRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.search.SearchHelpers;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class RequirementRepositoryImpl extends BaseRepositoryImpl<Requirement, Long> implements RequirementRepository {

    public static final String EXTERNAL_ID_FIELD = "externalId";
    public static final String EXTERNAL_PROJECT_ID_FIELD = "project.externalId";
    public static final String EXTERNAL_TYPE = "externalType";

    @Override
    public Requirement findByExternalId(String externalId) {
        Search search = new CacheableSearch(Requirement.class);
        search.addFilterEqual(EXTERNAL_ID_FIELD, externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Requirement findByExternalIdAndProjectId(String externalId, String projectId) {
        Search search = new CacheableSearch(Requirement.class);
        search.addFilterEqual(EXTERNAL_ID_FIELD, externalId);
        if (projectId != null) {
            search.addFilterEqual(EXTERNAL_PROJECT_ID_FIELD, projectId);
        }

        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Requirement> findAllByExternalId(Set<String> externalIds) {
        Search search = new CacheableSearch(Requirement.class);
        search.addFilterIn(EXTERNAL_ID_FIELD, externalIds);
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Requirement> findTopLevel(String projectId) {
        Search search = new CacheableSearch(Requirement.class)
                .addFilterEmpty("parent")
                .addFilterNotNull("externalType") //Ignore invalid requirements
                .addSortAsc("externalId", true);

        if (projectId != null) {
            search.addFilterEqual(EXTERNAL_PROJECT_ID_FIELD, projectId);
        }

        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getAllJiraExternalIds() {
        Search search = new Search(Requirement.class);
        search.addField(EXTERNAL_ID_FIELD);
        search.addSortAsc("parent");
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Requirement> findMatchingExternalId(String externalId, List<String> filterType, int limit) {
        Search search = new Search(Requirement.class);
        search.addFilterILike(EXTERNAL_ID_FIELD, SearchHelpers.toLikePattern(externalId));

        if (!filterType.isEmpty()) {
            search.addFilterIn(EXTERNAL_TYPE, filterType);
        }

        search.addSortAsc(EXTERNAL_ID_FIELD);
        search.setMaxResults(limit);
        try {
            return search(search);
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

}
