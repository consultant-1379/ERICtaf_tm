package com.ericsson.cifwk.tm.application.services.impl;

import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearch;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.domain.model.users.UserProfileRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.SavedSearchMapper;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.google.inject.persist.Transactional;
import com.googlecode.genericdao.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 *
 */
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private SavedSearchMapper savedSearchMapper;

    @Override
    public String findProductNameByUserId(Long userId) {
        UserProfile profile = findUserProfileByUserId(userId);
        if (profile != null && profile.getProduct() != null) {
            return profile.getProduct().getName();
        }
        return null;
    }

    @Override
    @Transactional
    public UserProfile findOrCreate(User user) {
        if (user == null) {
            return null;
        }

        UserProfile profile = findUserProfileByUserId(user.getId());
        if (profile == null) {
            logger.info("Creating user profile for {}", user.getExternalId());
            User mergedUser = userRepository.merge(user);
            return createProfile(mergedUser);
        } else {
            return profile;
        }
    }

    private UserProfile createProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    @Override
    @Transactional
    public void updateProfile(UserProfile userProfile, Product product, Project project) {
        userProfile.setProduct(product);
        userProfile.setProject(project);
    }

    @Override
    @Transactional
    public void updateSavedSearch(UserProfile userProfile, SavedSearchInfo savedSearchInfo) {
        SavedSearch savedSearch = savedSearchMapper.mapDto(savedSearchInfo, SavedSearch.class);
        savedSearch.setUserProfile(userProfile);
        if (savedSearchInfo.getId() == null) {
            userProfile.getSavedSearch().add(savedSearch);
        }
        userProfileRepository.persist(userProfile);
    }

    @Override
    public UserProfile findUserProfileByUserId(Long userId) {
        Search search = new Search(UserProfile.class);
        search.addFilterEqual("user.id", userId);
        return userProfileRepository.searchUnique(search);
    }

}
