package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.EmpiId;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Component
public class MyRestTemplateUtil {

    @EmpiId
    public String post(String requestBody) throws IOException {
        String httpPostAddr = ApplicationUtils.getPropertyValue("msg.address", "");
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        converterList.remove(1);    //移除StringHttpMessageConverter
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converterList.add(1, converter);    //convert顺序错误会导致失败

        restTemplate.setMessageConverters(converterList);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(requestBody, headers);
        String body = restTemplate.postForObject(httpPostAddr,formEntity,String.class);
        return body;
    }
}
