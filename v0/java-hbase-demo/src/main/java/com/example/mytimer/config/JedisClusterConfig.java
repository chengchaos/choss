package com.example.mytimer.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * https://blog.csdn.net/liubenlong007/article/details/53689306
 */
@Configuration
public class JedisClusterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.cluster")
    public SpringRedisClusterNodes springRedisClusterNodes() {
        return new SpringRedisClusterNodes();
    }

    // // Redis服务器地址
    // @Value("${spring.redis.host}")
    // private String host;
    //
    // // Redis服务器连接端口
    // @Value("${spring.redis.port}")
    // private int port;
    //
    // // Redis服务器连接密码（默认为空）
    // @Value("${spring.redis.password}")
    // private String password;

    // 连接超时时间（毫秒）
    @Value("${spring.redis.timeout}")
    private int timeout;

    // Redis数据库索引（默认为0）
    @Value("${spring.redis.database}")
    private int database;

    // 连接池最大连接数（使用负值表示没有限制）
    @Value("${spring.redis.pool.max-total}")
    private int maxTotal;

    // 连接池最大阻塞等待时间（使用负值表示没有限制）
    @Value("${spring.redis.pool.max-wait-millis}")
    private int maxWaitMillis;

    // 连接池中的最大空闲连接
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    // 连接池中的最小空闲连接
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Bean(name = "redisClusterConfiguration")
    public RedisClusterConfiguration redisClusterConfiguration(SpringRedisClusterNodes springRedisClusterNodes) {

        String nodes = springRedisClusterNodes.getNodes().stream().collect(Collectors.joining(","));

        Map<String, Object> source = new HashMap<>();
        source.put("spring.redis.cluster.nodes", nodes);
        source.put("spring.redis.cluster.timeout", timeout);
        source.put("spring.redis.cluster.max-redirects", 5);

        LOGGER.info("创建 RedisClusterConfiguration");
        PropertySource<?> propertySource = new MapPropertySource("RedisClusterConfiguration", source);

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(propertySource);
        LOGGER.info("RedisClusterConfiguration -> {}", redisClusterConfiguration);
        return redisClusterConfiguration;
       
    }

    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);
        config.setTestWhileIdle(true);

        return config;
    }

    /**
     * {@code @Bean } 和 {@code @ConfigurationProperties} 该功能在官方文档是没有提到的， 我们可以把
     * {@code @ConfigurationProperties} 和 {@code @Bean } 和在一起使用。 举个例子，我们需要用
     * {@code @Bean } 配置一个 Config 对象，Config 对象有a，b，c成员变量需要配置， 那么我们只要在 yml 或
     * properties 中定义了a=1,b=2,c=3， 然后通过 {@code @ConfigurationProperties} 就能把值注入进
     * Config 对象中
     * 
     * @return
     */
    @Bean(name = "jedisPoolConfig")
    @ConfigurationProperties(prefix = "spring.redis.pool")
    public JedisPoolConfig jedisPoolConfig2() {

        LOGGER.info("创建 JedisPoolConfig");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setTestOnCreate(true);
        config.setTestWhileIdle(true);
        LOGGER.info("JedisPoolConfig -> {}", config);

        return config;
    }

    @Bean(name = "jedisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(
            @Qualifier(value = "redisClusterConfiguration") RedisClusterConfiguration clusterConfig,
            @Qualifier(value = "jedisPoolConfig") JedisPoolConfig jedisPoolConfig) {

        LOGGER.info("创建 JedisConnectionFactory");
        JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfig, jedisPoolConfig);
        LOGGER.info("JedisConnectionFactory -> {}", factory);
        return factory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(
            @Qualifier(value = "jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {

        LOGGER.info("创建 RedisTemplate");
        StringRedisTemplate stringRedisTemplate = this.stringRedisTemplate(redisConnectionFactory);
        LOGGER.info("RedisTemplate -> {}", stringRedisTemplate);
        
        if (stringRedisTemplate != null) {
            return stringRedisTemplate;
        }
        
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(true);
        LOGGER.info("RedisTemplate -> {}", redisTemplate);

        return redisTemplate;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        LOGGER.info("创建 StringRedisTemplate");
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        LOGGER.info("redisTemplate -> {}", redisTemplate);

        return redisTemplate;
    }

    protected static final int DEFAULT_TIMEOUT = 2000;
    protected static final int DEFAULT_MAX_REDIRECTIONS = 5;

    @Bean
    public JedisCluster jedisCluster(SpringRedisClusterNodes springRedisClusterNodes) {

        LOGGER.info("\nclusterNodes ------------------------------------------: {}",
                springRedisClusterNodes.getNodes());
        LOGGER.info("\nclusterNodes ------------------------------------------: {}", jedisPoolConfig2());

        Set<HostAndPort> nodes = springRedisClusterNodes.getNodes().stream().map(HostAndPort::parseString)
                .collect(Collectors.toSet());

        JedisCluster jedisCluster = new JedisCluster(nodes, timeout, 30, 8,
                // Constant.REDIS_CACHE_CLUSTER_PASSWORD,
                new GenericObjectPoolConfig());
        System.err.println("JedisCluster -> " + jedisCluster);

        return jedisCluster;
    }
}
