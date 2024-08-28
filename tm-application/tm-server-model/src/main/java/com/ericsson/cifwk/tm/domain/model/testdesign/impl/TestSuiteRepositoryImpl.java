package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuiteRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.googlecode.genericdao.search.Search;

import java.util.Optional;

/**
 * Created by egergle on 26/07/2016.
 */
public class TestSuiteRepositoryImpl extends BaseRepositoryImpl<TestSuite, Long> implements TestSuiteRepository {

    @Override
    public Optional<TestSuite> findByNameAndFeatureId(String name, Long featureId) {
        Search search = new Search(TestSuite.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("feature.id", featureId);

        Optional<TestSuite> optionalTestType = Optional.ofNullable(searchUnique(search));
        return optionalTestType;
    }
}
