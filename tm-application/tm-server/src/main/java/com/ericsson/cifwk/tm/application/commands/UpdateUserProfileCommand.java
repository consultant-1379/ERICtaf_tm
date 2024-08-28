package com.ericsson.cifwk.tm.application.commands;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.requests.UpdateUserProfileRequest;
import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.application.services.UserProfileService;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.requirements.Project;
import com.ericsson.cifwk.tm.domain.model.requirements.ProjectRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearch;
import com.ericsson.cifwk.tm.domain.model.testdesign.SavedSearchRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserProfileMapper;
import com.ericsson.cifwk.tm.presentation.dto.ProductInfo;
import com.ericsson.cifwk.tm.presentation.dto.ProjectInfo;
import com.ericsson.cifwk.tm.presentation.dto.SavedSearchInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.responses.Responses;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ebuzdmi
 */
public class UpdateUserProfileCommand implements Command<UpdateUserProfileRequest> {

    @Inject
    private UserProfileService userProfileService;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private UserManagementService userManagementService;

    @Inject
    private SavedSearchRepository savedSearchRepository;

    @Inject
    private UserProfileMapper userProfileMapper;

    @Override
    public Response apply(UpdateUserProfileRequest input) {
        String userId = input.getUserId();
        UserProfileInfo userProfile = input.getUserProfile();

        UserProfile existingProfile = userManagementService.fetchOrCreateUserProfile(userId);
        ProductInfo productInfo = userProfile.getProduct();

        Product product = null;
        if (productInfo != null) {
            product = productRepository.findByExternalId(productInfo.getExternalId());
        }

        ProjectInfo projectInfo = userProfile.getProject();

        Project project = null;
        if (projectInfo != null) {
            project = projectRepository.findByExternalId(projectInfo.getExternalId());
        }

        Set<SavedSearchInfo> savedSearchInfos = userProfile.getSavedSearch().stream()
                .filter(item -> item.getId() == null).collect(Collectors.toSet());

        userProfileService.updateProfile(existingProfile, product, project);

        for (SavedSearchInfo savedSearchInfo : savedSearchInfos) {
            validateSavedSearch(savedSearchInfo, existingProfile);
            userProfileService.updateSavedSearch(existingProfile, savedSearchInfo);
        }

        UserProfileInfo userProfileInfo = userProfileMapper.mapEntity(existingProfile, UserProfileInfo.class);

        return Responses.ok(userProfileInfo);
    }

    private void validateSavedSearch(SavedSearchInfo savedSearchInfo, UserProfile userProfile) {
        Optional<SavedSearch> savedSearch = Optional.ofNullable(
                savedSearchRepository.getNameAndProfileId(userProfile.getId(),
                        savedSearchInfo.getName()));
        if (savedSearch.isPresent()) {
            throw new BadRequestException(Responses.badRequest("The search already exists for this user profile"));
        }

    }

}
