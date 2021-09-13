package com.zebone.nhis.pro.zsba.common.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
	
	/**
	 * 获取指定两个日期之间的所有日期
	 * (包含指定的开始、结束日期)
	 * @param start		开始日期
	 * @param end		结束日期
	 * @param pattern	格式
	 * @return
	 */
	public static List<String> getBetweenDate(String start, String end, String pattern) {
		List<String> dates = new ArrayList<String>();
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern);

			Calendar calendar = Calendar.getInstance();
			Date endDate=new Date();
			if(compare(start, end, pattern)<0){
				calendar.setTime(df.parse(start));
				endDate = df.parse(end);
			}else{
				calendar.setTime(df.parse(end));
				endDate = df.parse(start);
			}
			Date tmpDate = calendar.getTime();
			long endTime = endDate.getTime();
			while (tmpDate.before(endDate) || tmpDate.getTime() == endTime) {
				dates.add(df.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				tmpDate = calendar.getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dates;
	}
	
	/**
	 * s1对比s2 两个日期的大小
	 * @param s1		日期 字符串
	 * @param s2		日期 字符串
	 * @param pattern 	格式
	 * @return 返回 负数s1小 ，返回0两数相等，返回正整数s1大
	 */
	public static int compare(String s1, String s2, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(df.parse(s1));
			c2.setTime(df.parse(s2));
		} catch (Exception e) {
			System.err.println("格式不正确");
		}
		return c1.compareTo(c2);
	}

}
