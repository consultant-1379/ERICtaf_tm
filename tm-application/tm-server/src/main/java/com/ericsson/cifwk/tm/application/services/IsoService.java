package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.IsoServiceImpl;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(IsoServiceImpl.class)
public interface IsoService {

    List<IsoInfo> getIsosByDrop(Long dropId);

    Drop save(Drop drop);
}
