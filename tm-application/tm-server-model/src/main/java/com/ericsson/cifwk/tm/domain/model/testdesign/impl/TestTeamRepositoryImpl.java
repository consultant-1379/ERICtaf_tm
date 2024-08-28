package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestSuite;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeam;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTeamRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.googlecode.genericdao.search.Search;

import java.util.Optional;

/**
 * Created by egergle on 26/07/2016.
 */
public class TestTeamRepositoryImpl extends BaseRepositoryImpl<TestTeam, Long> implements TestTeamRepository {

    @Override
    public Optional<TestTeam> findByNameAndFeatureId(String name, Long featureId) {
        Search search = new Search(TestTeam.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("feature.id", featureId);

        Optional<TestTeam> optionalTestType = Optional.ofNullable(searchUnique(search));
        return optionalTestType;
    }
}
