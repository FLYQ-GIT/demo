package com.example.demo.common.config.redisConfig;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @Description 设置Redis多实例的基类
 * @author coffee
 * 2019年11月5日
 */
@Configuration
@EnableCaching
public class RedisConfig {
	
	@Value("${spring.redis.maxActive}")
    private int redisPoolMaxActive;

    @Value("${spring.redis.maxWait}")
    private int redisPoolMaxWait;

    @Value("${spring.redis.maxIdle}")
    private int redisPoolMaxIdle;

    @Value("${spring.redis.testOnBorrow}")
    private boolean redisPoolTestOnBorrow;
   
    @Value("${spring.redis.testOnReturn}")
    private boolean redisPoolTestOnReturn;
	
	/**
     * 配置Key的生成方式
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(o.getClass().getName())
                        .append(method.getName());
                for (Object object : objects) {
                    stringBuilder.append(object.toString());
                }
                return stringBuilder.toString();
            }
        };
    }

    /**
     * 创建redis连接工厂
     *
     * @param dbIndex
     * @param host
     * @param port
     * @param password
     * @param timeout
     * @return
     */
    public JedisConnectionFactory createJedisConnectionFactory(int dbIndex, String host, int port, String password, int timeout) {
    	JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setDatabase(dbIndex);
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setTimeout(timeout);
        jedisConnectionFactory.setPoolConfig(setPoolConfig(redisPoolMaxActive, redisPoolMaxWait, redisPoolMaxIdle, redisPoolTestOnBorrow, redisPoolTestOnReturn));
        return jedisConnectionFactory;

    }

    /**
     * 配置CacheManager
     *
     * @param redisTemplate
     * @return
     */
    @SuppressWarnings("rawtypes")
	@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        return redisCacheManager;
    }

    /**
     * 设置连接池属性
     *
     * @param maxIdle
     * @param minIdle
     * @param maxActive
     * @param maxWait
     * @param testOnBorrow
     * @return
     */
    public static JedisPoolConfig setPoolConfig(int maxActive, int maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn) {
    	JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        return poolConfig;
    }

    /**
     * 设置RedisTemplate的序列化方式
     *
     * @param redisTemplate
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setSerializer(RedisTemplate redisTemplate) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置键（key）的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置值（value）的序列化方式
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
    }
}
