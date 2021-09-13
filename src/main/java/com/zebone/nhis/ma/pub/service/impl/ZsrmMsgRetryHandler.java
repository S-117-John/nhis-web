package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.service.IMsgRetryHandler;
import com.zebone.nhis.ma.pub.service.MsgRetryProcessor;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class ZsrmMsgRetryHandler implements IMsgRetryHandler, ApplicationContextAware {

    private Map<String,MsgRetryProcessor> retryProcessorMap;

    @Override
    public void send(SysMsgRec sysMsgRec) {
        if(sysMsgRec == null){
            return;
        }
        if(StringUtils.isBlank(sysMsgRec.getSysCode())) {
            throw new BusException("sysCode传入不能为空");
        }
        MsgRetryProcessor msgRetryProcessor = retryProcessorMap.get(sysMsgRec.getSysCode());
        if(msgRetryProcessor == null){
            throw new BusException("没有匹配到合适的重发处理器MsgRetryProcessor");
        }
        msgRetryProcessor.send(sysMsgRec);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ConcurrentHashMap<String,MsgRetryProcessor> map = new ConcurrentHashMap<>();
        retryProcessorMap = applicationContext.getBeansOfType(MsgRetryProcessor.class);
        if(retryProcessorMap != null && retryProcessorMap.size()>0){
            retryProcessorMap.forEach((s, mrp) -> {
                if(mrp!=null && mrp.getSysCode()!=null){
                    map.put(mrp.getSysCode(), mrp);
                }
            });
        }
        retryProcessorMap = map;
    }
}
