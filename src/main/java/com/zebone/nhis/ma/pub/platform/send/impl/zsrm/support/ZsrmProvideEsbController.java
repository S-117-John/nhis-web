package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import cn.org.zxrl.util.Const;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.support.config.RateLimitConfig;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.service.ZsphPlatFormSendOpHandler;
import com.zebone.nhis.ma.pub.platform.zsrm.annotation.EventSearch;
import com.zebone.nhis.ma.pub.platform.zsrm.factory.ResponseProcessorFactory;
import com.zebone.nhis.ma.pub.platform.zsrm.service.SysMessageService;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ImplicitRulesResolver;
import com.zebone.nhis.ma.pub.platform.zsrm.support.ZsphMsgUtils;
import com.zebone.nhis.ma.pub.zsrm.annotation.EventMethod;
import com.zebone.nhis.ma.pub.zsrm.vo.HrpResultVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/static/api/")
public class ZsrmProvideEsbController {
    private static Logger logger = LoggerFactory.getLogger("nhis.esb");
    private static Map<String, Method> cacheMethod = new HashMap<>();

    @Resource(name = "zsrmSysMsgService")
    private SysMessageService sysMessageService;
    ZsphPlatFormSendOpHandler zsphPlatFormSendOpHandler = new ZsphPlatFormSendOpHandler();
    @PostConstruct
    public void init(){
        Method [] declaredMethods = ZsphPlatFormSendOpHandler.class.getDeclaredMethods();
        for(Method declaredMethod : declaredMethods){
            boolean isAnnotationPresent = declaredMethod.isAnnotationPresent(EventMethod.class);
            if(isAnnotationPresent){
                EventMethod eventMethod = declaredMethod.getAnnotation(EventMethod.class);
                String eventCode = eventMethod.value();
                if(StringUtils.isNotBlank(eventCode)){
                    cacheMethod.put(eventCode, declaredMethod);
                }
            }
        }
    }

    @RequestMapping(value = "/esbService", method = RequestMethod.POST)
    @ResponseBody
    public Object esbMethod(@RequestBody String param){
        String result = null, errMsg = null, methodCode = null, id=null;
        try{
            logger.info("接受平台入参内容: {}", param);
            ImplicitRulesResolver.ApiInfo apiInfo = ImplicitRulesResolver.getImplicitRules(param);
            methodCode = apiInfo.getImplicitRules();
            //id = apiInfo.getId();
            Method method = cacheMethod.get(methodCode);
            if(method == null){
                final String mcode = methodCode;
                Optional<String> first = cacheMethod.keySet().stream().filter(code -> {
                    if(code != null && code.contains(",")){
                        return Arrays.asList(code.split(",")).contains(mcode);
                    }
                    return false;
                }).findFirst();
                if(first.isPresent()){
                    method = cacheMethod.get(first.get());
                }
            }
            if(method == null){
            throw new BusException("未解析到具体方法" + methodCode);
            }
            User user = new User();
            user.setPkOrg(apiInfo.getHospitalId());
            user.setNameEmp("pt~~");
            UserContext.setUser(user);
            addRequestAttr(StringUtils.isBlank(apiInfo.getSourceSign())? "NONE" : apiInfo.getSourceSign() + "_" + apiInfo.getImplicitRules());
            ImplicitRulesResolver.setApiInfo(apiInfo);
            Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
            result = (String) method.invoke(zsphPlatFormSendOpHandler, paramMap);
            logger.info("正常响应平台：入ID：{},出参内容：{}",id,result);
        }catch(Exception e){
            errMsg = ExceptionUtils.getRootCauseMessage(e);
            if(!(ExceptionUtils.getRootCause(e) instanceof cn.org.zxrl.exception.BusinessException)){
                logger.error("接受平台请求处理异常,入ID：{},出参内容：{}",id,result,e.getMessage());
            }
            result = ZsphMsgUtils.getJsonStr(ResponseProcessorFactory.getInstance().getProcessor(methodCode).create(ZsphMsgUtils.createErrResponse(e)));
        }finally {
            ImplicitRulesResolver.clearApiInfo();
            if(methodCode != null){
                sysMessageService.saveReceiveMessage(StringUtils.replace(id, "_", ""), methodCode, param,errMsg);
            }
        }
        return result;
    }

    @ExceptionHandler
    @ResponseBody
    public Object exceptionHandler(Exception e){
        logger.error("接受平台请求其他异常：",e);
        return ZsphMsgUtils.getJsonStr(ResponseProcessorFactory.getInstance().getProcessor("*").create(ZsphMsgUtils.createErrResponse(e)));
    }

    public void addRequestAttr(String customValue){
        try{
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            request.setAttribute(Const.CUSTOM, RateLimitConfig.CUSTOM_PRX_ZSRM_API+customValue);
        } catch (Exception e){
            logger.error("设置request属性异常：",e);
        }
    }
}
