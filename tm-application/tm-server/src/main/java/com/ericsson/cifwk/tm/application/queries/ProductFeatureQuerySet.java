package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.application.services.impl.SearchMapping;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeature;
import com.ericsson.cifwk.tm.domain.model.domain.ProductFeatureRepository;
import com.ericsson.cifwk.tm.domain.model.shared.Paginated;
import com.ericsson.cifwk.tm.domain.model.shared.search.Query;
import com.ericsson.cifwk.tm.domain.model.shared.search.field.QueryField;
import com.ericsson.cifwk.tm.infrastructure.mapping.FeatureMapper;
import com.ericsson.cifwk.tm.presentation.dto.FeatureInfo;
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

/**
 * @author egergle
 */
@QuerySet
public final class ProductFeatureQuerySet {

    @Inject
    private ProductFeatureRepository featureRepository;

    @Inject
    private FeatureMapper featureMapper;

    @Inject
    private SearchMapping searchMapping;

    public Response getFeature(Long featureId) {
        ProductFeature feature = featureRepository.find(featureId);
        FeatureInfo featureInfo = featureMapper.mapEntity(feature, FeatureInfo.class);
        return Responses.nullable(featureInfo);
    }

    public Response getFeaturesByQuery(Query query,
                                       int page,
                                       int perPage,
                                       UriInfo uriInfo) {

        Map<String, QueryField> featureFields = searchMapping.getProductFields();
        QueryInfo queryInfo = query.convertToQueryInfo(featureFields);
        Search search = query.convertToSearch(ProductFeature.class, featureFields);
        Paginated<ProductFeature> paginated = featureRepository.searchPaginated(search, page, perPage);
        return PaginationHelper.page(
                paginated,
                uriInfo,
                new Function<ProductFeature, FeatureInfo>() {
                    @Override
                    public FeatureInfo apply(ProductFeature productFeature) {
                        return featureMapper.mapEntity(productFeature, new FeatureInfo());
                    }
                },
                Collections.singletonMap("query", queryInfo)
        );
    }
}

