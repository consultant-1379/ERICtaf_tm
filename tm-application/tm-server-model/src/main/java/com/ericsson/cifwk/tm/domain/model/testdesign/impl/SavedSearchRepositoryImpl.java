package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearch;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearchRepository;
import com.googlecode.genericdao.search.Search;

import javax.persistence.NoResultException;

/**
 * Created by egergle on 24/01/2017.
 */
public class SavedSearchRepositoryImpl extends BaseRepositoryImpl<SavedSearch, Long> implements SavedSearchRepository {
    @Override
    public SavedSearch getNameAndProfileId(Long profileId, String name) {
        Search search = new Search(SavedSearch.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("userProfile.id", profileId);
        try {
            return searchUnique(search);
        } catch (NoResultException e) {
            return null;
        }
    }
}
