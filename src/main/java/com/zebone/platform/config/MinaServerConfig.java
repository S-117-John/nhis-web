package com.zebone.platform.config;

import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7CodeFactory;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.shiro.util.ClassUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@ConditionalOnExpression("#{'true'.equalsIgnoreCase(environment['mina.server'])}")
public class MinaServerConfig {

    @Value("${mina.server.handler}")
    private String handlerClass;

    @Bean("executorFilter")
    public ExecutorFilter executorFilter(){
        return new ExecutorFilter();
    }

    /**
     * 日志信息注入过滤器，MDC(Mapped Diagnostic Context有译作线程映射表)是日志框架维护的一组信息键值对，可向日志输出信息中插入一些想要显示的内容。
     * @return
     */
    @Bean
    public MdcInjectionFilter mdcInjectionFilter(){
        return new MdcInjectionFilter(MdcInjectionFilter.MdcKey.remoteAddress);
    }

    @Bean
    public ProtocolCodecFilter protocolCodecFilter(){
        return new ProtocolCodecFilter(new Hl7CodeFactory());
    }

    @Bean
    public LoggingFilter loggingFilter(){
        return new LoggingFilter();
    }

    @Bean
    public IoFilterChainBuilder filterChainBuilder(@Qualifier("executorFilter") ExecutorFilter executorFilter,MdcInjectionFilter mdcInjectionFilter
            ,ProtocolCodecFilter protocolCodecFilter,LoggingFilter loggingFilter){
        DefaultIoFilterChainBuilder filterChainBuilder = new DefaultIoFilterChainBuilder();
        Map<String,IoFilter> filters = new LinkedHashMap<>();
        filters.put("executor",executorFilter);
        filters.put("mdcInjectionFilter",mdcInjectionFilter);
        filters.put("codecFilter",protocolCodecFilter);
        filters.put("loggingFilter",loggingFilter);
        filterChainBuilder.setFilters(filters);
        return filterChainBuilder;
    }

    @Bean(value = "ioAcceptor",initMethod = "bind",destroyMethod = "unbind")
    public IoAcceptor acceptor(IoFilterChainBuilder filterChainBuilder){
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setFilterChainBuilder(filterChainBuilder);
        acceptor.setDefaultLocalAddress(new InetSocketAddress("localhost",1235));
        acceptor.setHandler((IoHandler) ClassUtils.newInstance(handlerClass));
        acceptor.setReuseAddress(true);
        return acceptor;
    }
}
