/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.queries;

import com.ericsson.cifwk.tm.application.annotations.QuerySet;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.domain.model.requirements.ProductRepository;
import com.ericsson.cifwk.tm.domain.model.testdesign.TestCaseRepository;
import com.ericsson.cifwk.tm.domain.model.users.UserSessionRepository;
import com.ericsson.cifwk.tm.domain.statistics.StatisticsObject;
import com.ericsson.cifwk.tm.presentation.responses.Responses;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@QuerySet
public class StatisticsQuerySet {

    @Inject
    private TestCaseRepository testCaseRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private UserSessionRepository userSessionRepository;

    public Response getTestCaseCount() {
        List<Product> products = productRepository.findAll();

        List<StatisticsObject> collection = products.stream().map(item -> {
                int count = testCaseRepository.findTotalByProduct(item.getId());
                return new StatisticsObject(item.getExternalId(), count);
            }).sorted((StatisticsObject o1, StatisticsObject o2)->
                o2.getValue() - o1.getValue()
        ).collect(Collectors.toList());

        return Responses.ok(collection);
    }

    public Response getUserMetric() {
        List<Object[]> monthlyUsers = userSessionRepository.getMonthlyUsers();
        List<StatisticsObject> results = Lists.newArrayList();
        monthlyUsers.forEach(item -> results.add(
                new StatisticsObject(
                        item[1].toString(),
                        Integer.parseInt(item[2].toString()))
        ));
        return Responses.ok(results);
    }
}
