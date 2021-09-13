package com.zebone.nhis.ma.pub.platform.pskq.web;


import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.EventLog;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class HttpRestTemplate extends RestTemplate {

    @EventLog
    public String postForString(String requestBody){
        String httpPostAddr = ApplicationUtils.getPropertyValue("msg.address", "");
        List<HttpMessageConverter<?>> converterList = getMessageConverters();
        converterList.remove(1);    //移除StringHttpMessageConverter
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converterList.add(1, converter);    //convert顺序错误会导致失败
        setMessageConverters(converterList);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(requestBody, headers);
        String body = postForObject(httpPostAddr,formEntity,String.class);
        return body;
    }


    public String postForXml(String requestBody){
        String httpPostAddr = ApplicationUtils.getPropertyValue("msg.sms.send", "");
        List<HttpMessageConverter<?>> converterList = getMessageConverters();
        converterList.remove(1);    //移除StringHttpMessageConverter
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converterList.add(1, converter);    //convert顺序错误会导致失败
        setMessageConverters(converterList);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/xml; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_XML.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(requestBody, headers);
        String body = postForObject(httpPostAddr,formEntity,String.class);
        return body;
    }

    public String postForXml(String requestBody,String url){
        String httpPostAddr = url;
        List<HttpMessageConverter<?>> converterList = getMessageConverters();
        converterList.remove(1);    //移除StringHttpMessageConverter
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converterList.add(1, converter);    //convert顺序错误会导致失败
        setMessageConverters(converterList);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/xml; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_XML.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(requestBody, headers);
        String body = postForObject(httpPostAddr,formEntity,String.class);
        return body;
    }

    /**
     * 通过Http post 方式调用web服务
     * @param param 参数名称
     * @param method 方法名
     * @param message 消息
     * @return
     * @throws UnsupportedEncodingException
     */
    public String postHttp(String param,String url,String method,String message) throws Exception {
        String responseMsg = "<string>1</string>";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("utf-8");
        PostMethod postMethod = new PostMethod(url+"/"+method);
        postMethod.addParameter(param, message);
        postMethod.setRequestHeader("Content-Type", "application/xml; charset=utf-8");
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    /**
     *  HttpClient远程调用webservice的通用方法(POST)
     * @param url url请求地址
     * @param soap  soap请求报文
     * @param contentType   contentType内容类型
     * @return
     */
    public static String httpClient(String url,String soap,String contentType){
        HttpClient httpClient=new HttpClient();
        PostMethod postMethod=new PostMethod(url);
        String result = null;
        try {
            byte[] b=soap.getBytes("utf-8");
            InputStream is = new ByteArrayInputStream(b, 0, b.length);
            RequestEntity re = new InputStreamRequestEntity(is, b.length, contentType);
            postMethod.setRequestEntity(re);
            int statusCode = httpClient.executeMethod(postMethod);
            if (statusCode==200){
                result = postMethod.getResponseBodyAsString();
            }
            postMethod.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
