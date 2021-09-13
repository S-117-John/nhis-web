package com.zebone.platform.config;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;
import java.util.Iterator;
import java.util.Map;

/**
 * ws服务通过cxf发布的配置类。会依据ws配置文件动态加载和发布类。如果类上已经自动托管到Spring，将跳过，但不影响发布。
 */
@Configuration
@Lazy(value = false)
public class WebServiceConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final static Logger logger = LoggerFactory.getLogger(WebServiceConfig.class);

    @Autowired
    private WebServiceBean webServiceBean;
    @Resource(name = Bus.DEFAULT_BUS_ID)
    private Bus bus;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        try {
            if(applicationContext.getParent()==null){
                Iterator<Map.Entry<String, String>> iterator = webServiceBean.getRoute().entrySet().iterator();
                ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;
                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
                while (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    Class<?> aClass = Class.forName(next.getValue());
                    //1.将对象放到Spring容器
                    Object object;
                    Map<String, ?> beans = applicationContext.getBeansOfType(aClass);
                    if(beans.size() ==0){
                        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
                        beanFactory.registerBeanDefinition(aClass.getSimpleName(),beanDefinitionBuilder.getBeanDefinition());
                        object = context.getBean(aClass.getSimpleName(), aClass);
                    } else {
                        object = beans.values().stream().findFirst();
                    }
                    //2.将绑定Endpoint
                    String urlKey = next.getKey();
                    Endpoint endpoint = new EndpointImpl(bus, object);
                    endpoint.publish("/"+urlKey);
                }
            }
        } catch (Exception e){
            logger.error("ws服务bean定义异常：",e);
            SpringApplication.exit(applicationContext);
        }
    }
}
