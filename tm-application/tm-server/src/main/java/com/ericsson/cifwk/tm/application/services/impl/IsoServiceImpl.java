package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.IsoService;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.domain.ISO;
import com.ericsson.cifwk.tm.domain.model.domain.IsoRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.IsoMapper;
import com.ericsson.cifwk.tm.integration.IsoRetrievalService;
import com.ericsson.cifwk.tm.presentation.dto.IsoInfo;
import com.ericsson.cifwk.tm.utils.DiffHelper;
import com.ericsson.cifwk.tm.utils.VersionComparator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.persist.Transactional;


import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class IsoServiceImpl implements IsoService {

    @Inject
    private IsoRetrievalService isoRetrievalService;

    @Inject
    private DropRepository dropRepository;

    @Inject
    private IsoRepository isoRepository;

    @Inject
    private IsoMapper isoMapper;

    @Override
    public List<IsoInfo> getIsosByDrop(Long dropId) {
        Drop drop = dropRepository.find(dropId);
        if (drop == null) {
            throw new IllegalArgumentException("Drop with ID" + dropId + " does not exist");
        }
        List<ISO> savedIsos = isoRepository.findAll();

        List<IsoInfo> savedIsoDtos = entitiesToDto(savedIsos);
        List<IsoInfo> remoteDtos = isoRetrievalService.getIsos(drop.getProduct().getName(), drop.getName());

        DiffHelper<IsoInfo> diffHelper = new DiffHelper<>(Sets.newHashSet(savedIsoDtos), Sets.newHashSet(remoteDtos));
        Set<IsoInfo> newIsoDtos = diffHelper.getOnlyInB();

        if (!newIsoDtos.isEmpty()) {
            List<ISO> newIsoEntities = dtosToEntity(newIsoDtos);
            drop.addIsos(newIsoEntities);
            save(drop);
        }
        return getSortedDtoList(drop.getIsos());
    }

    @Override
    @Transactional
    public Drop save(Drop drop) {
        return dropRepository.save(drop);
    }

    private List<IsoInfo> entitiesToDto(List<ISO> entities) {
        return entities.stream()
                .map(e -> isoMapper.mapEntity(e, IsoInfo.class))
                .collect(Collectors.toList());
    }

    private List<ISO> dtosToEntity(Set<IsoInfo> dtos) {
        return dtos.stream()
                .map(d -> isoMapper.mapDto(d, ISO.class))
                .collect(Collectors.toList());
    }

    private List<IsoInfo> getSortedDtoList(Set<ISO> isos) {
        List entityList = Lists.newArrayList(isos);
        entityList.sort(new VersionComparator());

        return entitiesToDto(entityList);
    }

}
