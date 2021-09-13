package com.zebone.nhis.ma.pub.zsba.support;

import com.zebone.nhis.ma.pub.zsba.vo.outflow.SignCheckReq;
import com.zebone.nhis.pro.zsba.common.support.MD5Util;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class PresOutflowTool {

    public static String sign(Map<String, String> headers,SignCheckReq checkRequest) {
        //header传输值拼接，非必填参数，没有填，则不用拼接，如： md5(path+X-Uop-AppId=值&X-Uop-Timestamp=值&X-Uop-SignType=值&X-Uop-Crypto=值 + body值+publicKey)
        StringBuilder builder = new StringBuilder();
        builder.append(checkRequest.getPath());
        headers.forEach((k,v) ->  builder.append(k).append("=").append(v).append("&"));
        //移除最后一个&
        builder.deleteCharAt(builder.length() - 1);
        if (!checkRequest.isMultipart() && StringUtils.isNotBlank(checkRequest.getBody())) {
            builder.append(checkRequest.getBody());
        }
        builder.append(checkRequest.getPublicKey());
        return MD5Util.MD5Encode(builder.toString(), "UTF-8");
    }

    private static Map<String, String> getBaseHeader(String appId){
        Map<String, String> headers = new TreeMap<>();
        headers.put("X-Uop-AppId", appId);
        headers.put("X-Uop-Timestamp", String.valueOf(System.currentTimeMillis()));
        headers.put("X-Uop-SignType", "MD5");
        return headers;
    }

    public static Map<String, String> buildHeader(SignCheckReq signCheckReq){
        Map<String, String> header = getBaseHeader(signCheckReq.getAppId());
        header.put("X-Uop-Sign",PresOutflowTool.sign(header,signCheckReq));
        return header;
    }
}
