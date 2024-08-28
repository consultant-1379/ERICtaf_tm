package com.ericsson.cifwk.tm.domain.model.domain.impl;

import com.ericsson.cifwk.tm.domain.annotations.Repository;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.googlecode.genericdao.search.Search;

import java.util.List;

@Repository
public class DropRepositoryImpl extends BaseRepositoryImpl<Drop, Long> implements DropRepository {

    @Override
    public List<Drop> findByProduct(Long productId) {
        Search search = new Search(Drop.class);
        search.addFilterEqual("product.id", productId);
        return this.search(search);
    }

    @Override
    public Drop findByProductIDAndDropName(Long productId, String name) {
        Search search = new Search(Drop.class);
        search.addFilterEqual("product.id", productId);
        search.addFilterEqual("name", name);
        return this.searchUnique(search);
    }

}
