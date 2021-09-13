package com.zebone.nhis.pro.zsba.common.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 字符串工具类
 * @author zim
 *
 */
public class StringTools {
	
	protected static Logger logger = LoggerFactory.getLogger(StringTools.class);

	//首字母转小写
	public static String toLowerCaseFirstOne(String str){
	  if(Character.isLowerCase(str.charAt(0)))
	    return str;
	  else
	    return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}
	//首字母转大写
	public static String toUpperCaseFirstOne(String str){
	  if(Character.isUpperCase(str.charAt(0)))
	    return str;
	  else
	    return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
	}
	
	
	static String[] repCh = new String[]{"【","】","｛","｝"};
	static String[] repEn = new String[]{"[","]","{","}"};
	public static String replaceAlls(String str){
		for(int i=0; i< repCh.length; i++){
			str = str.replace(repCh[i], repEn[i]);
		}
		return str;
	}
	
	/**
	 * 解析JSON
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> jsonToMap(String jsonString) throws JSONException {
		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
		Map<String, String> result = new HashMap<String, String>();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}
	
	/**
	 * 向客户端写出内容
	 * @param rep
	 * @param msg
	 * @throws Exception
	 */
	public static void writeResp(HttpServletResponse rep, String msg){
		try {
			rep.setContentType("application/json");
			rep.setCharacterEncoding("utf-8");
			rep.getWriter().write(msg);
			rep.getWriter().flush();
			logger.info("响应数据：{}", msg);
		} catch (Exception e) {
			logger.info("向客户端写出内容时发生异常：" + e.getLocalizedMessage());
		}
	}
	
	
}
