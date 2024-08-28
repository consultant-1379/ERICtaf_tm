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

import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class UserMapper implements EntityMapper<User, UserInfo>, DtoMapper<UserInfo, User>  {

    @Override
    public UserInfo mapEntity(User entity, Class<? extends UserInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public UserInfo mapEntity(User entity, UserInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setId(entity.getId());
        dto.setUserId(entity.getExternalId());
        dto.setEmail(entity.getExternalEmail());
        dto.setName(entity.getExternalName());
        dto.setSurname(entity.getExternalSurname());
        dto.setUserName(entity.getUserName());
        return dto;
    }

    @Override
    public UserInfo mapEntity(
            User entity,
            Class<? extends UserInfo> dtoClass,
            Class<? extends DtoView<UserInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public UserInfo mapEntity(
            User entity,
            UserInfo dto,
            Class<? extends DtoView<UserInfo>> view) {
        return mapEntity(entity, dto);
    }

    @Override
    public User mapDto(UserInfo dto, Class<? extends User> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public User mapDto(UserInfo dto, User entity) {
        if (dto == null) {
            return null;
        }

        entity.setId(dto.getId());
        entity.setExternalId(dto.getUserId());
        entity.setExternalEmail(dto.getEmail());
        entity.setExternalName(dto.getName());
        entity.setExternalSurname(dto.getSurname());
        entity.setUserName(dto.getUserName());

        return entity;
    }
}
