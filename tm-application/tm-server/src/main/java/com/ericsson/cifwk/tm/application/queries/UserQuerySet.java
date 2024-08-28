package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.security.UserManagementService;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.users.User;
import com.ericsson.cifwk.tm.domain.model.users.UserProfile;
import com.ericsson.cifwk.tm.domain.model.users.UserProfileRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.UserProfileMapper;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.UserProfileInfo;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ebuzdmi
 */
@QuerySet
public final class UserQuerySet {

    @Inject
    private UserManagementService userManagementService;

    @Inject
    private UserProfileMapper userProfileMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private SearchMapping searchMapping;

    @Inject
    private UserProfileRepository userProfileRepository;

    public Response loadUserProfile(String userId) {
        UserProfile userProfile = userManagementService.fetchOrCreateUserProfile(userId);
        UserProfileInfo userProfileInfo = userProfileMapper.mapEntity(userProfile, UserProfileInfo.class);
        return Responses.nullable(userProfileInfo);
    }

    public Response getCompletion(String search, int limit) {
        if (Strings.isNullOrEmpty(search)) {
            return Responses.ok(CompletionInfo.empty());
        }
        List<User> users = userRepository.findMatchingExternalIdEmailAndUserName(search, limit);

        return Responses.ok(users);
    }

    public Response getUserProfileByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> userProfileFields = searchMapping.getUserProfileFields();
        QueryInfo queryInfo = query.convertToQueryInfo(userProfileFields);
        Search search = query.convertToSearch(UserProfile.class, userProfileFields);
        Paginated<UserProfile> paginated = userProfileRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<UserProfile, UserProfileInfo>() {
                    @Override
                    public UserProfileInfo apply(UserProfile userProfile) {
                        return userProfileMapper.mapEntity(userProfile, UserProfileInfo.class);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }
}

