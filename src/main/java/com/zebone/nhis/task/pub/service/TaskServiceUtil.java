package com.zebone.nhis.task.pub.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;


public class TaskServiceUtil {
    public static Object processExtMethod(Object...args) {
        Object result = null;

        //获取自定义记费服务service，对应配置文件ext.properties配置文件
        String processClass = ApplicationUtils.getPropertyValue("org.quartz.class", "");
        if (CommonUtils.isNull(processClass)) {
            return result;
        }
        Object bean = ServiceLocator.getInstance().getBean(processClass);
        if (bean != null) {
            ITaskJobService handler = (ITaskJobService) bean;
            result = handler.sendSmsForTask(args);
        }

        return result;
    }
}
