package com.example.mytimer.demo;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * <strong>
 * 简短的描述
 * </strong>
 * <br /><br />
 * 详细的说明
 * </p>
 *
 * @author chengchao - 18-10-18 下午7:26 <br />
 * @since 1.0
 */
//@Service
public class RedisReadWriteService {


    private static final Logger LOGGER = LoggerFactory.getLogger(RedisReadWriteService.class);



    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void save() {

        LOGGER.info("stringRedisTemplate -> {}", stringRedisTemplate);

        LOGGER.info("save");
        stringRedisTemplate.boundValueOps("chengchao").set("好人");


        Long res = stringRedisTemplate.execute((RedisCallback<Long>) connection -> {
            Long size = connection.dbSize();
            LOGGER.info("dbSize = {}", size);
            // Can cast to StringRedisConnection if using a StringRedisTemplate
            StringRedisConnection conn = (StringRedisConnection) connection;
            System.err.println("stringRedisConnection: "+ conn);
            return conn.incr("my-incr");
        });

        System.out.println(">>>>>>>>>" + res);
    }

    public String read() {
        LOGGER.info("stringRedisTemplate -> {}", stringRedisTemplate);
        Object o = stringRedisTemplate.boundValueOps("chengchao2").get();
        return Objects.nonNull(o) ? o.toString() : "null";
    }
}
