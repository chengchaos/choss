package com.example.mytimer.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 * <strong>
 * 简短的描述
 * </strong>
 * <br /><br />
 * 如果在多个函数上使用了 {@code @Scheduled}，那么一定是一个执行完毕，才能排下一个。
 * 这往往不是我们想要的效果。此时需要在 Scheduling 配置类为 schedule 返回一个预定的线程池，如下：
 * </p>
 *
 * @author chengchao - 18-10-18 下午2:47 <br />
 * @since 1.0
 */

@Configuration
//@EnableScheduling
public class SpringSchedulingConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringSchedulingConfiguration.class);

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        LOGGER.info("SpringSchedulingConfiguration.taskScheduler running ...");
        return Executors.newScheduledThreadPool(2);
    }
}
