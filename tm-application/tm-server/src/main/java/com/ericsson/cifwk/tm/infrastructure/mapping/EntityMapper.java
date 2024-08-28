/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.mapping;

import com.ericsson.cifwk.tm.presentation.dto.view.DtoView;

public interface EntityMapper<E, D> {

    D mapEntity(E entity, Class<? extends D> dtoClass);

    D mapEntity(E entity, D dto);

    D mapEntity(E entity, Class<? extends D> dtoClass, Class<? extends DtoView<D>> view);

    D mapEntity(E entity, D dto, Class<? extends DtoView<D>> view);

}
