package com.example.demo.common.config.redisConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
public class RedisConfig1 extends RedisConfig{
	
	private static final Logger logger = LoggerFactory.getLogger(RedisConfig1.class);

    @Value("${spring.redis1.host}")
    private String host;

    @Value("${spring.redis1.port}")
    private int port;
    
    @Value("${spring.redis1.database}")
    private int dbIndex;

    @Value("${spring.redis1.pass}")
    private String password;

    @Value("${spring.redis1.timeout}")
    private int timeout;

    /**
     * 配置redis连接工厂
     *
     * @return
     */
    @Primary
    @Bean
    public RedisConnectionFactory defaultRedisConnectionFactory() {
    	logger.info("-------------------rides({})初始化加载------------------------", "RedisConfig1");
        return createJedisConnectionFactory(dbIndex, host, port, password, timeout);
    }

    /**
     * 配置RedisUtil 注入方式使用@Resource(name="") 方式注入
     *
     * @return
     */
    @Bean(name = "redisUtil1")
    public RedisUtil redisUtil() {
    	RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    	redisTemplate.setConnectionFactory(defaultRedisConnectionFactory());
    	setSerializer(redisTemplate);
    	redisTemplate.afterPropertiesSet();
    	RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        // 从错误信息知,需要先调用afterPropertiesSet方法,此方法是应该是初始化参数和初始化工作
        // java.lang.IllegalArgumentException: template not initialized; call afterPropertiesSet() before using it
        return redisUtil;
    }

}
