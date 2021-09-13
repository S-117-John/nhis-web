package com.zebone.nhis.webservice.lbzy.support;

import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Component
@Aspect
public class LbzySelfExceptionAspect {
    private static Logger log = LoggerFactory.getLogger("nhis.lbzySelfServiceLog");

    @Resource(name = "transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    @Around(value = "execution(* com.zebone.nhis.webservice.cxf.impl.NHISLbzySelfWebServiceImpl.*(..))")
    public Object dealException(ProceedingJoinPoint joinPoint){
        Object object=null;
        TransactionStatus status = null;
        String methodName = null;
        try {
            methodName = joinPoint.getSignature().getName();
            if(joinPoint.getArgs() ==null || joinPoint.getArgs().length==0){
                throw new BusException("入参不能为空");
            }
            log.info("{}-入参：{}",methodName,joinPoint.getArgs()[0]);
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            status = platformTransactionManager.getTransaction(def);
            object = joinPoint.proceed(joinPoint.getArgs());
            if(!status.isCompleted()){
                platformTransactionManager.commit(status);
            }
        } catch (Throwable throwable) {
            if(!status.isCompleted()) {
                platformTransactionManager.rollback(status);
            }
            String err = throwable.getMessage();
            if(!(throwable instanceof BusException)){
                err = ExceptionUtils.getRootCauseMessage(throwable);
            }
            log.error("自助机接口处理异常：",throwable);
            err = err==null?"内部异常":(err.length()>100?err.substring(0,100)+"...":err);
            object = XmlUtil.beanToXml(new Response("-1",err),Response.class);
        }
        log.info("{}-响应：{}",methodName, object);
        return object;
    }

    @XmlRootElement(name = "Response")
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class Response{

        @XmlElement(name = "ResultCode")
        private String code;

        @XmlElement(name = "ErrorMsg")
        private String msg;

        public Response() {
        }

        public Response(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
