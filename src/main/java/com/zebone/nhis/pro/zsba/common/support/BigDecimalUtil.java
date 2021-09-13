package com.zebone.nhis.pro.zsba.common.support;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class BigDecimalUtil {
	
	//保留2位小数
	public static Double bigDecimalTo2(Double decimal){
		BigDecimal b = new BigDecimal(decimal);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 四舍五入保留
	 * @param v1  除数
	 * @param v2 被除数
	 * @param scale 保留小数位数
	 * @return
	 */
	public static BigDecimal divi(String v1, String v2, int scale){
		if(StringUtils.isNotEmpty(v1)){
			return new BigDecimal(v1).divide(new BigDecimal(v2), scale, BigDecimal.ROUND_HALF_UP);
		}
		return new BigDecimal(0);
	}
	
	public static void main(String[] args) {
		System.out.println(BigDecimalUtil.bigDecimalTo2(5.3102));
		System.out.println(BigDecimalUtil.bigDecimalTo2(0.01));
	}
}