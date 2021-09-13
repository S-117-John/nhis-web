package com.zebone.nhis.ex.nis.ns.support;

public class OrderTypeUtil {
/**
 * 判断医嘱类型是否是药品
 * @param codeOrdtype
 * @return
 */
	public static boolean isMedicine(String codeOrdtype) {
		if (null == codeOrdtype)
			return false;
		if ("".equals(codeOrdtype))
			return false;
		if (codeOrdtype.length() < 2)
			return false;
		if (!"01".equals(codeOrdtype.substring(0, 2)))
			return false;
		return true;
	}
}
