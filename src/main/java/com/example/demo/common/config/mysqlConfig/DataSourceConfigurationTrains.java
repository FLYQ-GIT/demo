package com.example.demo.common.config.mysqlConfig;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 配置 c3p0 数据源
 * @author coffee
 * 2019上午10:32:16
 */
@SpringBootConfiguration
@MapperScan(basePackages = "com.example.demo.trainsModule", sqlSessionTemplateRef  = "trainsSqlSessionTemplate")
public class DataSourceConfigurationTrains {
	
	private final Logger logger = LoggerFactory.getLogger(DataSourceConfigurationTrains.class);
	
	
	
	@Bean(name="dataSourceTrain")	// 将对象放到spring容器中
	@Qualifier(value = "dataSourceTrain")
	@Primary	// 默认数据源
	@ConfigurationProperties(prefix = "spring.datasource.c3p0.trains")	// c3p0参数前缀
	public DataSource dataSource(){
		
		logger.info("----------------------{}------------------------", "初始化c3p0[trains]");
		DataSource dataSource = DataSourceBuilder.create().type(ComboPooledDataSource.class).build();
		return dataSource;
	}
	

    /**
     * 创建工厂
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean(name = "trainsSqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSourceTrain") DataSource dataSource,@Qualifier("mybatisPropertiesTrains")MybatisProperties properties,ResourceLoader resourceLoader) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        // MyBatis主配置文件的位置
        bean.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
        // mapper 文件的位置
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(properties.getMapperLocations()[0]));
        // sql映射文件中的输入/输出参数设置类型别名
//        bean.setTypeAliasesPackage(properties.getTypeAliasesPackage());
        return bean.getObject();
    }
 
    /**
     * 创建事务
     * @param dataSource
     * @return
     * 使用方式 在server类/server指定方法上加@Transactional(transactionManager="trainsTransactionManager")
     * 弊端：由于配置多数据源后存在多个事务,导致同一个事务中同时处理多个表结构时,存在事务回滚问题
     */
    @Bean(name = "trainsTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("dataSourceTrain") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
    /**
     * 创建模板
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "trainsSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("trainsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
     * 定义配置文件
     * @return MybatisProperties
     */
    @Primary
    @Bean(name = "mybatisPropertiesTrains")
    @ConfigurationProperties(prefix = "mybatis.trains")
    public MybatisProperties mybatisPropertiesTrains() {
        return new MybatisProperties();
    }
}
