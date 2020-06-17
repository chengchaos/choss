package com.example.mytimer.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * <strong>
 *     Spring 的 @Scheduled 注解
 * </strong>
 *
 * <strong>首先： 在 application 或者 AutoConfig 上增加： {@code @EnableScheduling} </strong>
 * <br /><br />
 * 类的要求：
 * <ul>
 *     <li>1, @Scheduled 注解要写在实现上。</li>
 *     <li>2, 定时器任务的方法不能有返回值</li>
 *     <li>3, 实现类上要有组件的注解 @Component, @Service, @Repository 等</li>
 * </ul>
 *
 * 注解参数说明：
 * <ul>
 *     <li> {@code @Scheduled(fixedRate=2000) }: 上一次开始执行时间点后2秒再次执行；</li>
 *     <li> {@code @Scheduled(fixedDelay=2000) }: 上一次执行完毕时间点后2秒再次执行；</li>
 *     <li> {@code @Scheduled(initialDelay=1000, fixedDelay=2000) }: 第一次延迟 1 秒执行，
 *         然后在上一次执行完毕时间点后2秒再次执行；</li>
 *     <li> {@code @Scheduled(cron="* * * * * ?") }：按cron规则执行。</li>
 * </ul>
 * ---------------------
 * </p>
 *
 * @author chengchao - 18-10-18 下午2:17 <br />
 * @since 1.0
 */
//@Component
public class SpringScheduledDemo {


    private static final Logger LOGGER = LoggerFactory.getLogger(SpringScheduledDemo.class);

    @Scheduled(cron = "0/5 * * * * ?")
    public void run() {
        LOGGER.info("方法执行……");
    }

    @Scheduled(fixedDelayString = "${jobs.fixedDelay}")
    public void getTask1() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        LOGGER.info("任务1,从配置文件加载任务信息，当前时间：{}", currentTime);
    }

    @Scheduled(cron = "${jobs.cron}")
    public void getTask2() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        LOGGER.info("任务2,从配置文件加载任务信息，当前时间：{}", currentTime);
    }

}
