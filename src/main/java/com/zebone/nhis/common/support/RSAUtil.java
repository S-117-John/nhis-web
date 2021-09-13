package com.zebone.nhis.common.support;

import org.apache.mina.util.Base64;

import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 读取P12文件获取公钥密钥
 * 进行签名、验签
 */
public class RSAUtil {

    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String PLAIN_TEXT = "test string";
    private static final String ENCODING = "UTF-8";
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 读取P12格式证书的私钥.
     * @param keyStorePath
     *           文件地址
     * @param password
     *          密码
     * @return  privateKey
     *           私钥
     * @throws Exception
     */
    public static PrivateKey ReadP12Private(String keyStorePath, String password) throws Exception {
        PrivateKey privateKey = null;
        // 实例化密钥库，默认JKS类型
        KeyStore ks = KeyStore.getInstance("PKCS12");
        //获得密钥库文件流
        FileInputStream is = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        //私钥
        Enumeration aliases = ks.aliases();
        if (aliases.hasMoreElements()){
            String keyAlias = (String)aliases.nextElement();
             privateKey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
        }
        return privateKey;
    }

    /**
     * 读取P12格式证书的公钥.
     * @param keyStorePath
     *           文件地址
     * @param password
     *          密码
     * @return  pubkey
     *           公钥
     * @throws Exception
     */
    public static PublicKey ReadP12Public(String keyStorePath, String password) throws Exception {
        PublicKey pubkey  = null;
        // 实例化密钥库，默认JKS类型
        KeyStore ks = KeyStore.getInstance("PKCS12");
        //获得密钥库文件流
        FileInputStream is = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        //私钥
        Enumeration aliases = ks.aliases();
        if (aliases.hasMoreElements()){
            String keyAlias = (String)aliases.nextElement();
            Certificate cert = ks.getCertificate(keyAlias);
            pubkey = cert.getPublicKey();
        }
        return pubkey;
    }

    /**
     * 签名
     *
     * @param privateKey
     *            私钥
     * @param plain_text
     *            明文
     * @param signatueAlgoritHm
     *           签名算法
     * @return
     */
    public static byte[] sign(PrivateKey privateKey, String plain_text, String signatueAlgoritHm) throws Exception {
        byte[] signed = null;
            Signature Sign = Signature.getInstance(signatueAlgoritHm);
            Sign.initSign(privateKey);
            Sign.update(plain_text.getBytes());
            signed = Sign.sign();
        return signed;
    }

    /**
     * 签名
     *
     * @param privateKey
     *            私钥
     * @param data
     *            明文
     * @return
     */
    public static byte[] signUtf8(PrivateKey privateKey, String data) throws Exception {
        byte[] signed = null;
        Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
        Sign.initSign(privateKey);
        Sign.update(data.getBytes(ENCODING));
        signed = Sign.sign();
        return signed;
    }

    /**
     * 签名转字符串
     * @param privateKey 密钥
     * @param data  加密内容
     * @return
     */
    public static String signToString(PrivateKey privateKey, String data) throws Exception {
        return bytesToHexString(sign(privateKey,data,SIGNATURE_ALGORITHM));
    }

    /**
     * 签名转Base64字符串
     * @param privateKey 密钥
     * @param data  加密内容
     * @return
     */
    public static String signUtfToBase64(PrivateKey privateKey, String data) throws Exception {
        byte[] signed = RSAUtil.signUtf8(privateKey,data);
        return new String(Base64.encodeBase64(signed));

    }

    /**
     * 验签
     *
     * @param publicKey
     *            公钥
     * @param plain_text
     *            明文
     * @param signed
     *            签名
     *@return  true/false
     */
    public static boolean verifySign(PublicKey publicKey, String plain_text, byte[] signed) {
        boolean SignedSuccess=false;
        try {
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(plain_text.getBytes());
            SignedSuccess = verifySign.verify(signed);
            System.out.println("验证成功？---" + SignedSuccess);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return SignedSuccess;
    }

    /**
     * 验签
     *
     * @param publicKey
     *            公钥
     * @param plain_text
     *            明文
     * @param signed
     *            签名
     * @param signatueAlgoritHm
     *          签名算法
     *@return  true/false
     */
    public static boolean verifySign(PublicKey publicKey, String plain_text, byte[] signed, String signatueAlgoritHm) {
        boolean SignedSuccess=false;
        try {
            Signature verifySign = Signature.getInstance(signatueAlgoritHm);
            verifySign.initVerify(publicKey);
            verifySign.update(plain_text.getBytes());
            SignedSuccess = verifySign.verify(signed);
            System.out.println("验证成功？---" + SignedSuccess);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return SignedSuccess;
    }

    /**
     * bytes[]换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getSignCheckContentV2(Map<String, String> params) {
        if (params == null) {
            return null;
        }

        params.remove("sign");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = (String)keys.get(i);
            String value = (String)params.get(key);
            content.append(new StringBuilder().append(i == 0 ? "" : "&").append(key).append("=").append(value).toString());
        }

        return content.toString();
    }
    /**
     * 解码PublicKey
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.decodeBase64(key.getBytes());
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解码PrivateKey
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.decodeBase64(key.getBytes());
            //byte[] byteKey = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
