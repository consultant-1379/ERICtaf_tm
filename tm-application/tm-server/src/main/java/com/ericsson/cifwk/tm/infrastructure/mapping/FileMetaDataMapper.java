/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.FileData;
import com.ericsson.cifwk.tm.files.FileCategory;
import com.ericsson.cifwk.tm.presentation.dto.FileMetaDataInfo;
import com.ericsson.cifwk.tm.presentation.dto.FileDataInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import java.util.Date;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class FileMetaDataMapper implements EntityMapper<FileData, FileMetaDataInfo>,
        DtoMapper<FileDataInfo, FileData> {


    @Override
    public FileMetaDataInfo mapEntity(FileData entity, Class<? extends FileMetaDataInfo> dtoClass) {
        if (entity == null) {
            return null;
        }
        FileMetaDataInfo dto = newInstance(dtoClass);
        return mapEntity(entity, dto);
    }

    @Override
    public FileMetaDataInfo mapEntity(FileData entity, FileMetaDataInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setFileName(entity.getFilename());
        dto.setAuthor(entity.getAuthor());
        dto.setCreated(entity.getCreated());
        dto.setUrl(entity.getLocation());

        return dto;
    }

    @Override
    public FileMetaDataInfo mapEntity(FileData entity, Class<? extends FileMetaDataInfo> dtoClass,
                                      Class<? extends DtoView<FileMetaDataInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public FileMetaDataInfo mapEntity(FileData entity, FileMetaDataInfo dto,
                                      Class<? extends DtoView<FileMetaDataInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public FileData mapDto(FileDataInfo dto, Class<? extends FileData> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public FileData mapDto(FileDataInfo dto, FileData entity) {
        if (dto == null) {
            return null;
        }

        Preconditions.checkNotNull(entity);
        entity.setId(dto.getId());
        entity.setEntityId(dto.getEntityId());
        entity.setAuthor(dto.getAuthor());
        entity.setCreated(new Date());
        entity.setFilename(dto.getFileName());
        entity.setFileCategory(FileCategory.get(dto.getFileCategory()));
        entity.setLocation(dto.getUrl());
        return entity;

    }

}

