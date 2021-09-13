package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.aop;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.RequestData;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.ZsphMsgService;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class ZsphExceptionAspect {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
    /**
     * 将com.zebone.nhis.ma.pub.platform.send.impl.zsph.service 包下
     * 除了ZsphPlatFormSendPubHandler类中抛出的其他异常都拦截一次做个日志记录。
     * 注意并不影响异常抛出
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "execution(* com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.*.*(..))" +
            "&&!execution(* com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.ZsphPlatFormSendPubHandler.*(..))",throwing="ex")
    public void dealException(JoinPoint joinPoint, Throwable ex){
        if(!(ex instanceof BusException)){
            log.error("发送平台消息异常o(╯□╰)o:",ex);
        }
        ZsphMsgService zsphMsgService = ServiceLocator.getInstance().getBean(ZsphMsgService.class);
        Object[] params = joinPoint.getArgs();
        if(params!=null && params.length>0 && params[0] instanceof Map){
            Map<String,Object> paramMap = (Map<String, Object>) params[0];
            String methodName = joinPoint.getSignature().getName();
            String data = JsonUtil.writeValueAsString(paramMap);
            RequestData requestData = RequestData.newBuilder()
                    .id(NHISUUID.getKeyId())
                    .remoteMethod(methodName)
                    .data(data)
                    .build();

            zsphMsgService.addMsg(requestData,"BUS_ERROR", ExceptionUtils.getRootCauseMessage(ex));
        }
    }
}
