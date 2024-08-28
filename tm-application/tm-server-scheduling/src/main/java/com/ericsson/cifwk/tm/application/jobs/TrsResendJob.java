package com.ericsson.cifwk.tm.application.jobs;

import com.ericsson.cifwk.tm.application.services.TrsResendService;
import com.google.inject.persist.UnitOfWork;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@DisallowConcurrentExecution
public class TrsResendJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrsResendJob.class);

    @Inject
    private TrsResendService trsResendService;

    @Inject
    private UnitOfWork unitOfWork;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("TRS resend job started");
        try {
            unitOfWork.begin();
            trsResendService.recordUnsentExecutions();
        } catch (Exception e) {
            throw new JobExecutionException(e.getMessage(), e);
        } finally {
            unitOfWork.end();
            LOGGER.info("TRS resend job finished");
        }
    }
}
