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

import com.ericsson.cifwk.tm.domain.model.users.Notification;
import com.ericsson.cifwk.tm.domain.model.users.NotificationType;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.presentation.dto.NotificationInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class NotificationMapper
        implements EntityMapper<Notification, NotificationInfo>, DtoMapper<NotificationInfo, Notification> {

    private final EnumReferenceMapper referenceMapper;
    private final UserRepository userRepository;

    @Inject
    public NotificationMapper(
            EnumReferenceMapper referenceMapper,
            UserRepository userRepository) {
        this.referenceMapper = referenceMapper;
        this.userRepository = userRepository;
    }

    @Override
    public NotificationInfo mapEntity(Notification entity, Class<? extends NotificationInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public NotificationInfo mapEntity(Notification entity, NotificationInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);

        dto.setId(entity.getId());
        dto.setType(referenceMapper.mapEntity(entity.getType(), new ReferenceDataItem()));
        dto.setText(entity.getText());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());

        User author = entity.getAuthor();
        if (author != null) {
            dto.setAuthor(author.getExternalId());
        }
        return dto;
    }

    @Override
    public NotificationInfo mapEntity(
            Notification entity,
            Class<? extends NotificationInfo> dtoClass,
            Class<? extends DtoView<NotificationInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public NotificationInfo mapEntity(
            Notification entity,
            NotificationInfo dto,
            Class<? extends DtoView<NotificationInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public Notification mapDto(NotificationInfo dto, Class<? extends Notification> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public Notification mapDto(NotificationInfo dto, Notification entity) {
        if (dto == null) {
            return null;
        }
        Preconditions.checkNotNull(entity);

        entity.setId(dto.getId());
        entity.setType(referenceMapper.mapCastDto(dto.getType(), NotificationType.class));
        entity.setText(dto.getText());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        String author = dto.getAuthor();
        if (author != null) {
            entity.setAuthor(userRepository.findByExternalId(author));
        }

        return entity;
    }

}
