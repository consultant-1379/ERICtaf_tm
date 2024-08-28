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

import com.ericsson.cifwk.tm.presentation.dto.ReferenceDataItem;

import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

public interface ReferenceSupplier {

    List<ReferenceDataItem> get(MultivaluedMap<String, String> params);

}
