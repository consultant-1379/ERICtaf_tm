/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */
package com.ericsson.cifwk.tm.infrastructure.event;

import com.ericsson.cifwk.tm.common.TrsRecord;
import com.ericsson.cifwk.tm.domain.events.TrsUpdateEvent;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecution;
import com.ericsson.cifwk.tm.domain.model.execution.TestExecutionRepository;
import com.ericsson.cifwk.tm.domain.model.management.TestCampaign;
import com.ericsson.cifwk.tm.domain.model.requirements.Product;
import com.ericsson.cifwk.tm.trs.service.TrsService;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.netflix.governator.annotations.WarmUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class TrsEventService {

    private final Logger logger = LoggerFactory.getLogger(TrsEventService.class);

    @Inject
    private Provider<TrsService> trsService;

    @Inject
    private Provider<TestExecutionRepository> testExecutionRepository;

    @Inject
    private EventBus eventBus;

    @WarmUp
    void configure() {
        eventBus.register(this);
    }

    @Subscribe
    public void onEvent(TrsUpdateEvent trsUpdateEvent) {
        recordExecutions(trsUpdateEvent.getTestCampaign(), trsUpdateEvent.getTestExecutions());
    }

    @Transactional
    private void recordExecutions(TestCampaign testCampaign, List<TestExecution> testExecutions) {
        Product product = testCampaign.getFeatures().stream().findFirst().get().getProduct();
        if (product.isTrsRecordable()) {
            try {
                TestExecution[] executionArray = new TestExecution[testExecutions.size()];
                executionArray = testExecutions.toArray(executionArray);
                Optional<TrsRecord> record = trsService.get().recordExecutions(testCampaign, executionArray);
                if (record.isPresent()) {
                    Arrays.stream(executionArray).forEach(e -> e.setRecordedInTrs(true));
                    testExecutionRepository.get().save(executionArray);
                }
            } catch (Exception e) {
                logger.error("Could not record Test Execution in TRS", e);
            }
        }
    }
}
