/*
 * COPYRIGHT Ericsson (c) 2015.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.filter;

import com.ericsson.cifwk.tm.presentation.resources.impl.references.repository.FilterSupplier;

import javax.ws.rs.BadRequestException;

public class LongParamFilter extends AbstractParamFilter<Long> implements ParamFilter<Long> {

    public LongParamFilter(String name, FilterSupplier<Long> filterSupplier) {
        super(name, filterSupplier);
    }

    @Override
    public Long parse(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new BadRequestException("'" + name + "' should be a number");
        }
    }
}
