/**
 * 
 */
package com.zebone.nhis.common.support;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import com.zebone.platform.modules.exception.BusException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 * @ClassName: Sm4Util 
 * @Description: Sm4工具类
 * @author: cxd
 * @date: 2021年3月23日 上午9:47:24  
 */
public class SM4Util {

	static {
        Security.addProvider(new BouncyCastleProvider());
    }
    private static final String ENCODING = "UTF-8";
    public static final String ALGORITHM_NAME = "SM4";
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    // 128-32位16进制；256-64位16进制
//    public static final int DEFAULT_KEY_SIZE = 128;
 
    /**
     * 生成ECB暗号
     * @explain ECB模式（电子密码本模式：Electronic codebook）
     * @param algorithmName 算法名称
     * @param mode 模式  1
     * @param key
     * @return
     * @throws Exception
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

 
    //加密******************************************
    /**
     * @explain 系统产生秘钥
     * @param keySize
     * @return
     * @throws Exception
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密
     * @explain 加密模式：ECB 密文长度不固定，会随着被加密字符串长度的变化而变化
     * @param hexKey 16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encryptEcb(String hexKey, String paramStr){
        return encryptEcb2(hexKey,paramStr,true);
    }

    public static String encryptEcb16(String hexKey, String paramStr){
        return encryptEcb2(hexKey,paramStr,false);
    }

    private static String encryptEcb2(String key, String paramStr,boolean isKey32){
        String cipherText = "";
       try{
           // 16进制字符串-->byte[]
           byte[] keyData = isKey32?fromHexString(key):key.getBytes("UTF-8");
           // String-->byte[]
           byte[] srcData = paramStr.getBytes(ENCODING);
           // 加密后的数组
           byte[] cipherArray = encryptEcbPadding(keyData, srcData);
           // byte[]-->hexString
           cipherText = toHexString(cipherArray);
       } catch (Exception e){
           throw new BusException("SM4加密异常："+e.getMessage());
       }
        return cipherText;
    }
 
    /**
     * 加密模式之Ecb
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptEcbPadding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);//声称Ecb暗号,通过第二个参数判断加密还是解密
        return cipher.doFinal(data);
    }
 
    //解密****************************************
    /**
     * sm4解密
     * @explain 解密模式：采用ECB
     * @param key
     * @param cipherText 16进制的加密字符串（忽略大小写）
     * @param  isKey32 是否32位key
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptEcb(String key, String cipherText,boolean isKey32) throws Exception {
        // 用于接收解密后的字符串
        String decryptStr = "";
        try {
            // hexString-->byte[]
            byte[] keyData = isKey32?fromHexString(key):key.getBytes("UTF-8");
            // hexString-->byte[]
            byte[] cipherData = fromHexString(cipherText);
            // 解密
            byte[] srcData = decryptEcbPadding(keyData, cipherData);
            // byte[]-->String
            decryptStr = new String(srcData, ENCODING);
        } catch (Exception e) {
            throw new BusException("SM4解密异常："+e.getMessage());
        }
        return decryptStr;
    }
 
    /**
     * 解密
     * @explain
     * @param key
     * @param cipherText
     * @return
     * @throws Exception
     */
    public static byte[] decryptEcbPadding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);//生成Ecb暗号,通过第二个参数判断加密还是解密
        return cipher.doFinal(cipherText);
    }
 
    /**
     * 校验加密前后的字符串是否为同一数据
     * @explain
     * @param hexKey 16进制密钥（忽略大小写）
     * @param cipherText 16进制加密后的字符串
     * @param paramStr 加密前的字符串
     * @return 是否为同一数据
     * @throws Exception
     */
    public static boolean verifyEcb(String hexKey, String cipherText, String paramStr) throws Exception {
        // 用于接收校验结果
        boolean flag = false;
        // hexString-->byte[]
        byte[] keyData = fromHexString(hexKey);
        // 将16进制字符串转换成数组
        byte[] cipherData = fromHexString(cipherText);
        // 解密
        byte[] decryptData = decryptEcbPadding(keyData, cipherData);
        // 将原字符串转换成byte[]
        byte[] srcData = paramStr.getBytes(ENCODING);
        // 判断2个数组是否一致
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }

    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1) {
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        }

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }

    public static void main(String[] args) {
        try {
            System.out.println(encryptEcb("c63b07c0009c4860b5a2a8cba0c18745", "你好123"));
            System.out.println(encryptEcb16("5ed70848fa4f5436", "你好123"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] fromHexString(String s) {
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException(s);
        } else {
            byte[] array = new byte[s.length() / 2];

            for(int i = 0; i < array.length; ++i) {
                int b = Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
                array[i] = (byte)(255 & b);
            }

            return array;
        }
    }
}
