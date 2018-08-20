package com.tallybook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018/8/21.
 */
@Configuration
public class RedisConfig {
    private @Value("${redis.database}") String database;
    private @Value("${redis.host}") String host;
    private @Value("${redis.password}") String password;
    private @Value("${redis.port}") int port;
    private @Value("${redis.pool.max-idle}") int maxIdle;
    private @Value("${redis.pool.min-idle}") int minIdle;
    private @Value("${redis.pool.max-active}") int maxActive;
    private @Value("${redis.pool.max-wait}") long maxWait;

    private JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        //jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setDatabase(Integer.valueOf(database));
        jedisConnectionFactory.setPoolConfig(poolConfig());
        return jedisConnectionFactory;
    }

    /**初始化bean
     */
    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

}
