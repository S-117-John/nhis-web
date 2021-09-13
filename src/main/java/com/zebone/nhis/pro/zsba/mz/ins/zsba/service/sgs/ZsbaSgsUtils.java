package com.zebone.nhis.pro.zsba.mz.ins.zsba.service.sgs;

import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ZsbaSgsUtils {
	/**
	 * 生成请求xml数据
	 * @param todoInfo 数据 (key为wsdl文件中参数的name值注意大小写和顺序都要保持一致,value为实际值)
	 * @return
	 */
	public static String makeXml(JSONObject todoInfo) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("<program>\n");
	    for (String key : todoInfo.keySet()) {
	    	Object obj = todoInfo.get(key);

	    	if(obj != null) {
	    		if(obj instanceof JSONArray) {
	    			JSONArray jsona=(JSONArray)obj;
	    			sb.append("<" + key + ">");
	    			
	    			for (Iterator iterator = jsona.iterator(); iterator.hasNext();) {
	    	            JSONObject object = (JSONObject)iterator.next();
	    	            sb.append("<row>");
	    	            for (String key1 : object.keySet()) {
		    	            sb.append("<" + key1 + ">");
		    	            sb.append(object.get(key1).toString().replaceAll("&", "&amp;"));//特殊字符需要转换
		    	            sb.append("</" + key1 + ">");
		    		        sb.append("\r\n");
	    	            }
	    	            sb.append("</row>");
	    	        }
	    			sb.append("</" + key + ">");
	    		} else {
	    			sb.append("<" + key + ">");
	                sb.append(todoInfo.get(key).toString().replaceAll("&", "&amp;"));//特殊字符需要转换
	                sb.append("</" + key + ">");
	    	        sb.append("\r\n");
	    		}
	    	}
	    }
	    sb.append("</program>");
	    return sb.toString();
	}
}
