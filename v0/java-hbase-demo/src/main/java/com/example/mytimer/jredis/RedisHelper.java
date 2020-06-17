package com.example.mytimer.jredis;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisClusterScriptingCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ScriptingCommands;

/**
 * <p>
 * <strong>
 * 简短的描述
 * </strong>
 * <br /><br />
 * 详细的说明
 * </p>
 *
 * @author chengchao - 18-10-18 下午4:30 <br />
 * @since 1.0
 */
public class RedisHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisHelper.class);

    private static final String LOCK_SUCCESS = "OK";

    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * NX : 即当 key 不存在时，进行 set 操作；若 key 已经存在，不做任何操作；
     * XX : 则只有当 key 已经存在时才进行 set
     */
    private static final String SET_IF_NOT_EXISTS = "NX";

    /**
     * EXPX : 意思是我们要给这个 key 加一个过期的设置，具体时间由第五个参数决定。
     * EX : 秒;
     * PX : 毫秒;
     */
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final String SCRIPT =  "" +
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end"
            ;

    /**
     *
     * @param jedis Redis 客户端
     * @param lockKey 锁 Key
     * @param requestId 请求标识
     * @param expireTimeMillis 过期时间（毫秒）
     * @return 成功获取到锁： true； 失败： false；
     */
    public static boolean tryGetDistributedLock(JedisCommands jedis, String lockKey, String requestId, int expireTimeMillis) {

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXISTS, SET_WITH_EXPIRE_TIME, expireTimeMillis);

        LOGGER.debug("result : {}", result);
        LOGGER.info("result : {}", result);
        return (LOCK_SUCCESS.equals(result));
    }

    /**
     * 释放分布式锁
     * @param jedis Redis 客户端
     * @param lockKey 锁 Key
     * @param requestId 请求标识
     * @return 成功： true； 失败： false；
     */
    public static boolean releaseDistributedLock(ScriptingCommands jedis, String lockKey, String requestId) {

        Object result = jedis.eval(SCRIPT, 
                Collections.singletonList(lockKey),
                Collections.singletonList(requestId));
        
        LOGGER.debug("result : {}", result);
        LOGGER.info("result : {}", result);
        return (RELEASE_SUCCESS.equals(result));
    }

    public static boolean releaseDistributedLock(JedisClusterScriptingCommands jedis, String lockKey, String requestId) {

        Object result = jedis.eval(SCRIPT, 
                Collections.singletonList(lockKey),
                Collections.singletonList(requestId));
        
        LOGGER.debug("result : {}", result);
        LOGGER.info("result : {}", result);
        return (RELEASE_SUCCESS.equals(result));
    }
}
