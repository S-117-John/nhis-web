package com.zebone.nhis.webservice.pskq.aop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.nhis.webservice.pskq.annotation.DuplicateCommitLimit;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.platform.modules.exception.BusException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Component
@Aspect
public class DuplicateAspect {

    @Before(value="@annotation(com.zebone.nhis.webservice.pskq.annotation.DuplicateCommitLimit)")
    public void doBefore(JoinPoint joinPoint){
        Class<?> targetCls=joinPoint.getTarget().getClass();
        MethodSignature ms=(MethodSignature)joinPoint.getSignature();
        try {
            Method method=targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
            DuplicateCommitLimit duplicateCommitLimit = method.getAnnotation(DuplicateCommitLimit.class);
            if(duplicateCommitLimit!=null){
                Object[] args = joinPoint.getArgs();
                if(args.length!=1){
                    return;
                }
                String param = (String) args[0];
                Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
                RequestBody requestBody = gson.fromJson(param,RequestBody.class);
                long time = duplicateCommitLimit.time();
                String oldValue = (String) RedisUtils.getCacheObj(requestBody.getId());
                if(StringUtils.isEmpty(oldValue)){
                    RedisUtils.setCacheObj(requestBody.getId(),requestBody.getId(),time);
                }else {
                    throw new BusException("重复请求");
                }


            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
