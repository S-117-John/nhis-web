package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 发送和解析<br>
 *     只发送时调用 RequestTemplate即可；只解析调用ResponseResolver即可
 */
public class SendAndResolve {

    protected static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");

    private RequestTemplate requestTemplate;

    private ResponseResolver responseResolver;

    private SendDirectHandler sendDirectHandler;
    private SendAndResolve(){
        requestTemplate = ServiceLocator.getInstance().getBean(RequestTemplate.class);
        sendDirectHandler = ServiceLocator.getInstance().getBean(SendDirectHandler.class);
        responseResolver = new ResponseResolver();
    }

    private static class SendAndResolveHolder{
        private static SendAndResolve INSTANCE = new SendAndResolve();
    }

    public static SendAndResolve getInstance(){
        return SendAndResolveHolder.INSTANCE;
    }

    public Object send(HttpMethod httpMethod, RequestData requestData){
        List<String> reqUrls = sendDirectHandler.getReqUrls(requestData.getRemoteMethod(),requestData.getData());
        if(CollectionUtils.isNotEmpty(reqUrls)){
            for (String reqUrl : reqUrls) {
                try{
                    requestData.setUrlType(EnumUrlType.DIRECT);
                    requestData.setDirectUrl(reqUrl);
                    responseResolver.process(new ProcessData(requestData,requestTemplate.request(httpMethod,requestData)));
                } catch (Exception e){
                    logger.error("直连请求处理异常：",e);
                }
            }
            return null;
        }
        return responseResolver.process(new ProcessData(requestData,requestTemplate.request(httpMethod,requestData)));
    }


}
