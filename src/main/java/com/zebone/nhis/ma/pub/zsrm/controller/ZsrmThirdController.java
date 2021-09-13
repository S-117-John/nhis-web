package com.zebone.nhis.ma.pub.zsrm.controller;


import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.zsrm.annotation.EventMethod;
import com.zebone.nhis.ma.pub.zsrm.handler.ZsrmHrpPostHandler;
import com.zebone.nhis.ma.pub.zsrm.vo.HrpDoPriceVo;
import com.zebone.nhis.ma.pub.zsrm.vo.HrpPubReceiveVo;
import com.zebone.nhis.ma.pub.zsrm.vo.HrpResultVo;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.*;

@Controller
@RequestMapping("/static/api/zsrm")
public class ZsrmThirdController {

    private static Logger logger = LoggerFactory.getLogger("nhis.hrp");

    private static Map<String,Method> cacheMethod = new HashMap<>();
    private static ZsrmHrpPostHandler zsrmHrpPostHandler = new ZsrmHrpPostHandler();


    static {
        Method[] declaredMethods = zsrmHrpPostHandler.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            // 判断是否方法上存在注解  MethodInterface
            boolean annotationPresent = declaredMethod.isAnnotationPresent(EventMethod.class);
            if (annotationPresent) {
                EventMethod methodAnnotation = declaredMethod.getAnnotation(EventMethod.class);
                String eventCode = methodAnnotation.value();
                if(StringUtils.isNotBlank(eventCode)){
                    cacheMethod.put(eventCode, declaredMethod);
                }
            }
        }
    }

    @RequestMapping(value = "/hrpService", method = RequestMethod.POST)
    @ResponseBody
    public Object hrpMethod(@RequestBody String param){
        String result = "",errMsg=null,operation=null,id=null;
        try {
            logger.info("接受HRP入参内容:{}",param);
            //~~~~~~~~start~~~~~~解析到具体操作方法
            HrpPubReceiveVo receiveVo=JsonUtil.readValue(param,HrpPubReceiveVo.class);
            if(receiveVo==null){
                throw new BusException("解析参数失败！");
            }

            operation= receiveVo.getOperation();
            if(CommonUtils.isNull(operation)){
                throw new BusException("未获取到传入：operation");
            }
            //~~~~~~~~~~end~~~~~~~~
            Method method = cacheMethod.get(operation);
            if(method == null){
                final String mcode = operation;
                Optional<String> first = cacheMethod.keySet().stream().filter(code -> {
                    if (code != null && code.contains(",")) {
                        return Arrays.asList(code.split(",")).contains(mcode);
                    }
                    return false;
                }).findFirst();
                if(first.isPresent()){
                    method = cacheMethod.get(first.get());
                }
            }
            if(method == null){
                throw new BusException("未解析到具体方法："+operation);
            }

            id=receiveVo.getSourceId();

            result = (String) method.invoke(zsrmHrpPostHandler, param);
            logger.info("正常响应平台：入ID：{},出参内容：{}",id,result);
        } catch (Exception e){
            errMsg = ExceptionUtils.getRootCauseMessage(e);
            HrpResultVo resultvo=new HrpResultVo("0",errMsg,new ArrayList());
            result=JsonUtil.writeValueAsString(resultvo);
        }
        return result;
    }

}
