/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.application.jobs;

import com.ericsson.cifwk.tm.application.services.NotificationCleanUpService;
import com.google.inject.Inject;
import com.google.inject.persist.UnitOfWork;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class NotificationUpdateJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(NotificationUpdateJob.class);

    private final int weeks = 10;

    private final NotificationCleanUpService notificationService;
    private final UnitOfWork unitOfWork;

    @Inject
    public NotificationUpdateJob(NotificationCleanUpService notificationService, UnitOfWork unitOfWork) {
        this.notificationService = notificationService;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("job started");
        try {
            unitOfWork.begin();
            notificationService.cleanUp(weeks);
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage(), e);
        } finally {
            unitOfWork.end();
            logger.info("job finished");
        }
    }

}
