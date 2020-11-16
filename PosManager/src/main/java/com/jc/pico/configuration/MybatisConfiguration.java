/*
 * Filename	: MybatisConfiguration.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;


import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages={
    "com.jc.pico.mapper",
    "com.jc.pico.utils",
    "com.jc.pico.utils.customMapper.admin",
    "com.jc.pico.utils.mybatis.typehandler"})
@EnableTransactionManagement
public class MybatisConfiguration {

  @Bean(destroyMethod = "close", name = "dataSource")
  public DataSource dataSource() {

	  final HikariConfig hikariConfig = new HikariConfig();
	  hikariConfig.setDriverClassName(Globals.DB_DRIVER);
	  hikariConfig.setJdbcUrl(Globals.DB_URL);
	  hikariConfig.setUsername(Globals.DB_USER);
	  hikariConfig.setPassword(Globals.DB_PASSWORD);
	  hikariConfig.setMaximumPoolSize(20);
	  //hikariConfig.setConnectionTestQuery("select 1");
	  hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
	  hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
	  hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
	  hikariConfig.addDataSourceProperty("useServerPrepStmts", true);
	  
	  
	  final HikariDataSource dataSource = new HikariDataSource(hikariConfig);
 
//	  DataSource dataSource = new DataSource();
//
//	  dataSource.setDriverClassName(Globals.DB_DRIVER);
//	  dataSource.setUrl(Globals.DB_URL);
//	  dataSource.setUsername(Globals.DB_USER);
//	  dataSource.setPassword(Globals.DB_PASSWORD);
//	  dataSource.setValidationQuery(Globals.DB_TEST_QUERY);
//	  dataSource.setTestWhileIdle(true);
//	  dataSource.setTestOnBorrow(true);
//	  dataSource.setTimeBetweenEvictionRunsMillis(Globals.DB_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
//	  dataSource.setMaxActive(Globals.DB_MAX_ACTIVE);
//	  dataSource.setMaxIdle(Globals.DB_MAX_IDLE);
//	  dataSource.setMinIdle(Globals.DB_MIN_IDLE);
//	  dataSource.setMaxWait(Globals.DB_MAX_WAIT);
//	
	  return dataSource;
  }


  @Bean(name = "dataSourceTransactionManager")
  public PlatformTransactionManager dataSourceTransactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());

    List<Resource> resources = Lists.newArrayList();
    String paths[] = Globals.DB_RESOURCE_PATH.split(";");
    for (int i = 0; i < paths.length; i++) {
      List<Resource> res = Arrays.asList(new PathMatchingResourcePatternResolver().getResources(paths[i]));
      resources.addAll(res);
    }

    sessionFactory.setMapperLocations(
      resources.toArray(new Resource[]{})
    );

    sessionFactory
      .setConfigLocation(new PathMatchingResourcePatternResolver().getResource(Globals.DB_CONFIGURATION_PATH));

    return sessionFactory.getObject();
  }

  @Bean
  public SqlSessionTemplate sessionTemplate() throws Exception {
    return new SqlSessionTemplate(sqlSessionFactoryBean());
  }

}
