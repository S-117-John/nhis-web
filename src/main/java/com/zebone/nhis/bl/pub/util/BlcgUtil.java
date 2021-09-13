package com.zebone.nhis.bl.pub.util;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.syx.service.CgOpCustomerService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.exception.BusException;

public class BlcgUtil {



	/**
	 * 将字符串boolean类型，转换成java的boolean
	 * @param flag
	 * @return
	 */
	public static boolean converToTrueOrFalse(String flag) {

		if ("1".equals(flag)) {
			return true;
		} else if ("0".equals(flag)) {
			return false;
		} else {
			//throw new BusException("Flag 值错误");
			return false;
		}
	}

	/**
	 * 判断一个值是否在规定的范围
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean rangeInDefined(double current, double min, double max) {

		return Math.max(min, current) == Math.min(current, max);
	}

	/**
	 * 校验map中的value值是否是空（如果为空直接抛异常）
	 * @param map
	 * @return
	 */
	public static void validateEmptyValueMap(Map<String, Object> map) {

		for (Map.Entry<String, Object> entryTemp : map.entrySet())
			if (entryTemp.getValue() == null)
				throw new BusException("Map中key=" + entryTemp.getKey() + "的value为空");
	}

	/**
	 * 比较两个集合元素是否相等
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <T> boolean equalList(List<T> list1, List<T> list2) {

		return (list1.size() == list2.size()) && list1.containsAll(list2);
	}

	public static CgOpCustomerService getCgOpCustomerService(){
		String clas = ApplicationUtils.getPropertyValue("cg.OpCustomerServiceClass","cgOpCustomerService");
		return ServiceLocator.getInstance().getBean(clas, CgOpCustomerService.class);
	}
}
