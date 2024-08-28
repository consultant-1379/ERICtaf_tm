package com.ericsson.cifwk.tm.application.jobs;

import com.ericsson.cifwk.tm.application.services.DropUpdateService;
import com.google.inject.Inject;
import com.google.inject.persist.UnitOfWork;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisallowConcurrentExecution
public class DropUpdateJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUpdateJob.class);

    private final DropUpdateService dropUpdateService;

    private final UnitOfWork unitOfWork;

    @Inject
    public DropUpdateJob(DropUpdateService dropUpdateService, UnitOfWork unitOfWork) {
        this.dropUpdateService = dropUpdateService;
        this.unitOfWork = unitOfWork;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Drop update job started");
        try {
            unitOfWork.begin();
            dropUpdateService.updateDrops();
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage(), e);
        } finally {
            unitOfWork.end();
            LOGGER.info("Drop update job finished");
        }
    }
}
