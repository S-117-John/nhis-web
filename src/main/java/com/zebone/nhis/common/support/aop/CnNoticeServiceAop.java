package com.zebone.nhis.common.support.aop;

import com.zebone.nhis.common.handler.CnNoticeHandler;
import com.zebone.nhis.common.module.cn.ipdw.CnNotice;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Map;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class CnNoticeServiceAop {

    private Logger logger = LoggerFactory.getLogger(CnNoticeServiceAop.class);

    @Autowired
    private CnNoticeHandler cnNoticeHandler;

    @AfterReturning(value = "execution(* com.zebone.nhis.common.service.CnNoticeService.save*(..))",returning="result")
    public void around(Object result) throws Throwable {
        //正常执行完成时，获取返回值
        complete(result);
    }

    @AfterReturning(value = "execution(* com.zebone.nhis.common.service.CnNoticeService.updateChkCnNotice(..))",returning="result")
    public void after(JoinPoint point, Object result) throws Throwable {
        //正常执行完之后操作缓存数据
        try {
            if(cnNoticeHandler.useCache()){
                if(result != null && result instanceof Map){
                    cnNoticeHandler.removeData((Map<String,Object>)result);
                }
            }
        } catch (Exception e){
            logger.error("删除cnNotice信息异常：",e);
        }
    }

    /**
     * 放入缓存，不要影响正常业务逻辑catch起来
     * @param object
     */
    private void complete(Object object){
        try{
            if(!cnNoticeHandler.useCache()){
                return;
            }
            if(object !=null && object instanceof CnNotice){
                cnNoticeHandler.add((CnNotice) object);
            }
        } catch (Exception e){
            logger.error("存放cnNotice信息异常：",e);
        }
    }


}
