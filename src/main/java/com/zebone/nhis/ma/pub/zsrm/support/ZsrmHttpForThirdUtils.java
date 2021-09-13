package com.zebone.nhis.ma.pub.zsrm.support;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class ZsrmHttpForThirdUtils{

    /**
     * HTTP xml访问
     * @param method
     * @param param
     * @return
     */
    public  static String postHpptForXml(String method,String param){
        //请求地址
        StringBuffer url = new StringBuffer();
        url.append(ApplicationUtils.getPropertyValue("scm.opdt.herbpack.webservice.url", ""));
        url.append(method);
        RestTemplate Temp = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.parseMediaType("application/xml; charset=UTF-8"));
        requestHeaders.add("Accept", MediaType.APPLICATION_XML_VALUE.toString());
        HttpEntity<String> requestEntity = new HttpEntity<String>(param, requestHeaders);
        String result=null;
        try {
            result = Temp.postForObject(url.toString(), requestEntity, String.class);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }


}