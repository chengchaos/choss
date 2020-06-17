package com.example.mytimer;

import com.example.mytimer.demo.QuartzSchedulerDemo;
import com.example.mytimer.demo.RedisReadWriteService;
import com.example.mytimer.jredis.RedisHelper;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
//@EnableScheduling
@ComponentScan("cn.futuremove.tsp.uni.config.dbs")
public class DemoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);


	public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

//        QuartzSchedulerDemo quartzSchedulerDemo = context.getBean(QuartzSchedulerDemo.class);
//        RedisReadWriteService redisReadWriteService = context.getBean(RedisReadWriteService.class);
//        redisReadWriteService.save();
//        LOGGER.info(quartzSchedulerDemo.toString());
//
//        String quartzJobName = "mydemo";
//        try {
//            quartzSchedulerDemo.start(quartzJobName, "0/3 * * * * ?");
//        } catch (SchedulerException e) {
//            LOGGER.error("SchedulerException : ", e);
//        }
//
//        JedisCluster jedisCluster = context.getBean(JedisCluster.class);
//
//        System.err.println("JedisCluster -> "+ jedisCluster);
//
//        String s;
//        for (int i = 0 ; i < 2; i++) {
//            //jedisCluster.set(i + "", "chengchao" + i);
////            s = jedisCluster.get(i + "");
//
//            boolean b1 = RedisHelper.tryGetDistributedLock(jedisCluster, "mykey", "i", 10000);
//            boolean b2 = RedisHelper.releaseDistributedLock(jedisCluster, "mykey", "i");
//            LOGGER.info("b1 : {}, b2 : {}", b1, b2);
//        }
//
//
//
//
//
//        //TimerDemo.execute();
//
//        try {
//            TimeUnit.MINUTES.sleep(1L);
//        } catch (InterruptedException e) {
//            LOGGER.warn("SchedulerException : ", e);
//            Thread.currentThread().interrupt();
//        }
//
//        String v = redisReadWriteService.read();
//        System.out.println(" v ============> "+ v);
//        quartzSchedulerDemo.stop(quartzJobName);
    }
}
