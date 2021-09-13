package com.zebone.nhis.pi.pub.service;

import com.zebone.nhis.common.handler.PiCodeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class PiCodeFactory implements ApplicationContextAware {

    @Value("#{applicationProperties['nhis.picode.handlerClass']}")
    private String piCodeHandlerClass;

    private ApplicationContext applicationContext;

    private PiCodeHandler piCodeHandler;

    @PostConstruct
    public void init() {
        piCodeHandler = applicationContext.getBean(StringUtils.isBlank(piCodeHandlerClass)?"defaultPiCoderHandler":piCodeHandlerClass, PiCodeHandler.class);
    }

    public PiCodeHandler getHandler() {
        return piCodeHandler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
