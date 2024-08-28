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
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponent;
import com.ericsson.cifwk.tm.domain.model.requirements.TechnicalComponentRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.infrastructure.mapping.TechnicalComponentMapper;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.dto.TechnicalComponentInfo;
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
public class TechnicalComponentQuerySet {

    @Inject
    private TechnicalComponentRepository technicalComponentRepository;

    @Inject
    private TechnicalComponentMapper technicalComponentMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getTechnicalComponent(Long scopeId) {
        TechnicalComponent technicalComponent = technicalComponentRepository.find(scopeId);
        ReferenceDataItem dto = technicalComponentMapper.mapEntity(technicalComponent, new ReferenceDataItem());
        return Responses.nullable(dto);
    }

    public Response getComponentsByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> featureFields = searchMapping.getFeatureFields();
        QueryInfo queryInfo = query.convertToQueryInfo(featureFields);
        Search search = query.convertToSearch(TechnicalComponent.class, featureFields);
        Paginated<TechnicalComponent> paginated = technicalComponentRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<TechnicalComponent, TechnicalComponentInfo>() {
                    @Override
                    public TechnicalComponentInfo apply(TechnicalComponent technicalComponent) {
                        return technicalComponentMapper.mapEntity(new TechnicalComponentInfo(), technicalComponent);
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }

}
