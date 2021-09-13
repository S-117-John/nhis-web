package com.zebone.nhis.common.support;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class BeanUtils {
	public static boolean isNotNull(Object obj){
		if(obj==null){
			return false;
		}
	    try {
	        for (Field f : obj.getClass().getDeclaredFields()) {
	            f.setAccessible(true);
	            if (f.get(obj) != null) {
	                return true;
	            }
	        }
	    }catch (IllegalAccessException e){

	    }
	    return false;
	}

	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
				emptyNames.add(pd.getName());
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	/**
	 * 复制属性，忽略空NULL
	 * @param source
	 * @param target
	 */
	public static void copyPropertiesIgnoreNull(Object source, Object target) {
		org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
	}
}
