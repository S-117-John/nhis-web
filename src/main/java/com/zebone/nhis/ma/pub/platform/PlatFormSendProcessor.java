package com.zebone.nhis.ma.pub.platform;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.send.IPlatFormSendHandler;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;

@Component
public class PlatFormSendProcessor {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute(final Map<String,Object> paramMap, final User user, final String methodName){
        //为当前线程注册一个新的事务同步处理器(默认是最低级别，若有多个，这是最后一个执行）。这里是当主线程事务提交后，再异步执行发送平台消息（这样子线程一定能拿到持久化到数据库的信息..）
        if ("1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0"))) {
            //对应配置文件msg.properties配置文件
            String processClass = ApplicationUtils.getPropertyValue("msg.processClass", "");
            Object bean = ServiceLocator.getInstance().getBean(processClass);
            if (bean != null) {
                IPlatFormSendHandler handler = (IPlatFormSendHandler) bean;
                final PlatFormMsgSendRun run = new PlatFormMsgSendRun(handler,methodName,paramMap, user);
                //当前线程中没有Spring事务管理器，直接执行、有就注册
                //TransactionSynchronizationManager.isActualTransactionActive()
                if(StringUtils.isBlank(TransactionSynchronizationManager.getCurrentTransactionName())){
                    executorService.execute(run);
                } else {
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            executorService.execute(run);
                        }
                    });
                }
            }
        }

    }

    class PlatFormMsgSendRun extends Thread{

        private IPlatFormSendHandler handler;
        private Map<String,Object> paramMap;
        private String methodName;
        private User user;
        private Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

        public PlatFormMsgSendRun(IPlatFormSendHandler handler,String methodName,Map<String,Object> paramMap,User user){
            this.handler = handler;
            this.methodName = methodName;
            this.paramMap = paramMap;
            this.user = user;
        }

        @Override
        public void run() {
            try{
                long startTime = System.currentTimeMillis();
                UserContext.setUser(user);
                Method method = handler.getClass().getMethod(methodName,Map.class);
                Object obj =  method.invoke(handler, paramMap);
                if("true".equals(ApplicationUtils.getPropertyValue("msg.send.log.enable", ""))){
                    long  exectime  = System.currentTimeMillis() - startTime;
                    logger.info("调用平台发送消息方法线程名："+Thread.currentThread().getName()+"【"+methodName+"】,执行耗时："+exectime+"毫秒！");
                }
            }catch(Exception e){
                logger.error("==========调用平台发送消息方法【"+methodName+"】失败,"+e.getMessage());
                e.printStackTrace();
            } finally {
                UserContext.cleanUser();
            }
        }
    }
}
