/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.presentation.resources.impl;

import com.ericsson.cifwk.tm.application.queries.StatisticsQuerySet;
import com.ericsson.cifwk.tm.presentation.annotations.Controller;
import com.ericsson.cifwk.tm.presentation.resources.StatisticsResource;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Controller
public class StatisticsController implements StatisticsResource {

    @Inject
    private StatisticsQuerySet statisticsQuerySet;

    @Override
    public Response getUsers() {
        return statisticsQuerySet.getUserMetric();
    }

    @Override
    public Response getTestCases() {
        return statisticsQuerySet.getTestCaseCount();
    }
}
