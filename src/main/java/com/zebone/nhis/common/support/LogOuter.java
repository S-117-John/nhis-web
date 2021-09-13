package com.zebone.nhis.common.support;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public class LogOuter implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean(name = "myLogFilter")
    public Slf4jLogFilter getSlf4jLogFilter(){
        Slf4jLogFilter logFilter = new Slf4jLogFilter();
        logFilter.setConnectionLogEnabled(false);
        logFilter.setStatementLogEnabled(true);
        logFilter.setResultSetLogEnabled(false);//开启后能看到实际执行fetch时间，查询出的字段，得到的结果集
        logFilter.setStatementExecutableSqlLogEnable(true);
        return logFilter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        MultiDataSource dataSource = (MultiDataSource) applicationContext.getBean("dataSource");
        DruidDataSource druidDataSource = (DruidDataSource) dataSource.getDataSource();
        druidDataSource.setProxyFilters(Lists.newArrayList(applicationContext.getBean("myLogFilter",Slf4jLogFilter.class)));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
