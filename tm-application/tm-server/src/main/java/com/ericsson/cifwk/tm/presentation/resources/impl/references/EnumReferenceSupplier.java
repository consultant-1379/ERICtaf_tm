/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references;

import com.ericsson.cifwk.tm.common.NamedWithId;
import com.ericsson.cifwk.tm.infrastructure.mapping.Mapping;
import com.ericsson.cifwk.tm.infrastructure.mapping.ReferenceMapper;
import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;
import com.google.common.collect.Lists;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;
import java.util.List;

public class EnumReferenceSupplier<N extends Number, T extends Enum<T> & NamedWithId<N>>
        implements ReferenceSupplier {

    private final Class<T> enumClass;

    public EnumReferenceSupplier(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public List<ReferenceDataItem> get(MultivaluedMap<String, String> params) {
        T[] enumConstants = enumClass.getEnumConstants();
        List<NamedWithId> items = Lists.newArrayList();
        Collections.addAll(items, enumConstants);
        return Mapping.mapEntities(items, new ReferenceMapper(), ReferenceDataItem.class);
    }

}
