/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.infrastructure.scheduling;

import com.ericsson.cifwk.tm.application.jobs.DefectUpdateJob;
import com.ericsson.cifwk.tm.application.jobs.DropUpdateJob;
import com.ericsson.cifwk.tm.application.jobs.NotificationUpdateJob;
import com.ericsson.cifwk.tm.application.jobs.RequirementUpdateJob;
import com.ericsson.cifwk.tm.application.jobs.TrsResendJob;
import com.ericsson.cifwk.tm.integration.RequirementManagement;
import com.google.inject.Inject;
import com.netflix.governator.annotations.Configuration;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;

@Singleton
public class JobScheduler {

    public static final int JOB_WAIT_TIMEOUT = 30;

    private final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    private final Scheduler scheduler;

    @Configuration("scheduler.job.hour")
    private int hour = 6;

    @Configuration("scheduler.job.minute")
    private int minute = 0;

    @Configuration("scheduler.job.day")
    private int day = 1;

    @Inject
    public JobScheduler(SchedulerFactory factory, GuiceJobFactory jobFactory) throws SchedulerException {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
    }

    /**
     * Called from Initialization Servlet to ensure everything is initialized properly
     *
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        scheduler.start();
        scheduleJobs();
    }

    private void scheduleJobs() throws SchedulerException {
        JobDetail requirementJob = JobBuilder.newJob(RequirementUpdateJob.class)
                .build();

        JobDetail defectJob = JobBuilder.newJob(DefectUpdateJob.class)
                .build();

        JobDetail notificationJob = JobBuilder.newJob(NotificationUpdateJob.class)
                .build();

        JobDetail dropUpdateJob = JobBuilder.newJob(DropUpdateJob.class)
                .build();

        JobDetail trsUpdateJob = JobBuilder.newJob(TrsResendJob.class)
                .build();

        scheduler.scheduleJob(requirementJob, buildTrigger());
        scheduler.scheduleJob(defectJob, buildTrigger());
        scheduler.scheduleJob(notificationJob, buildMonthlyTrigger());
        scheduler.scheduleJob(dropUpdateJob, buildDailyTrigger());
        scheduler.scheduleJob(trsUpdateJob, trigger());
    }

    private Trigger buildTrigger() {
        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(RequirementManagement.EXPIRY_HOURS - 1))
                .build();
    }

    private Trigger buildDailyTrigger() {
        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(hour, minute))
                .build();
    }

    private Trigger buildMonthlyTrigger() {
        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(day, hour, minute))
                .build();
    }

    private Trigger trigger() {
        return  TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * 1/1 * ? *"))
                .build();
    }


    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down scheduler");
        try {
            scheduler.shutdown(true);

            int sec = 0;
            while (sec++ < JOB_WAIT_TIMEOUT) {
                Thread.sleep(1000);
                if (scheduler.isShutdown()) {
                    logger.info("Shutting down scheduler successful");
                    return;
                }
            }
            logger.error("Unable to shutdown scheduler for sec " + sec);
        } catch (SchedulerException | InterruptedException e) {
            logger.error("Unable to shutdown scheduler: ", e);
        }
    }
}

