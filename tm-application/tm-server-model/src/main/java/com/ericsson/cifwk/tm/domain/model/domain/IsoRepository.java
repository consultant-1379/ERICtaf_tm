package com.ericsson.cifwk.tm.domain.model.domain;

import com.ericsson.cifwk.tm.domain.model.domain.impl.IsoRepositoryImpl;
import com.ericsson.cifwk.tm.domain.model.shared.BaseRepository;
import com.ericsson.cifwk.tm.domain.model.shared.impl.BaseRepositoryImpl;
import com.google.common.cache.LoadingCache;
import com.google.inject.ImplementedBy;

/**
 * Created by ejhnhng on 16/03/2016.
 */
@ImplementedBy(IsoRepositoryImpl.class)
public interface IsoRepository extends BaseRepository<ISO, Long> {

}
