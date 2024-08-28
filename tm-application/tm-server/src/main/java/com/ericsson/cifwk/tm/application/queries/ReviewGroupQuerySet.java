/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroup;
import com.ericsson.cifwk.tm.domain.model.testdesign.ReviewGroupRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReviewGroupMapper;
import com.ericsson.cifwk.tm.presentation.dto.CompletionInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReviewGroupInfo;
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

@QuerySet
public class ReviewGroupQuerySet {

    @Inject
    private ReviewGroupRepository reviewGroupRepository;

    @Inject
    private ReviewGroupMapper reviewGroupMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> reviewFields = searchMapping.getReviewGroupFields();
        Paginated<ReviewGroup> paginated = customSearch(query, page, perPage, reviewFields);
        QueryInfo queryInfo = query.convertToQueryInfo(reviewFields);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<ReviewGroup, ReviewGroupInfo>() {
                    @Override
                    public ReviewGroupInfo apply(ReviewGroup reviewGroup) {
                        return reviewGroupMapper.mapEntity(reviewGroup, ReviewGroupInfo.class);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

    public Response getCompletion(String search, int limit) {
        if (Strings.isNullOrEmpty(search)) {
            return Responses.ok(CompletionInfo.empty());
        }
        List<ReviewGroup> reviewGroups = reviewGroupRepository.findMatchingGroup(search, limit);

        return Responses.ok(reviewGroups);
    }

    private Paginated<ReviewGroup> customSearch(Query query, int page, int perPage, Map<String, QueryField> reviewFields) {
        Search search = query.convertToSearch(ReviewGroup.class, reviewFields);
        search.setDistinct(true);
        return reviewGroupRepository.searchPaginated(search, page, perPage);
    }

}
