package com.ericsson.cifwk.tm.infrastructure.mapping;


import com.ericsson.cifwk.tm.domain.model.posts.Post;
import com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.integration.ldap.LDAPSearchType;
import com.ericsson.cifwk.tm.integration.ldap.UserDirectory;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.UserInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.ericsson.cifwk.tm.domain.model.posts.PostObjectNameReference.mapObjectReferenceByValue;
import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class PostMapper implements
        EntityMapper<Post, PostInfo>, DtoMapper<PostInfo, Post> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostMapper.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserDirectory userDirectory;

    @Override
    public Post mapDto(PostInfo dto, Post entity) {
        if (dto == null) {
            return null;
        }

        entity.setId(dto.getId());
        entity.setDeletedAt(dto.getDeletedAt());
        entity.setDeleted(dto.isDeleted());
        entity.setMessage(dto.getMessage());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setObjectId(dto.getObjectId());

        if (dto.getObjectNameReference() != null) {
            entity.setObjectNameReference(Long.parseLong(dto.getObjectNameReference().getId()));
        } else {
            entity.setObjectNameReference(null);
        }

        entity.setObjectId(dto.getObjectId());

        if (dto.getUser() == null) {
            entity.setUser(null);
        } else {
            entity.setUser(findOrCreateUser(dto.getUser().getUserId()));
        }
        return entity;
    }

    @Override
    public PostInfo mapEntity(Post entity, PostInfo dto,
                              Class<? extends DtoView<PostInfo>> view) {
        if (entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDeleted(entity.isDeleted());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setMessage(entity.getMessage());
        dto.setObjectId(entity.getObjectId());

        if (entity.getObjectNameReference() != null) {
            PostObjectNameReference objectName = mapObjectReferenceByValue(entity.getObjectNameReference());
            dto.setObjectNameReference(
                    new ReferenceDataItem(Long.toString(objectName.getValue()), objectName.getKey()));
        } else {
            dto.setObjectNameReference(null);
        }


        UserMapper userMapper = new UserMapper();
        dto.setUser(userMapper.mapEntity(entity.getUser(), new UserInfo()));

        return dto;
    }

    @Override
    public Post mapDto(PostInfo dto, Class<? extends Post> entityClass) {
        return mapDto(dto, newInstance(entityClass));
    }

    @Override
    public PostInfo mapEntity(Post entity, Class<? extends PostInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public PostInfo mapEntity(Post entity, PostInfo dto) {
        return mapEntity(entity, dto, null);
    }

    @Override
    public PostInfo mapEntity(Post entity, Class<? extends PostInfo> dtoClass,
                              Class<? extends DtoView<PostInfo>> view) {
        return mapEntity(entity, newInstance(dtoClass), view);
    }

    private User findOrCreateUser(String username) {
        if (username == null) {
            return null;
        }
        User user = userRepository.findByExternalId(username);
        if (user == null) {
            user = new User(username);
        }

        if (!user.hasExternalAttributes()) {
            UserInfo userInfo = userDirectory.findInLDAP(user.getExternalId(), LDAPSearchType.USER);
            if (userInfo != null) {
                user.setExternalAttributes(userInfo.getEmail(), userInfo.getName(), userInfo.getSurname());
            } else {
                LOGGER.error("Unable to find user {} in LDAP", user.getExternalId());
                return null;
            }
        }
        return user;
    }

}
