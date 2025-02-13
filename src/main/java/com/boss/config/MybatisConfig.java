package com.boss.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.boss.config.enums.DynamicTableTreadLocal;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zdy
 */
@Configuration
@Slf4j
public class MybatisConfig {
    @Autowired
    private Environment evn;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor paginationInterceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameParser = new DynamicTableNameInnerInterceptor();
        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>(2);
        tableNameHandlerMap.put("common_table", (sql, tableName) -> DynamicTableTreadLocal.INSTANCE.getTableName());
        dynamicTableNameParser.setTableNameHandler((sql, tableName) -> {
            TableNameHandler handler = tableNameHandlerMap.get(tableName);
            return handler != null ? handler.dynamicTableName(sql, tableName) : tableName;
        });
        paginationInterceptor.addInnerInterceptor(dynamicTableNameParser);

        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(false);
        paginationInnerInterceptor.setMaxLimit(100000L);
        paginationInnerInterceptor.setDbType(DbType.MYSQL);

        paginationInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return paginationInterceptor;
    }

    /**
     * 创建数据源对象
     * @param dataSourceName 数据源名称
     * @return data source
     */
    private HikariDataSource createDataSource(String dataSourceName) {
        HikariDataSource dataSource = new HikariDataSource();
        String prefix = "spring.datasource.dynamic.datasource." + dataSourceName + ".";
        dataSource.setJdbcUrl(evn.getProperty(prefix + "jdbc-url"));
        dataSource.setUsername(evn.getProperty(prefix + "username"));
        dataSource.setPassword(evn.getProperty(prefix + "password"));
        dataSource.setDriverClassName(evn.getProperty(prefix + "driver-class-name"));
        dataSource.setConnectionTimeout(Long.parseLong(Objects.requireNonNull(evn.getProperty("spring.datasource.dynamic.hikari.connection-timeout"))));
        //最大连接数
        dataSource.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(evn.getProperty("spring.datasource.dynamic.hikari.max-pool-size"))));
        //最大生命周期
        dataSource.setMaxLifetime(Long.parseLong(Objects.requireNonNull(evn.getProperty("spring.datasource.dynamic.hikari.max-lifetime"))));
        //最小空闲连接
        dataSource.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(evn.getProperty("spring.datasource.dynamic.hikari.minimum-idle"))));
        //判断连接是否有效
        dataSource.setConnectionTestQuery(evn.getProperty("spring.datasource.dynamic.hikari.connection-test-query"));
        return dataSource;
    }

    /**
     * spring boot 启动后将自定义创建好的数据源对象放到TargetDataSources中用于后续的切换数据源用
     *             (比如：DynamicDataSourceContextHolder.setDataSourceKey("dbMall")，手动切换到dbMall数据源
     * 同时指定默认数据源连接
     * @return 动态数据源对象
     */
    @Bean
    public DynamicDataSource dynamicDataSource() {
        //获取动态数据库的实例（单例方式）
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        //获取yml配置的数据源
        String datasource = evn.getProperty("spring.datasource.dynamic.all-datasource-name");
        if (StringUtils.isBlank(datasource)) {
            log.error("未配置数据源：spring.datasource.dynamic.all-datasource-name");
        }
        //自定义数据源key值，将创建好的数据源对象，赋值到targetDataSources中,用于切换数据源时指定对应key即可切换
        Map<Object, Object> map = new HashMap<>();
        //获取默认数据源
        String defaultDataSource = evn.getProperty("spring.datasource.dynamic.primary");
        String[] sources = datasource.split(",");
        for (String source : sources) {
            HikariDataSource curDataSource = createDataSource(source);
            map.put(source, curDataSource);
            if (defaultDataSource.equals(source)) {
                //设置默认数据源
                dynamicDataSource.setDefaultTargetDataSource(curDataSource);
            }
        }
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dynamicDataSource);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        factoryBean.setTypeAliasesPackage(evn.getProperty("mybatis-plus.typeAliasesPackage"));// 指定实体类所在的包
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(evn.getProperty("mybatis-plus.mapper-locations")));//扫描mapper.xml文件包
        factoryBean.setPlugins(mybatisPlusInterceptor());
        /**
         * mybatis全局配置
         */
        MybatisConfiguration configuration = new MybatisConfiguration();
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
        globalConfig.getDbConfig().setLogicDeleteValue(evn.getProperty("mybatis-plus.global-config.db-config.logic-delete-value"));
        globalConfig.getDbConfig().setLogicNotDeleteValue(evn.getProperty("mybatis-plus.global-config.db-config.logic-not-delete-value"));
        /**
         * 设置公共字段默认赋值
         */
//        globalConfig.setMetaObjectHandler(new TableMetaObjectHandler());
        factoryBean.setGlobalConfig(globalConfig);
        return factoryBean.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
