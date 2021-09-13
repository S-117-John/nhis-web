package com.zebone.nhis.ma.pub.handler;

import com.zebone.nhis.ma.pub.service.SystemPubRealizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * NHIS EXT方式实现类（无事物）
 * 1.NHIS自身方法实现，如其他项目不需要重写，直接调用接口
 * * 2.当项目未配置基本实现时，默认走此实现
 *
 * @author wuqaing
 */
@Service
public class SystemPubRealizationHandler {

    @Resource
    private SystemPubRealizationService systemPubRealizationService;

    public Object invokeMethod(String methodName, Object... args) {
        Object result = null;
        switch (methodName) {
            case "builtAssistOcc":
                systemPubRealizationService.builtAssistOcc(args);
                break;
            case "handAnesthesiaExeRec":
                systemPubRealizationService.handAnesthesiaExeRec(args);
                break;
            default:

                break;
        }
        return result;
    }
}
