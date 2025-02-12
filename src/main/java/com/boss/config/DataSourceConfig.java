package com.boss.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源配置
 * 支持MySQL主从、Oracle、PostgreSQL多数据源切换
 */
@Configuration
public class DataSourceConfig {

    /**
     * MySQL主数据源
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * MySQL从数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Oracle数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * PostgreSQL数据源
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.postgresql")
    public DataSource postgresqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 动态数据源
     */
    @Bean
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        
        // 配置所有数据源
        Map<Object, Object> targetDataSources = new HashMap<>(4);
        targetDataSources.put("master", masterDataSource());
        targetDataSources.put("slave", slaveDataSource());
        targetDataSources.put("oracle", oracleDataSource());
        targetDataSources.put("postgresql", postgresqlDataSource());
        
        // 设置默认数据源为master
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        
        return dynamicDataSource;
    }

    /**
     * 动态数据源实现
     */
    public static class DynamicDataSource extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceContextHolder.getDataSource();
        }
    }
} 