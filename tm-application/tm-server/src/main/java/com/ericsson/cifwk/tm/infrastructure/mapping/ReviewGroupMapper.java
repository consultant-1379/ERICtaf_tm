package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class ReviewGroupMapper implements EntityMapper<ReviewGroup, ReviewGroupInfo>, DtoMapper<ReviewGroupInfo, ReviewGroup> {

    @Inject
    private UserMapper userMapper;

    @Override
    public ReviewGroup mapDto(ReviewGroupInfo dto, Class<? extends ReviewGroup> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public ReviewGroup mapDto(ReviewGroupInfo dto, ReviewGroup entity) {
        if (dto == null) {
            return null;
        }
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        Set<User> users = dto.getUsers().stream().map(item -> userMapper.mapDto(item, User.class)).collect(Collectors.toSet());
        entity.setUsers(users);

        return entity;
    }

    @Override
    public ReviewGroupInfo mapEntity(ReviewGroup entity, Class<? extends ReviewGroupInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public ReviewGroupInfo mapEntity(ReviewGroup entity, ReviewGroupInfo dto) {
        if (entity == null) {
            return null;
        }
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        Set<UserInfo> users = entity.getUsers().stream().map(item -> userMapper.mapEntity(item, UserInfo.class)).collect(Collectors.toSet());
        dto.setUsers(users);
        return dto;
    }

    @Override
    public ReviewGroupInfo mapEntity(ReviewGroup entity, Class<? extends ReviewGroupInfo> dtoClass, Class<? extends DtoView<ReviewGroupInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public ReviewGroupInfo mapEntity(ReviewGroup entity, ReviewGroupInfo dto, Class<? extends DtoView<ReviewGroupInfo>> view) {
        return mapEntity(entity, dto);
    }
}
