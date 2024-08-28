package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.domain.Drop;
import com.ericsson.cifwk.tm.domain.model.domain.DropRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.infrastructure.mapping.DropMapper;
import com.ericsson.cifwk.tm.presentation.dto.DropInfo;
import com.ericsson.cifwk.tm.presentation.dto.QueryInfo;
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
public class DropQuerySet {

    @Inject
    private DropRepository dropRepository;

    @Inject
    private DropMapper dropMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getDrop(Long dropId) {
        Drop drop = dropRepository.find(dropId);
        DropInfo dropDto = dropMapper.mapEntity(drop, DropInfo.class);
        return Responses.nullable(dropDto);
    }

    public Response getDropsByQuery(
            Query query,
            int page,
            int perPage,
            UriInfo uriInfo) {

        Map<String, QueryField> featureFields = searchMapping.getDropFields();
        QueryInfo queryInfo = query.convertToQueryInfo(featureFields);
        Search search = query.convertToSearch(Drop.class, featureFields);
        Paginated<Drop> paginated = dropRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<Drop, DropInfo>() {
                    @Override
                    public DropInfo apply(Drop drop) {
                        return dropMapper.mapEntity(drop, new DropInfo());
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }
}
