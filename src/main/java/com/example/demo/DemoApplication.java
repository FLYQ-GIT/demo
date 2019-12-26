package com.example.demo;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author coffee
 * 2019下午2:05:23
 */
@SpringBootApplication
@EnableTransactionManagement			// 项目中要使用事务,spring boot启动类必须要开启事务
@MapperScan(value="com.example.demo")	// 扫描mapper包,当前项目中*dao.java,估dao层不需要再单独配置@Mapper
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) throws SQLException {
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
		
		DataSource dataSource = context.getBean(DataSource.class);
		logger.info("-------------------校验c3p0连接池是否配置成功：{}------------------------",dataSource.getClass());
	}

}
