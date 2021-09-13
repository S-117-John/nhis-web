package com.zebone.nhis.ma.pub.zsba.handler;

import com.zebone.nhis.ma.pub.zsba.service.BaExtOwnBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname BaExtOwnBusinessHandler
 * @Description 博爱项目NHIS自身个性化服务实现Handler
 * @Date 2020-05-25 11:48
 * @Created by wuqiang
 */
@Service
public class BaExtOwnBusinessHandler {
    private Logger logger = LoggerFactory.getLogger("nhis.BaWebServiceLog");


    @Resource
    private BaExtOwnBusinessService baExtOwnBusinessService;
    public Object invokeMethod(String methodName, Object... args) {
        Object result = null;
        switch (methodName) {
            case "handAnesthesiaExeRec":
                baExtOwnBusinessService.handAnesthesiaExeRec(args);
                break;
            default:
                break;
        }
        return result;
    }

}
