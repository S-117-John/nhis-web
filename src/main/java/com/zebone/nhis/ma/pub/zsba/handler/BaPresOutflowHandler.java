package com.zebone.nhis.ma.pub.zsba.handler;

import com.zebone.nhis.ma.pub.zsba.service.BaPresOutflowService;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 处方外流相关接口实现
 */
@Service
public class BaPresOutflowHandler {

    private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");

    @Resource
    private BaPresOutflowService baPresOutflowService;

    public Object invokeMethod(String methodName, Object... args) {
        Object result;
        try {
            Method method = ReflectUtils.findDeclaredMethod(baPresOutflowService.getClass(), methodName, new Class[]{args==null?Object[].class:args.getClass()});
            result = method.invoke(baPresOutflowService, new Object[]{args});
        } catch (Exception e) {
            logger.error("处方外流-其他异常：",e);
            throw new BusException("其他异常："+ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }
}
