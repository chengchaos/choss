package com.example.mytimer.demo;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * <strong>
 *     quartz-scheduler demo
 * </strong>
 * <br /><br />
 * http://www.quartz-scheduler.org/documentation/
 * </p>
 *
 * @author chengchao - 18-10-18 下午2:59 <br />
 * @since 1.0
 */
//@Component
public class QuartzSchedulerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzSchedulerDemo.class);

    private Map<String, Scheduler> schedulerHolder = new HashMap<>();


    public QuartzSchedulerDemo() {
        super();
    }


    public void start(String name, String cronExpression) throws SchedulerException {


        JobDetail job = JobBuilder.newJob(InvokeStatJob.class)
                .withIdentity(name, "group1")
                .build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey("myTrigger", "myTriggerGroup"))
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        this.schedulerHolder.put(name, scheduler);
        scheduler.scheduleJob(job, cronTrigger);
        scheduler.start();

    }

    public void stop(String name) {

        Scheduler scheduler = this.schedulerHolder.get(name);

        if (scheduler != null) {
            try {
                scheduler.shutdown(true);
                this.schedulerHolder.remove(name);
            } catch (SchedulerException e) {
                LOGGER.error("Scheduler shutdown Exception", e);
            }
        }
    }



    public static class InvokeStatJob implements Job {

        private static final Logger LOGGER = LoggerFactory.getLogger(InvokeStatJob.class);

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {

            JobKey jobKey = context.getJobDetail().getKey();
            TriggerKey triggerKey = context.getTrigger().getKey();

            LOGGER.info("时间: {}; Trigger: {}; JobKey: {}; ", new Date(), triggerKey, jobKey);
        }
    }
}

