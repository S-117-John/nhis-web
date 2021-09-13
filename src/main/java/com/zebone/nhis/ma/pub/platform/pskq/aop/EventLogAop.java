package com.zebone.nhis.ma.pub.platform.pskq.aop;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.zebone.nhis.ma.pub.platform.pskq.service.impl.RestSysMessageServiceImpl;

@Component("eventLogSendAop")
@Aspect
public class EventLogAop {

    @Resource(name = "restSysMessageServiceImpl")
    private RestSysMessageServiceImpl restSysMessageService;

//    @AfterReturning(value="@annotation(com.zebone.nhis.ma.pub.platform.pskq.annotation.EventLog)",returning = "object")
//    public void before(JoinPoint joinPoint,Object object){
//    	String param = "";
//        try{
//            Object[] args = joinPoint.getArgs();
//            if(args.length!=1){
//                return;
//            }
//
//            param = (String) args[0];
//            if(object!=null&& (object instanceof String)){
//                restSysMessageService.save(param,(String)object);
//            }
//
//        }catch (Exception e){
//            restSysMessageService.save(param,object,"异常记录发送响应参数",e.getMessage());
//        }
//
//    }

    @Around(value="@annotation(com.zebone.nhis.ma.pub.platform.pskq.annotation.EventLog)")
    public Object around(ProceedingJoinPoint joinPoint){
        String param = "";
        Object object = null;
        try{
            Object[] args = joinPoint.getArgs();
            if(args.length!=1){
                return null;
            }

            param = (String) args[0];
            object = joinPoint.proceed(args);
            if(object!=null&& (object instanceof String)){
                restSysMessageService.save(param,(String)object);
            }

        }catch (Exception e){
            restSysMessageService.save(param,object,"异常记录发送响应参数",e.getMessage());
        } catch (Throwable throwable) {
            restSysMessageService.save(param,object,"异常记录发送响应参数",throwable.getMessage());
        }finally {
            return object;
        }

    }
}
