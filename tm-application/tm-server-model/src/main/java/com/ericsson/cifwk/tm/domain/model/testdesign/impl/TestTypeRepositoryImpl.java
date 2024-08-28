package com.ericsson.cifwk.tm.domain.model.testdesign.impl;

import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestType;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestTypeRepository;
import com.googlecode.genericdao.search.Search;

import java.util.Optional;

/**
 * Created by egergle on 30/06/2016.
 */
public class TestTypeRepositoryImpl extends BaseRepositoryImpl<TestType, Long> implements TestTypeRepository {

    @Override
    public Optional<TestType> findByNameAndProductId(String name, Long product_id) {
        Search search = new Search(TestType.class);
        search.addFilterEqual("name", name);
        search.addFilterEqual("product.id", product_id);

        Optional<TestType> optionalTestType = Optional.ofNullable(searchUnique(search));
        return optionalTestType;

    }
}
