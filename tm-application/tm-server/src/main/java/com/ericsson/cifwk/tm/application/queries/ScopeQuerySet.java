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
import com.ericsson.cifwk.tm.domain.model.testdesign.Scope;
import com.ericsson.cifwk.tm.domain.model.testdesign.ScopeRepository;
import com.ericsson.cifwk.tm.infrastructure.mapping.ScopeMapper;
import com.ericsson.cifwk.tm.presentation.dto.GroupInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.responses.PaginationHelper;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.base.Function;
import com.googlecode.genericdao.search.Search;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.Map;

@QuerySet
public class ScopeQuerySet {

    @Inject
    private ScopeRepository scopeRepository;

    @Inject
    private ScopeMapper scopeMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getScope(Long scopeId) {
        Scope scope = scopeRepository.find(scopeId);
        ReferenceDataItem dto = scopeMapper.mapEntity(scope, new ReferenceDataItem());
        return Responses.nullable(dto);
    }

    public Response getGroupsByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> featureFields = searchMapping.getProductFields();
        QueryInfo queryInfo = query.convertToQueryInfo(featureFields);
        Search search = query.convertToSearch(Scope.class, featureFields);
        Paginated<Scope> paginated = scopeRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<Scope, GroupInfo>() {
                    @Override
                    public GroupInfo apply(Scope scope) {
                        return scopeMapper.mapEntity(new GroupInfo(), scope);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

}
