/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.impl;

import com.ericsson.cifwk.tm.application.Command;
import com.ericsson.cifwk.tm.application.CommandProcessor;
import com.google.inject.persist.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

// Not a @Singleton so that EntityManager is properly scoped.
public class CommandProcessorImpl implements CommandProcessor {

    @Inject
    private EntityManager em; // required for @Transactional to work

    @Override
    @Transactional
    public <T> Response process(Command<T> command, T input) {
        return command.apply(input);
    }

}
