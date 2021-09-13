package com.zebone.nhis.task.sms.executor;

import com.zebone.nhis.common.support.ApplicationUtils;


public class MessageExecutorFactory {

    private static MessageExecutorFactory factory = new MessageExecutorFactory();
    private MessageExecutorFactory(){}

    public static MessageExecutorFactory getInstance(){
        return factory;
    }

    public MessageExecutor getExecutor(){
        String enableMq = ApplicationUtils.getPropertyValue("msg.sms.mq","0");
        if("1".equals(enableMq)){
            return new MqMessageExecutor();
        }
        return new DefaultMessageExecutor();
    }

}
