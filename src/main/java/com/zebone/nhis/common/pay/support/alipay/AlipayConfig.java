package com.zebone.nhis.common.pay.support.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置置类
 *详细：设置帐户有关信息及返回路径
 */

public class AlipayConfig {

    private static String partner = "";
    private static String appId = "";
    private static String privateKey = "";// 私钥
    private static String signType = ""; //前台配置         //AlipayConstants.SIGN_TYPE_RSA2;
//    private static String format = AlipayConstants.FORMAT_JSON;
//    private static AlipayClient alipayClient;
    private static String serverUrl = "";
    private static String defaultServerUrl = "https://openapi.alipay.com/gateway.do";
    private static String apipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzpkR/slPUpdHMon2yI4J9cock8mXgFLCRB19Eacyi0i7Fpglis8YVg1n3wYBGARWjCOsevhaq6oudx8Vql0AB/nH8Qg/QxtpRPYRAz+yVifrv42BsiThHR2gb/YOIONOcSrLZT80NvoBg4pu3sLJgK2P4ENyGfKlJYx4V2LhZMNShujSRm+koWMp6bFHlK6CaeWgHd5pzYuKdZ2iE7OVcvqfOCvwPCpnpFPMx3p1sIv/gZBiHCtEgoPoyM3/8y0uXAY41y0uooPs7FP8/De2feeJsipv8krE+fbdeBkF0bM8AlW5LFWt6h99QPQA4cxfurPp6mZjetP3NhzZjBiY7QIDAQAB";

    public static String getPartner() {
        return partner;
    }

    public static String getAppId() {
        return appId;
    }

    public static String getApipayPublicKey() {
        return apipayPublicKey;
    }

    public static String getSignType() {
        return signType;
    }
    
    

}
