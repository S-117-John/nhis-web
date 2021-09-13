package com.zebone.nhis.common.pay.support.weixinPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.foxinmy.weixin4j.model.WeixinPayAccount;
import com.foxinmy.weixin4j.payment.WeixinPayProxy;
import com.foxinmy.weixin4j.sign.WeixinPaymentSignature;
import com.foxinmy.weixin4j.sign.WeixinSignature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class WeixinConfig {
	
	private static String appid;
	
	private static String appsecret;
	
	private static String mchId;
	
	private static String partnerId;
	
	private static String paySignKey;
	
	private static String serverUrl;
	
	private static WeixinPayProxy wxpayClient;
	
	private static WeixinSignature weixinSignature;
	
	public static WeixinPayProxy wxPayClient(){
	    if (wxpayClient == null) {
	        Properties props = null;
            try {
                props = PropertiesLoaderUtils
                        .loadProperties(new EncodedResource(new ClassPathResource("config/pay.properties"), "UTF-8"));
            } catch (IOException e) {
            }
	        
	        //String serverUrl = ApplicationUtils.getPropertyValue("weixin.serverUrl", "");
	        //String alipayAccount = ApplicationUtils.getPropertyValue("weixin.account", "");
            String serverUrl = props.getProperty("weixin.serverUrl");
            String alipayAccount = props.getProperty("weixin.account");
	        JSONObject jo = JSON.parseObject(alipayAccount);
	        appid = jo.getString("id");
	        appsecret = jo.getString("secret");
	        mchId = jo.getString("mchId");
	        paySignKey = jo.getString("paySignKey");
	        partnerId = jo.getString("partnerId");
	        String certificateFile = jo.getString("certificateFile");
	        WeixinPayAccount weixinPayAccount = new WeixinPayAccount(appid, paySignKey, mchId);
	        weixinPayAccount.setCertificateFile(certificateFile);
	        weixinPayAccount.setPartnerId(partnerId);
	        weixinSignature = new WeixinPaymentSignature(paySignKey);
	        if (StringUtils.isNotBlank(serverUrl)){
	            wxpayClient = new WeixinPayProxy(weixinPayAccount);
	        } else {
	            wxpayClient = new WeixinPayProxy(weixinPayAccount);
	        }
	    }
	    return wxpayClient;
	}

    public static String getAppid() {
        return appid;
    }

    public static String getAppsecret() {
        return appsecret;
    }

    public static String getMchId() {
        return mchId;
    }

    public static String getPartnerId() {
        return partnerId;
    }

    public static String getPaySignKey() {
        return paySignKey;
    }

    public static WeixinSignature getWeixinSignature() {
        return weixinSignature;
    }

    public static String getServerUrl() {
        return serverUrl;
    }
	

}
