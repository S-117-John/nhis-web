package com.zebone.nhis.ma.pub.platform.zb.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 操作Map的工具类
 * @author huangben
 *
 */
public class MapUtils {

	/**
	 * 用于对象转换Map<K, V>
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) throws Exception {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}

		return map;
	}
	
	// Bean to map
		public static Object beanToMap(Object obj) {
			String stringBean = JsonUtil.writeValueAsString(obj);
			return JsonUtil.readValue(stringBean, Map.class);
		} 
		
		
		// Bean to map
				@SuppressWarnings("unchecked")
				public static List<Map<String, Object>> lisBToLisMap(List<Object> list) {
					List<Map<String, Object>> listMap = new ArrayList<>();
					for(Object lis:list){
						String stringBean = JsonUtil.writeValueAsString(lis);
						listMap.add((Map<String, Object>)JsonUtil.readValue(stringBean, Map.class));
					}
					
					return listMap;
				} 

}
