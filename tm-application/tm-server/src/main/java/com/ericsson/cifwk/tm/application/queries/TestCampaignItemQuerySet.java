package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.security.UserSessionService;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItem;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaignItemRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.TestCampaignItemMapper;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.Map;

@QuerySet
public class TestCampaignItemQuerySet {

    @Inject
    private TestCampaignItemRepository testCampaignItemRepository;

    @Inject
    private TestCampaignItemMapper testCampaignItemMapper;

    @Inject
    private UserSessionService userSessionService;

    @Inject
    private SearchMapping searchMapping;

    public Response getAssignmentByQuery(
            String userId,
            Query query,
            int page, int perPage,
            UriInfo uriInfo,
            final Class<? extends DtoView<TestCampaignItemInfo>> dtoView) {
        Map<String, QueryField> testPlanItemFields = searchMapping.getTestPlanItemFields();

        Search search = query.convertToSearch(TestCampaignItem.class, testPlanItemFields);
        search.addFilterEqual("user.externalId", getUserId(userId));

        Paginated<TestCampaignItem> paginated = testCampaignItemRepository.searchPaginated(search, page, perPage);

        QueryInfo queryInfo = query.convertToQueryInfo(testPlanItemFields);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TestCampaignItem, TestCampaignItemInfo>() {
                    @Override
                    public TestCampaignItemInfo apply(TestCampaignItem testCampaignItem) {
                        return testCampaignItemMapper.mapEntity(testCampaignItem, TestCampaignItemInfo.class, dtoView);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    private String getUserId(String userId) {
        if (userId != null) {
            return userId;
        } else {
            return userSessionService.getCurrentUser().getUserId();
        }
    }
}
