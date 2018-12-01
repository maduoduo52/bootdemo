package com.mdd.admin.config.mybatisplus.mybatisplus.config;

import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 15:28
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisPlusConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MybatisProperties properties;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Autowired(required = false)
    private Interceptor[] interceptors;

    @Autowired(required = false)
    private DatabaseIdProvider databaseIdProvider;

    //ID类型
    @Value("${mybatis.configuration.id-type}")
    private Integer idType;
    //字段策略
    @Value("${mybatis.configuration.field-strategy}")
    private Integer fieldStrategy;
    //开启驼峰
    @Value("${mybatis.configuration.map-underscore-to-camel-case}")
    private boolean dbColumnUnderline;
    //是否刷新mapper
    @Value("${mybatis.configuration.refresh-mapper}")
    private boolean isRefresh;
    // 是否大写命名
    @Value("${mybatis.configuration.capital-mode}")
    private boolean isCapitalMode;

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    @Profile("dev")
    public PerformanceInterceptor performanceInterceptor() {
        log.info("===>mybatisplus SQL 性能插件注入");
        return new PerformanceInterceptor();
    }

    /**
     * mybatis-plus分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("===>mybatisplus SQL 分页插件注入");
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

    @Bean
    public OptimisticLockerInterceptor lockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * mp全局配置
     * 这里全部使用mybatis-autoconfigure 已经自动加载的资源。不手动指定
     * 配置文件和mybatis-boot的配置文件同步
     *
     * @return
     */
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() {
        log.info("===>mybatisplus全局配置start");
        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
        mybatisPlus.setDataSource(dataSource);
        mybatisPlus.setVfs(SpringBootVFS.class);
        if (StringUtils.hasText(this.properties.getConfigLocation())) {
            mybatisPlus.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        if (!ObjectUtils.isEmpty(this.interceptors)) {
            mybatisPlus.setPlugins(this.interceptors);
        }

        GlobalConfiguration global = new GlobalConfiguration(new LogicSqlInjector());
        global.setDbType(DBType.MYSQL.name());
        global.setIdType(idType);
        global.setCapitalMode(isCapitalMode);
        global.setDbColumnUnderline(dbColumnUnderline);
        global.setFieldStrategy(fieldStrategy);

        //逻辑字段值设置（1：表示逻辑删除，0：表示逻辑未删除）
        global.setLogicDeleteValue("1");
        global.setLogicNotDeleteValue("0");

        //自动字段注入
        global.setMetaObjectHandler(new EwMetaObjectHandler());
        mybatisPlus.setGlobalConfig(global);

        org.apache.ibatis.session.Configuration configuration = properties.getConfiguration();
        MybatisConfiguration mc = new MybatisConfiguration();
        mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        mc.setSafeRowBoundsEnabled(configuration.isSafeRowBoundsEnabled());
        mc.setSafeResultHandlerEnabled(configuration.isSafeResultHandlerEnabled());
        mc.setMapUnderscoreToCamelCase(configuration.isMapUnderscoreToCamelCase());
        mc.setAggressiveLazyLoading(configuration.isAggressiveLazyLoading());
        mc.setMultipleResultSetsEnabled(configuration.isMultipleResultSetsEnabled());
        mc.setUseGeneratedKeys(configuration.isUseGeneratedKeys());
        mc.setUseColumnLabel(configuration.isUseColumnLabel());
        mc.setCacheEnabled(configuration.isCacheEnabled());
        mc.setCallSettersOnNulls(configuration.isCallSettersOnNulls());
        mc.setUseActualParamName(configuration.isUseActualParamName());
        mc.setReturnInstanceForEmptyRow(configuration.isReturnInstanceForEmptyRow());
        mybatisPlus.setConfiguration(mc);

        if (this.databaseIdProvider != null) {
            mybatisPlus.setDatabaseIdProvider(databaseIdProvider);
        }
        if (StringUtils.hasLength(this.properties.getTypeAliasesPackage())) {
            mybatisPlus.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(this.properties.getTypeHandlersPackage())) {
            mybatisPlus.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(this.properties.resolveMapperLocations())) {
            mybatisPlus.setMapperLocations(this.properties.resolveMapperLocations());
        }
        log.info("===>mybatisplus全局配置end");
        return mybatisPlus;
    }
}
