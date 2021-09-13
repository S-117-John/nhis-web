package com.zebone.nhis.pro.zsba.death.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pro.zsba.common.support.MD5Util;
import com.zebone.nhis.pro.zsba.death.dao.ZsbaDeathCertificateMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;


/**
 * @Classname ZsbaDeathCertificateService
 * @Description 死亡接口服务
 * @Date 2020-08-13 16:35
 * @Created by wuqiang
 */
@Service
public class ZsbaDeathCertificateService {
    @Autowired
    private ZsbaDeathCertificateMapper zsbaDeathCertificateMapper;

    /**
     * @return java.lang.String
     * @Description
     * @auther wuqiang
     * @Date 2020-08-14
     * @Param [param, user]
     */
    public Map<String, Object> getSign(String param, IUser user) {
        Map<String, Object> map = JsonUtil.readValue(param, Map.class);
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        String url = String.valueOf(map.get("url"));
        map.remove("url");
        User user1 = (User) user;
        //版本1
        String apiVersion = ApplicationUtils.getPropertyValue("apiVersion", "");
        String appId = ApplicationUtils.getPropertyValue("appId", "");
        String secretKey = ApplicationUtils.getPropertyValue("secretKey", "");
        boolean notNull = StringUtils.isNotBlank(apiVersion)
                && StringUtils.isNotBlank(appId)
                && StringUtils.isNotBlank(secretKey);
        if (!notNull) {
            throw new BusException("请检查death.properties 配置项,版本号，id，key不允许为空");
        }
        map.put("apiVersion", apiVersion);
        map.put("appId", appId);
        String signatureMethod = "HmacSHA1";
        map.put("signatureMethod", signatureMethod);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
        //随机数
        map.put("signatureNonce", rannum);
        //时间戳
        map.put("requestTimestamp", System.currentTimeMillis());
        //唯一id
        map.put("requestId", System.currentTimeMillis() + user1.getCodeEmp());
        String content = buildContent(url, getSortParamMap(map));
        String signature = "";
        try {
            Mac hmacSha = Mac.getInstance(signatureMethod);
            byte[] keyBytes = secretKey.getBytes("UTF-8");
            hmacSha.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, signatureMethod));
            signature = new String(Base64.encodeBase64(hmacSha.doFinal(content.getBytes("UTF-8"))), "UTF-8");
        } catch (Exception e) {
            throw new BusException("死亡证明接口签名错误");
        }
        map.put("signature", signature);
        return map;
    }


    /**
     * 参数排序
     */
    private Map<String, Object> getSortParamMap(Map<String, Object> requstData) {
        Map<String, Object> sortParamMap = new TreeMap<String, Object>();
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(requstData.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        });
        for (Map.Entry<String, Object> item : infoIds) {
            if (item.getKey() != null || item.getKey() != "") {
                String key = item.getKey();
                Object val = item.getValue();
                if (!(val == "" || val == null)) {
                    if ("methodRequestData".equals(item.getKey())) {
                        try {
                            val = writeJsonFromObject(val);
                        } catch (JsonProcessingException e) {
                            throw new BusException("业务参数生成JSOn报错，请联系管理员");
                        }
                    }
                    sortParamMap.put(key, val);
                }
            }
        }
        return sortParamMap;
    }

    private String writeJsonFromObject(Object value) throws JsonProcessingException {
        ObjectMapper instance = new ObjectMapper();
        instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return instance.writeValueAsString(value);
    }

    /**
     * 构建待签名
     *
     * @param url
     * @param sortParamMap
     * @return 待签名
     */
    private String buildContent(String url, Map<String, Object> sortParamMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        StringBuilder sbParam = new StringBuilder();
        for (Map.Entry<String, Object> item : sortParamMap.entrySet()) {
            sbParam.append(item.getKey());
            sbParam.append("=");
            sbParam.append(item.getValue().toString());
            sbParam.append("&");
        }
        if (sbParam.length() > 0) {
            sb.append("?");
            sb.append(sbParam.deleteCharAt(sbParam.length() - 1));
        }
        return sb.toString();
    }

    /***
     * @Description 查询死亡患者登记信息
     * @auther wuqiang
     * @Date 2020-08-18
     * @Param [param, user]
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public Map<String, Object> getPiInfomation(String param, IUser user) {
        String pkPi = JsonUtil.getFieldValue(param, "pkPi");
        Map<String, Object> map = zsbaDeathCertificateMapper.getPiInformation(pkPi);
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        if (map.get("birthDay") != null) {
            String age = ApplicationUtils.getAgeFormat((Date) map.get("birthDay"), new Date());
            boolean conS = StringUtils.contains(age, "岁");
            //岁以下
            boolean conY = !conS && StringUtils.endsWith(age, "月");
            boolean conT = !conS && StringUtils.endsWith(age, "天");
            boolean conH = !conS && StringUtils.endsWith(age, "小时");
            if (conS) {
                map.put("nl", StringUtils.substringBefore(age, "岁"));
                map.put("nldw", "1");
            }
            if (conY) {
                map.put("nl", StringUtils.substringBefore(age, "月"));
                map.put("nldw", "2");
            }
            if (conT) {
                map.put("nl", StringUtils.substringBefore(age, "天"));
                map.put("nldw", "3");
            }
            if (conH) {
                map.put("nl", StringUtils.substringBefore(age, "小时"));
                map.put("nldw", "4");
            }
            map.put("birthDay", DateUtils.dateToStr("yyyy-MM-dd", (Date) map.get("birthDay")));
        }
        return map;
    }

    /**
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Description 查询角色下用户信息
     * @auther wuqiang
     * @Date 2020-08-19
     * @Param [param, user]
     */
    public List<Map<String, Object>> getUserByPkRole(String param, IUser user) {
        String pkRole = JsonUtil.getFieldValue(param, "pkRole");
        return zsbaDeathCertificateMapper.GetUserByPkRole(pkRole);
    }

    /**
     * @Description 获取电子健康码签名
     * @auther wuqiang
     * @Date 2021-03-26
     * @Param [param, user]
     */
    public Map<String, Object> getHealthMD5Sign(String param, IUser user) {
        String pkRole = JsonUtil.getFieldValue(param, "sign");
        Map<String, Object> map = new HashMap<>(1);
        map.put("sign", MD5Util.getMD5Code(pkRole));
        return map;
    }

    /**
     * @Description 获取电子健康码二维码
     * @auther wuqiang
     * @Date 2021-03-26
     * @Param [param, user]
     */
    public Map<String, Object> getHealthQr(String param, IUser user) {
        Map paramMap = JsonUtil.readValue(param, Map.class);
        String ehealthCardId = MapUtils.getString(paramMap, "ehealthCardId");
        if (StringUtils.isWhitespace(ehealthCardId)) {
            throw new BusException("获取患者电子健康二维码：ehealthCardId 参数为空");
        }
        return (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC1026", new Object[]{paramMap});
    }
}


