package com.ericsson.cifwk.tm.application.services;

import com.ericsson.cifwk.tm.application.services.impl.UserProfileServiceImpl;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.google.inject.ImplementedBy;

/**
 *
 */
@ImplementedBy(UserProfileServiceImpl.class)
public interface UserProfileService {

    String findProductNameByUserId(Long userId);

    UserProfile findOrCreate(User user);

    void updateProfile(UserProfile userProfile, Product product, Project project);

    void updateSavedSearch(UserProfile userProfile, SavedSearchInfo savedSearchInfo);

    UserProfile findUserProfileByUserId(Long userId);

}
