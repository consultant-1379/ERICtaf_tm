/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application;

import com.ericsson.cifwk.tm.application.impl.CommandProcessorImpl;
import com.google.inject.ImplementedBy;

import javax.ws.rs.core.Response;

@ImplementedBy(CommandProcessorImpl.class)
public interface CommandProcessor {

    <T> Response process(Command<T> command, T input);

}
