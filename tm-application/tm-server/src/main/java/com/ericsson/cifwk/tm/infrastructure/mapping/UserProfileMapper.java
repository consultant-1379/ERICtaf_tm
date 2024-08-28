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

import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ericsson.cifwk.tm.infrastructure.mapping.Mapping.newInstance;

public class UserProfileMapper implements EntityMapper<UserProfile, UserProfileInfo> {


    private final ProductMapper productMapper;
    private final ProjectMapper projectMapper;
    private final SavedSearchMapper savedSearchMapper;

    @Inject
    public UserProfileMapper(ProjectMapper projectMapper, ProductMapper productMapper, SavedSearchMapper savedSearchMapper) {
        this.projectMapper = projectMapper;
        this.productMapper = productMapper;
        this.savedSearchMapper = savedSearchMapper;
    }

    @Override
    public UserProfileInfo mapEntity(UserProfile entity, Class<? extends UserProfileInfo> dtoClass) {
        return mapEntity(entity, newInstance(dtoClass));
    }

    @Override
    public UserProfileInfo mapEntity(UserProfile entity, UserProfileInfo dto) {
        if (entity == null) {
            return null;
        }
        Preconditions.checkNotNull(dto);
        dto.setUserId(entity.getUser().getExternalId());
        dto.setUserName(entity.getUser().getUserName());
        dto.setProduct(productMapper.mapEntity(entity.getProduct(), ProductInfo.class));
        dto.setProject(projectMapper.mapEntity(entity.getProject(), ProjectInfo.class));
        dto.setAdministrator(entity.isAdministrator());

        Set<SavedSearchInfo> testCaseSearches = entity.getSavedSearch().stream()
                .map(item -> savedSearchMapper.mapEntity(item, SavedSearchInfo.class))
                .collect(Collectors.toSet());

        dto.setSavedSearch(testCaseSearches);
        return dto;
    }

    @Override
    public UserProfileInfo mapEntity(
            UserProfile entity,
            Class<? extends UserProfileInfo> dtoClass,
            Class<? extends DtoView<UserProfileInfo>> view) {
        return mapEntity(entity, dtoClass);
    }

    @Override
    public UserProfileInfo mapEntity(
            UserProfile entity,
            UserProfileInfo dto,
            Class<? extends DtoView<UserProfileInfo>> view) {
        return mapEntity(entity, dto);
    }

}
