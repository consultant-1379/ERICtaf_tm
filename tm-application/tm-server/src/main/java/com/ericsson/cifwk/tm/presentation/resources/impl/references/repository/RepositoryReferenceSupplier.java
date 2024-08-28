/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references.repository;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReferenceMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.ericsson.cifwk.tm.presentation.resources.impl.references.ReferenceSupplier;
import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.filter.ParamFilter;
import com.googlecode.genericdao.dao.jpa.GenericDAO;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Set;

public class RepositoryReferenceSupplier<N extends Number, T extends NamedWithId<N>, R extends GenericDAO<T, N>>
        implements ReferenceSupplier {

    private final R repository;
    private final Set<? extends ParamFilter> paramParsers;
    private final SearchConfigurator searchConfigurator;

    public RepositoryReferenceSupplier(
            R repository) {
        this(repository, null, null);
    }

    public RepositoryReferenceSupplier(
            R repository,
            Set<? extends ParamFilter> paramParsers) {
        this(repository, paramParsers, null);
    }

    public RepositoryReferenceSupplier(
            R repository,
            Set<? extends ParamFilter> paramParsers,
            SearchConfigurator searchConfigurator) {
        this.repository = repository;
        this.paramParsers = paramParsers;
        this.searchConfigurator = searchConfigurator;
    }

    @Override
    public List<ReferenceDataItem> get(MultivaluedMap<String, String> params) {
        Search search = new Search();
        if (paramParsers != null) {
            for (ParamFilter<?> paramFilter : paramParsers) {
                List<String> values = params.get(paramFilter.name());
                Filter filter = getFilter(paramFilter, values);
                if (filter != null) {
                    search.addFilter(filter);
                }
            }
        }
        if (searchConfigurator != null) {
            searchConfigurator.configure(search);
        }
        List<NamedWithId> searchResult = repository.search(search);
        return Mapping.mapEntities(searchResult, new ReferenceMapper(), ReferenceDataItem.class);
    }

    private <P> Filter getFilter(ParamFilter<P> paramFilter, List<String> values) {
        if (values == null) {
            return null;
        }
        List<P> parsedValues = ParamParsers.parseAll(paramFilter, values);
        if (!parsedValues.isEmpty()) {
            return paramFilter.filter(parsedValues);
        }
        return null;
    }

}
