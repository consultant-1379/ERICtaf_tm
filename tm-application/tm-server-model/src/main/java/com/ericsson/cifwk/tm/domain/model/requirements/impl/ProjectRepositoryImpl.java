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
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

@Repository
public class ProjectRepositoryImpl extends BaseRepositoryImpl<Project, Long> implements ProjectRepository {

    @Override
    public Project findByExternalId(String externalId) {
        if (externalId == null) {
            return null;
        }

        Search search = new Search(Project.class);
        search.addFilterEqual("externalId", externalId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Project findByExternalIdAndName(String externalId, String name) {
        if (externalId == null || name == null) {
            return null;
        }

        Search search = new Search(Project.class);
        search.addFilterEqual("externalId", externalId);
        search.addFilterEqual("name", name);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Map<Long, String> findAllIdsToExternalIds() {
        Map<Long, String> result = new HashMap<>();
        Search searchQuery = new Search(Project.class);
        searchQuery.addField("id");
        searchQuery.addField("externalId");
        searchQuery.setResultMode(ISearch.RESULT_LIST);
        List<List<Object>> searchResult = search(searchQuery);
        for (List<Object> fields : searchResult) {
            checkState(fields.size() == 2, "Search should return two fields");
            result.put((Long) fields.get(0), (String) fields.get(1));
        }
        return result;
    }

}
