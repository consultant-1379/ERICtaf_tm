package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.queries.TestCampaignItemQuerySet;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.dto.TestCampaignItemInfo;
import com.ericsson.cifwk.tm.presentation.dto.view.TestCampaignItemViewFactory;
import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;
import com.ericsson.cifwk.tm.presentation.resources.AssignmentResource;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Controller
public class AssignmentController implements AssignmentResource {

    @Inject
    private TestCampaignItemQuerySet testCampaignItemQuerySet;

    @Context
    private UriInfo uriInfo;

    @Inject
    private TestCampaignItemViewFactory testCampaignItemViewFactory;

    @Override
    public Response getAssignments(
            String userId,
            String q,
            int page, int perPage,
            String orderBy, String orderMode,
            String view) {
        Class<? extends DtoView<TestCampaignItemInfo>> dtoView = testCampaignItemViewFactory.getByName(view);
        Query query = Query.fromQueryString(q);
        query.sortBy(orderBy, orderMode);
        return testCampaignItemQuerySet.getAssignmentByQuery(userId, query, page, perPage, uriInfo, dtoView);
    }

}
