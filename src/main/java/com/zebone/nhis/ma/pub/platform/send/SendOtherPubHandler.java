package com.zebone.nhis.ma.pub.platform.send;

import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 平台之外发送时调用,将异步调用互不影响
 */
public final class SendOtherPubHandler {

    private static final Logger logger = LoggerFactory.getLogger("nhis.otherPlatLog");

    public static void invokeOther(Class<?> targetClass, String methodName, Map<String, Object> paramMap) {
        try {
            Object bean = ServiceLocator.getInstance().getBean(targetClass);
            Method method = ReflectUtils.findDeclaredMethod(targetClass, methodName, new Class[]{Map.class});
            User user = UserContext.getUser();
            new Thread(()->{
                UserContext.setUser(user);
                try {
                    method.invoke(bean, paramMap);
                } catch (Exception e) {
                    logger.error("调用内部接口异常：",e);
                }
            }).start();
        } catch (Exception e){
            logger.error("调用远程接口异常：",e);
        }
    }


}
