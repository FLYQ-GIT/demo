package com.example.demo.common.config.redisConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.demo.common.utils.RedisUtil;

/**
 * 
 * @Description 配置默认Redis操作实例 到Spring中
 * @author coffee
 * 2019年11月5日
 */
@Configuration
@EnableCaching
public class RedisConfig2 extends RedisConfig{
	
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig2.class);

    @Value("${spring.redis2.host}")
    private String host;

    @Value("${spring.redis2.port}")
    private int port;
    
    @Value("${spring.redis2.database}")
    private int dbIndex;

    @Value("${spring.redis2.pass}")
    private String password;

    @Value("${spring.redis2.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory() {
    	logger.info("-------------------rides({})初始化加载------------------------", "RedisConfig2");
    	return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置RedisUtil 注入方式使用@Resource(name="") 方式注入
     *
     * @return
     */
    @Bean(name = "redisUtil2")
    public RedisUtil redisUtil() {
    	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    	redisTemplate.setConnectionFactory(cacheRedisConnectionFactory());
    	setSerializer(redisTemplate);
    	redisTemplate.afterPropertiesSet();
    	RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
}
