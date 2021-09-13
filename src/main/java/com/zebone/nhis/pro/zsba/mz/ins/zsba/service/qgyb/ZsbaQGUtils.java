package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zebone.platform.modules.exception.BusException;

public class ZsbaQGUtils {
    /*** 获取当前时间戳 */
    public static long getCurrentUnixSeconds() {
        ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.toEpochSecond(zoneOffset);
    }
    
    /**
     * 利用java原生的类实现SHA256加密
     * @param str 加密后的报文
     * @return
     */
 	/*** 使用SHA-256算法进行加密 */
     public static String getSHA256Str(String str) {
         String encodeStr = "";
         try {
             MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
             messageDigest.update(str.getBytes("UTF-8"));
             encodeStr = byte2Hex(messageDigest.digest());
         } catch (NoSuchAlgorithmException var4) {
             var4.printStackTrace();
         } catch (UnsupportedEncodingException var5) {
             var5.printStackTrace();
         }
         return encodeStr;
     }
 	
 	/**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
 	private static String byte2Hex(byte[] bytes) {
         StringBuffer stringBuffer = new StringBuffer();
         String temp = null;
         for (int i = 0; i < bytes.length; ++i) {
             temp = Integer.toHexString(bytes[i] & 255);
             if (temp.length() == 1) {
                 stringBuffer.append("0");
             }
             stringBuffer.append(temp);
         }
         return stringBuffer.toString();
     }
 	
    private static Pattern linePattern = Pattern.compile("_([a-z])");

    private static Pattern humpPattern = Pattern.compile("\\B(\\p{Upper})(\\p{Lower}*)");
	  /**
     * 驼峰转下划线(Map-to-Str)
     *
     * @param str
     * @return
     */
    public static String humpToLine( Map<String, Object> map) {
      //  Map<String, Object> map = JSONObject.parseObject(str);
        Map<String, Object> newMap = Maps.newHashMap();
        Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            StringBuffer sb = new StringBuffer();
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            Matcher matcher = humpPattern.matcher(key);
            while (matcher.find()) {
                matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            }
            matcher.appendTail(sb);
            newMap.put(sb.toString(), entry.getValue());
        }
        return JSONObject.toJSONString(newMap);

    }
	  /**
     * 驼峰转下划线(Map-to-Map)
     * @param str
     * @return
     */
    public static Map<String, Object> humpToMap(Map<String, Object> map) {
        //  Map<String, Object> map = JSONObject.parseObject(str);
          Map<String, Object> newMap = Maps.newHashMap();
          Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
          while (it.hasNext()) {
              StringBuffer sb = new StringBuffer();
              Map.Entry<String, Object> entry = it.next();
              String key = entry.getKey();
              Matcher matcher = humpPattern.matcher(key);
              while (matcher.find()) {
                  matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
              }
              matcher.appendTail(sb);
              newMap.put(sb.toString(), entry.getValue());
          }
          return newMap;
      }

    
    /**
     * @param 驼峰转下划线(Map-to-Map)
     * @return
     */
    public static List<Map<String, Object>> humpToLineListMap(List<Map<String, Object>> list) {
        //List<Map> list = JSONObject.parseArray(str, Map.class);
        List<Map<String, Object>> res = new ArrayList<>();
        list.stream().forEach(p -> {
            Map<String, Object> newMap = Maps.newHashMap();
            Iterator<Map.Entry<String, Object>> it = p.entrySet().iterator();
            while (it.hasNext()) {
                StringBuffer sb = new StringBuffer();
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Matcher matcher = humpPattern.matcher(key);
                while (matcher.find()) {
                    matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                }
                matcher.appendTail(sb);
                newMap.put(sb.toString(), entry.getValue());
            }
            res.add(newMap);
        });
        return res;
    }
    /**
     * @param 驼峰转下划线(Map-to-Str)
     * @return
     */
    public static String humpToLineListStr(List<Map<String, Object>> list) {
        //List<Map> list = JSONObject.parseArray(str, Map.class);
        List<Map<String, Object>> res = new ArrayList<>();
        list.stream().forEach(p -> {
            Map<String, Object> newMap = Maps.newHashMap();
            Iterator<Map.Entry<String, Object>> it = p.entrySet().iterator();
            while (it.hasNext()) {
                StringBuffer sb = new StringBuffer();
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                Matcher matcher = humpPattern.matcher(key);
                while (matcher.find()) {
                    matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
                }
                matcher.appendTail(sb);
                newMap.put(sb.toString(), entry.getValue());
            }
            res.add(newMap);
        });

        return JSONObject.toJSONString(res);
    }
    
	/**
	 * 下划线转驼峰式（Map->to-Map）
	 * @param map
	 * @return	newMap
	 */
	public static Map<String,Object> underlineToCamel(Map<String,Object> map){
		Map<String, Object> newMap = new HashMap<String, Object>();
	      Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
	      while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        String key = entry.getKey();
	        String newKey = underlineToCamel(key);
	        newMap.put(newKey, entry.getValue());
	      }
	    return newMap;
	}
	/**
	 * 下划线转驼峰式（Str-to_Str）
	 * @param map
	 * @return	newMap
	 */
	public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(Character.toLowerCase(param.charAt(i)));
            }
        }
        return sb.toString();
    }
	 /**
     * 15位身份证 转18位
     * @param fifteenIDCard
     */
   public static String getEighteenIDCard(String fifteenIDCard){
       if (fifteenIDCard != null && fifteenIDCard.length() == 15) {
           StringBuilder sb = new StringBuilder();
           sb.append(fifteenIDCard.substring(0, 6)).append("19").append(
                   fifteenIDCard.substring(6));
           sb.append(getVerifyCode(sb.toString()));
           return sb.toString();
       } else {
           throw new BusException("不是15位的身份证");
       }
   }
   /**
    *  获取身份证的校验码
    */
   public static char getVerifyCode(String idCardNumber) {
       if (idCardNumber == null || idCardNumber.length() < 17) {
           throw new BusException("不合法的身份证号码");
       }
       char[] Ai = idCardNumber.toCharArray();
       int[] Wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
       char[] verifyCode = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3',
               '2' };
       int S = 0;
       int Y;
       for (int i = 0; i < Wi.length; i++) {
           S += (Ai[i] - '0') * Wi[i];
       }
       Y = S % 11;
       return verifyCode[Y];
   }
}
