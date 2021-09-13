package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.EnumUrlType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * 发送请求<br>
 *     注意URL配置为http://xxxx.xxx.xx/{remoteMethod}
 */
@Component
public class RequestTemplate implements InitializingBean {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");


    private RestTemplate restTemplate;

    private String urlBase = null;
    private String url = null;

    public String get(RequestData requestData){
        return request(HttpMethod.GET,requestData).getData();
    }

    public ResponseData post(RequestData requestData){
        return request(HttpMethod.POST,requestData);
    }

    public ResponseData put(RequestData requestData){
        return request(HttpMethod.POST,requestData);
    }

    public ResponseData delete(RequestData requestData){
        return request(HttpMethod.POST,requestData);
    }

    public ResponseData request(HttpMethod httpMethod, RequestData requestData){
        ResponseData responseData = new ResponseData();
        try {
            String url;
            //直连优先级最高
            if(requestData.getDirectUrl()!=null && requestData.getUrlType()!=null && requestData.getUrlType() == EnumUrlType.DIRECT) {
                url = requestData.getDirectUrl();
            } else {
                url = requestData.getUrlType()==null?this.url:getUrl(requestData.getUrlType());
            }

            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, getHttpEntity(requestData.getData()), String.class,requestData.getRemoteMethod());
            responseData.setSuccess(true);
            responseData.setData(responseEntity.getBody());
            log.info("请求成功，返回值：{}",responseData.getData());
        } catch (Exception e){
            log.error("请求YS平台异常:",e);
            responseData.setSuccess(false);
            responseData.setData(e.getMessage());
        }
        return responseData;
    }


    private HttpEntity<String> getHttpEntity(String data){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
        return formEntity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        url = ApplicationUtils.getPropertyValue("msg.address", "");
        urlBase = ApplicationUtils.getPropertyValue("msg.address.base", "");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(30000);
        requestFactory.setReadTimeout(30000);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    private String getUrl(EnumUrlType urlType){
        if(urlType == null){
            return url;
        }
        if(urlType == EnumUrlType.BASE){
            return urlBase;
        }
        return url;
    }
}
