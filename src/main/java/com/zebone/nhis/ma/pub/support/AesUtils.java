package com.zebone.nhis.ma.pub.support;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 商保加密解密Util
 */
public class AesUtils {

    //加密
    public static String encryptByAes(String encryptKey, String encryptString) {
        if (encryptKey == null) {
            //Assert.assertNotNull("encryptKey为空");
            return null;
        }
        // 判断Key是否为16位
        if (encryptKey.length() != 16) {
            //throw new ValidationException("encryptKey长度不是16位");
        }
        try {
            byte[] raw = encryptKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(encryptString.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            //throw UtilException.unchecked(e);
        }
        return null;
    }


    //解密
    public static String decryptByAes(String decryptKey, String decryptString) {
        // 判断Key是否正确
        if (decryptKey == null) {
            //Assert.assertNotNull("decryptKey为空");
            return null;
        }

        // 判断Key是否为16位
        if (decryptKey.length() != 16) {
            //throw new ValidationException("decryptKey长度不是16位");
        }
        try {
            byte[] raw = decryptKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.getDecoder().decode(decryptString);//先用base64解密

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception e) {
            //throw UtilException.unchecked(e);
        }
        return null;
    }


    public static void main(String[] args) {
        //秘钥
        String encryptKey = "d39525819968e65m";

        //加密方法测试
        String data = "{\n" +
                "\t\"package\": {\n" +
                "\t\t\"additionInfo\": {\n" +
                "\t\t\t\"errorCode\": \"1\",\n" +
                "\t\t\t\"errorMsg\": \"success\",\n" +
                "\t\t\t\"receiverTradeNum\": \"132\"\n" +
                "\t\t},\n" +
                "\t\t\"body\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"credentialType\":\"01\",\n" +
                "                \"credentialNum\":\"430102199105089494\",\n" +
                "                \"name\":\"孙湘雅\",\n" +
                "                \"gender\":\"1\",\n" +
                "                \"birthday\":\"19920918\",\n" +
                "                \"commercialBillNum\":\"\",\n" +
                "                \"treatType\":\"2\",\n" +
                "                \"queryDate\":\"20210222140700\",\n" +
                "                \"professionalCode\":\"200000042\",\n" +
                "                \"professionalName\":\"泰康\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"head\": {\n" +
                "\t\t\t\"businessID\": \"S200\",\n" +
                "\t\t\t\"sendTradeNum\": \"20180925140036-EGIS_CLAIM-79867\",\n" +
                "\t\t\t\"senderCode\": \"400000001\",\n" +
                "\t\t\t\"senderName\": \"北京肿瘤医院\",\n" +
                "\t\t\t\"channelCode\": \"123\",\n" +
                "\t\t\t\"channelName\": \"西城卫计委\",\n" +
                "\t\t\t\"receiverCode\": \"200000042\",\n" +
                "\t\t\t\"receiverName\": \"泰康保险股份有限公司\",\n" +
                "\t\t\t\"intermediaryCode\": \"4\",\n" +
                "\t\t\t\"intermediaryName\": \"自费\",\n" +
                "\t\t\t\"hosorgNum\": \"操作员编码\",\n" +
                "\t\t\t\"hosorgName\": \"操作员姓名\",\n" +
                "\t\t\t\"systemType\": \"1\",\n" +
                "\t\t\t\"businessType\": \"2\",\n" +
                "\t\t\t\"standardVersionCode\": \"version:4.0.0\",\n" +
                "\t\t\t\"clientmacAddress\": \"客户端地址\",\n" +
                "\t\t\t\"recordCount\": \"\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";

        if (data.startsWith("{") && data.endsWith("}")) {
            System.out.println("-----------------------------");
        }
        String s = AesUtils.encryptByAes(encryptKey, data);
        System.out.println("加密之后：" + s);

        //解密方法测试
        String result = AesUtils.decryptByAes(encryptKey, s);
        System.out.println("解密之后：" + result);

    }

}
