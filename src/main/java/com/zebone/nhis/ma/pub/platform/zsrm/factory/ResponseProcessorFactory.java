package com.zebone.nhis.ma.pub.platform.zsrm.factory;

import com.zebone.nhis.ma.pub.platform.zsrm.annotation.EventResponse;
import com.zebone.nhis.ma.pub.platform.zsrm.factory.impl.DefaultResponseProcessor;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseProcessorFactory {

    private static ResponseProcessorFactory factory = new ResponseProcessorFactory();
    private static Map<String,ResponseProcessor> processorBeans = new ConcurrentHashMap<>();
    private ResponseProcessorFactory(){}
    public static ResponseProcessorFactory getInstance(){
        return factory;
    }

    /**
     * 依据methodCode获取处理器，如果没有，会使用默认,永远不返回空
     * @param methodCode
     * @return
     */
    public ResponseProcessor getProcessor(String methodCode){
        ResponseProcessor result = null;
        ResponseProcessor def = SpringContextHolder.getBean(DefaultResponseProcessor.class);
        if(StringUtils.isBlank(methodCode)){
            return def;
        }
        //TODO methodCode 和processorBeans一对多的，暂时不用调整~~他们统一调整了格式,这些没卵用了~~
        if(processorBeans.size()>0){
            result = processorBeans.get(methodCode);
        } else {
            //返回beanName为键的map~~转为EventResponse-value存储的
            Map<String, ResponseProcessor> beans = SpringContextHolder.getApplicationContext().getBeansOfType(ResponseProcessor.class);
            if(beans !=null && beans.size()>0){
                Collection<ResponseProcessor> processors = beans.values();
                for (ResponseProcessor processor : processors) {
                    if(processor.getClass().isAnnotationPresent(EventResponse.class)){
                        EventResponse eventResponse = processor.getClass().getAnnotation(EventResponse.class);
                        String code = eventResponse.value();
                        processorBeans.put(code, processor);
                        if(StringUtils.equalsIgnoreCase(methodCode, code)){
                            return processor;
                        }
                    }
                }
            }
        }

        return result==null?def:result;
    }
}
