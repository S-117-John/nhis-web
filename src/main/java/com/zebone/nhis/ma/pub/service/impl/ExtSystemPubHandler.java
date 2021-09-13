package com.zebone.nhis.ma.pub.service.impl;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.handler.SystemPubRealizationHandler;
import com.zebone.nhis.ma.pub.service.IExtSystemService;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * NHIS EXT基础实现类（无事物）
 * 1.公用NHIS自身功能基本实现类
 * 2.当项目未配置基本实现时，默认走此实现
 *
 * @author wuqaing
 */
@Service("ExtSystemPubHandler")
public class ExtSystemPubHandler implements IExtSystemService {
    //private
    @Resource
    private SystemPubRealizationHandler systemPubRealizationHandler;

    @Override
    public Object processExtSystem(String systemName, String methodName, Object... args) {
        Object result = null;
        if (CommonUtils.isEmptyString(systemName) || CommonUtils.isEmptyString(methodName)) {
            throw new BusException("调用外部系统处理接口时，未传入系统名systemName或处理业务的方法名methodName");
        }
        switch (systemName) {
            //自动生成医技类执行
            case "builtAssistOcc":
                systemPubRealizationHandler.invokeMethod(methodName, args);
                break;
            case "handAnesthesiaExeRec":
                systemPubRealizationHandler.invokeMethod(methodName, args);
                break;
            //对应多个系统，可继续追加
            default:
                break;
        }
        return result;
    }
}
