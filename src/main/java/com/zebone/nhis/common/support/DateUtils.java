package com.zebone.nhis.common.support;

import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 默认时间格式
	 */
	private static final String defaultDateFormat = "yyyyMMddHHmmss";
	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();

	public static DateFormat getDefaultDateFormat() {
		DateFormat df = threadLocal.get();
		if (df == null) {
			df = new SimpleDateFormat(defaultDateFormat);
			threadLocal.set(df);
		}
		return df;
	}

	// public static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 判断是否同年
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameYear(Calendar date, Calendar date1) {

		return date.get(Calendar.YEAR) == date1.get(Calendar.YEAR);
	}

	/**
	 * 判断是否同年
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameYear(Date date, Date date1) {

		Calendar cdate = Calendar.getInstance();
		cdate.setTime(date);

		Calendar cdate1 = Calendar.getInstance();
		cdate1.setTime(date1);

		return cdate.get(Calendar.YEAR) == cdate1.get(Calendar.YEAR);
	}

	/**
	 * 判断是否同月
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameMonth(Calendar date, Calendar date1) {

		if (isSameYear(date, date1)) {
			return date.get(Calendar.MONTH) == date1.get(Calendar.MONTH);
		} else {
			return false;
		}
	}

	/**
	 * 判断是否同月
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameMonth(Date date, Date date1) {

		Calendar cdate = Calendar.getInstance();
		cdate.setTime(date);

		Calendar cdate1 = Calendar.getInstance();
		cdate1.setTime(date1);

		if (isSameYear(cdate, cdate1)) {
			return cdate.get(Calendar.MONTH) == cdate1.get(Calendar.MONTH);
		} else {
			return false;
		}
	}

	/**
	 * 判断是否同日
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameDay(Calendar date, Calendar date1) {

		if (isSameMonth(date, date1)) {
			return date.get(Calendar.DAY_OF_MONTH) == date1
					.get(Calendar.DAY_OF_MONTH);
		} else {
			return false;
		}
	}

	/**
	 * 判断是否同日
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static boolean isSameDay(Date date, Date date1) {

		Calendar cdate = Calendar.getInstance();
		cdate.setTime(date);

		Calendar cdate1 = Calendar.getInstance();
		cdate1.setTime(date1);

		if (isSameMonth(cdate, cdate1)) {
			return cdate.get(Calendar.DAY_OF_MONTH) == cdate1
					.get(Calendar.DAY_OF_MONTH);
		} else {
			return false;
		}
	}

	/**
	 * @Description 通过数据库函数获取患者年龄
	 * @auther wuqiang
	 * @Date 2020-06-30
	 * @Param [birthday, dateNow]
	 * @return java.lang.String
	 */
	public static String getAgeByBirthday(Date birthday,Date dateNow) {
		dateNow=dateNow==null?new Date():dateNow;
		return 	ApplicationUtils.getAgeFormat(birthday,dateNow);
	}
/**
 * 根据日期计算年龄
 * @param birthDay
 * @return
 * @throws Exception
 */
	public static  int getAge(Date birthDay) throws Exception {
	        Calendar cal = Calendar.getInstance();  
	        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
	            throw new IllegalArgumentException(
	                    "The birthDay is before Now.It's unbelievable!");
	        }
	        int yearNow = cal.get(Calendar.YEAR);  //当前年份
	        int monthNow = cal.get(Calendar.MONTH);  //当前月份
	        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
	        cal.setTime(birthDay);  
	        int yearBirth = cal.get(Calendar.YEAR);
	        int monthBirth = cal.get(Calendar.MONTH);
	        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);   
	        int age = yearNow - yearBirth;   //计算整岁数
	        if (monthNow <= monthBirth) {
	            if (monthNow == monthBirth) {
	                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
	            }else{
	                age--;//当前月份在生日之前，年龄减一 
	            } 
	        } 
	        return age; 
	} 


	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return 相差天数
	 * @throws
	 */
	public static int getDateSpace(Date beginDate, Date endDate) {

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(beginDate);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDate);

		// 设置时间为0
		calBegin.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calBegin.set(java.util.Calendar.MINUTE, 0);
		calBegin.set(java.util.Calendar.SECOND, 0);

		calEnd.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calEnd.set(java.util.Calendar.MINUTE, 0);
		calEnd.set(java.util.Calendar.SECOND, 0);

		int days = (int) (calEnd.getTime().getTime() / 1000 / 3600 / 24 - calBegin
				.getTime().getTime() / 1000 / 3600 / 24);
		return days;
	}
	
	
	/**
	 * 计算住院天数，超过当前时间的小时数则 + 1
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return 相差天数
	 * @throws
	 */
	public static int getDateSpaceByIn(Date beginDate, Date endDate) {

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(beginDate);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(endDate);

		// 设置时间为0
		calBegin.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calBegin.set(java.util.Calendar.MINUTE, 0);
		calBegin.set(java.util.Calendar.SECOND, 0);

		calEnd.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calEnd.set(java.util.Calendar.MINUTE, 0);
		calEnd.set(java.util.Calendar.SECOND, 0);

		int days = (int) (calEnd.getTime().getTime() / 1000 / 3600 / 24 - calBegin
				.getTime().getTime() / 1000 / 3600 / 24);
		if(calEnd.getTime().getTime() / 1000 / 3600 / 24 - calBegin
				.getTime().getTime() / 1000 / 3600 / 24 - days > 0)
			days = days + 1;
		return days;
	}

	/**
	 * 得到当年的第一天。
	 * 
	 * @return
	 */
	public static Date getFDayInCurrentYear() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 0, 1);
		return calendar.getTime();
	}

	/**
	 * 得到当天。
	 * 
	 * @return
	 */
	public static Date getCDayInCurrentYear() {

		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}

	/**
	 * 得到当天的星期。 type : 1:yyyy-MM-dd 2:yyyy-MM-dd hh24: mi: ss
	 * 
	 * @return
	 */
	public static String getDayofWeek(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String weekday = "";
		int week_day = calendar.get(Calendar.DAY_OF_WEEK);
		if (week_day == 1) {
			weekday = "星期天";
		} else if (week_day == 2) {
			weekday = "星期一";
		} else if (week_day == 3) {
			weekday = "星期二";
		} else if (week_day == 4) {
			weekday = "星期三";
		} else if (week_day == 5) {
			weekday = "星期四";
		} else if (week_day == 6) {
			weekday = "星期五";
		} else if (week_day == 7) {
			weekday = "星期六";
		}
		return weekday;

	}

	/**
	 * 得到当天的星期数。 type : 1:yyyy-MM-dd 2:yyyy-MM-dd hh24: mi: ss
	 * 
	 * @return
	 */
	public static int getDayNumOfWeek(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekday = 0;
		int week_day = calendar.get(Calendar.DAY_OF_WEEK);
		if (week_day == 1) {
			weekday = 7;
		} else if (week_day == 2) {
			weekday = 1;
		} else if (week_day == 3) {
			weekday = 2;
		} else if (week_day == 4) {
			weekday = 3;
		} else if (week_day == 5) {
			weekday = 4;
		} else if (week_day == 6) {
			weekday = 5;
		} else if (week_day == 7) {
			weekday = 6;
		}
		return weekday;

	}

	/**
	 * 得到当年的最后一天。
	 * 
	 * @return
	 */
	public static Date getEDayInCurrentYear() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), 11, 31);
		return calendar.getTime();
	}

	/**
	 * 得到下一年。
	 * 
	 * @return
	 */
	public static String getNextYear() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 得到当前年。
	 * 
	 * @return
	 */
	public static String getCurrYear() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 得到当月。
	 * 
	 * @return
	 */
	public static String getCurrMonth() {

		DateFormat dateFormat = new SimpleDateFormat("MM");
		Calendar calendar = Calendar.getInstance();
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 得到上一年。
	 * 
	 * @return
	 */
	public static String getHistoryYear() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		return dateFormat.format(calendar.getTime());
	}
	
	/**
	 * 得到某一年的当前时刻。
	 * @param num 1 为明年  -1为去年
	 * @return
	 */
	public static Date getTimeForOneYear(int num) {

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, num);
		return calendar.getTime();
	}

	/**
	 * 判断是否为闰年
	 * 
	 * @param year
	 * @return
	 */
	public boolean isLeap(int year) {

		if ((year % 4 == 0) && (year % 100 != 0)) {
			return true;
		} else if (year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取指定时间的前几天 或者后几天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getSpecifiedDay(Date date, int days) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取下一天
	 * 
	 * @return
	 */
	public static Date getNextDay() {

		return getSpecifiedDay(new Date(), 1);
	}

	/**
	 * 获取上一天
	 * 
	 * @return
	 */
	public static Date getPreDay() {

		return getSpecifiedDay(new Date(), -1);
	}

	/**
	 * 获取某天的前/后几天
	 * 
	 * @param date
	 * @return yyyyMMdd格式日期字符串
	 */
	public static String getSpecifiedDateStr(Date date, int days) {

		Date newDate = getSpecifiedDay(date, days);
		return getDefaultDateFormat().format(newDate).substring(0, 8);
	}

	/**
	 * 获取某天的前/后几天
	 *
	 * @param date
	 * @return yyyy-MM-dd格式日期字符串
	 */
	public static String getSpecifiedDateStr2(Date date, int days) {

		Date newDate = getSpecifiedDay(date, days);
		return formatDate(newDate, "yyyy-MM-dd HH:mm:ss").substring(0, 10);
	}

	/**
	 * 取日期时间的日期部分，返回yyyyMMdd格式日期字符串
	 * 
	 * @param date
	 * @return yyyyMMdd格式日期字符串
	 */
	public static String getDateStr(Date date) {

		return getDefaultDateFormat().format(date).substring(0, 8);
	}
	/**
	 * 取日期时间，返回yyyyMMddHHmmss格式日期字符串
	 * 
	 * @param date
	 * @return yyyyMMddHHmmss格式日期字符串
	 */
	public static String getDateTimeStr(Date date){
		return getDefaultDateFormat().format(date);
	}

	/**
	 * 取时间部分字符串
	 * 
	 * @param date
	 * @return HHmmss格式的时间字符串
	 */
	public static String getTimeStr(Date date) {

		return getDefaultDateFormat().format(date).substring(8, 14);
	}

	/**
	 * 取分钟部分
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getMin(Date date) {

		if (date == null)
			return null;
		return CommonUtils.getInteger(getDefaultDateFormat().format(date).substring(
				10, 12));
	}

	/**
	 * 取小时部分
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getHour(Date date) {

		if (date == null)
			return null;
		return CommonUtils.getInteger(getDefaultDateFormat().format(date).substring(
				8, 10));
	}

	/**
	 * 取年部分
	 * 
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date) {

		if (date == null)
			return null;
		return CommonUtils.getInteger(getDefaultDateFormat().format(date).substring(
				0, 4));
	}

	/**
	 * 获取两个日期之间的小时数
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Integer getHoursBetween(Date begin, Date end) {

		long mills = end.getTime() - begin.getTime();
		return Long.valueOf(mills / 3600000).intValue();
	}

	/**
	 * 获取两个日期之间的小时数
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Integer getMinsBetween(Date begin, Date end) {

		long mills = end.getTime() - begin.getTime();
		return Long.valueOf(mills / 60000).intValue();
	}

	/**
	 * 获取两个日期之间的秒数
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Integer getSecondBetween(Date begin, Date end) {

		long mills = end.getTime() - begin.getTime();
		return Long.valueOf(mills / 1000).intValue();
	}

	/**
	 * 将Date时间转成字符串
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String dateToStr(String format, Date date) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

		return simpleDateFormat.format(date);
	}

	/**
	 * 将时间字符串转化长Date
	 * 
	 * @param format
	 * @return
	 */
	public static Date strToDate(String strdate) {

		try {
			return getDefaultDateFormat().parse(strdate);
		} catch (ParseException e) {
			throw new BusException("时间格式不符合：yyyyMMddHHmmssges");
		}
	}

	/**
	 * 将时间字符串转化长Date
	 * 
	 * @param format
	 * @return
	 */
	public static Date strToDate(String strdate, String format) {
		try {
	       if(defaultDateFormat.equals(format)){
	    	   return getDefaultDateFormat().parse(strdate);
	       }else{
	    	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			   return simpleDateFormat.parse(strdate);
	       }
		} catch (ParseException e) {
			throw new BusException("时间格式不符合：" + format);
		}
	}

	/**
	 * 获取时间的凌晨时间
	 * 
	 * @param date
	 * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
	 *       1 返回yyyy-MM-dd 23:59:59日期
	 * @return
	 */
	public static Date getDateMorning(Date date, int flag) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		// 时分秒（毫秒数）
		long millisecond = hour * 60 * 60 * 1000 + minute * 60 * 1000 + second
				* 1000;
		// 凌晨00:00:00
		cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);

		if (flag == 0) {
			return cal.getTime();
		} else if (flag == 1) {
			// 凌晨23:59:59
			cal.setTimeInMillis(cal.getTimeInMillis() + 23 * 60 * 60 * 1000
					+ 59 * 60 * 1000 + 59 * 1000);
		}
		return cal.getTime();
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if(date==null){
			return "";
		}
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 日期增减（正数增加，负数减少）
	 * 
	 * @param time
	 *            编辑的时间
	 * @param num
	 *            增加数量
	 * @param dateType
	 *            1:年,2:月,3:日,4:小时,5:分钟,6:秒
	 * @param pattern
	 *            日期格式
	 * @return
	 */
	public static String addDate(Date dateTime, int num, int dateType, String pattern) {
		switch (dateType) {
		case 1:
			dateType = Calendar.YEAR;
			break;
		case 2:
			dateType = Calendar.MONTH;
			break;
		case 3:
			dateType = Calendar.DATE;
			break;
		case 4:
			dateType = Calendar.HOUR;
			break;
		case 5:
			dateType = Calendar.MINUTE;
			break;
		case 6:
			dateType = Calendar.SECOND;
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar rightNow = Calendar.getInstance();
		Date dt1;
		rightNow.setTime(dateTime);
		rightNow.add(dateType, num);// 日期加月份
		dt1 = rightNow.getTime();
		return sdf.format(dt1);
	}

	/**
	 *
	 * @param dateTime
	 * @param num
	 * @param dateType
	 * @param pattern
	 * @return
	 */
	public static Date addDate(Date dateTime, int num, int dateType) {
		switch (dateType) {
			case 1:
				dateType = Calendar.YEAR;
				break;
			case 2:
				dateType = Calendar.MONTH;
				break;
			case 3:
				dateType = Calendar.DATE;
				break;
			case 4:
				dateType = Calendar.HOUR;
				break;
			case 5:
				dateType = Calendar.MINUTE;
				break;
			case 6:
				dateType = Calendar.SECOND;
				break;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(dateTime);
		date.add(dateType,num);
		return date.getTime();
	}

	public static Date parseDate(String strDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(strDate);
	}

	public static String parseDateStr(Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 转换指定格式的字符串为日期
	 * @param strDate
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String strDate,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(strDate);
	}
	
	/**
	 * 判断时间是否在时间段内
	 * @param nowTime
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
	    Calendar date = Calendar.getInstance();
	    if(nowTime==null){
	    	nowTime=new Date();
	    }
	    date.setTime(nowTime);
	    Calendar begin = Calendar.getInstance();
	    if(beginTime==null){
	    	beginTime=nowTime;
	    }
	    begin.setTime(beginTime);
	    Calendar end = Calendar.getInstance();
	    if(endTime==null){
	    	endTime=nowTime;
	    }
	    end.setTime(endTime);
	    if (date.after(begin) && date.before(end)) {
	        return true;
	    }else if(nowTime.compareTo(beginTime)==0 || nowTime.compareTo(endTime) == 0 ){
	    	return true;
	    }else {
	        return false;
	    }
	}
	
	/**
	 * 获取指定日期(当月、当年)的第一天
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfGiven(String dateStr,String format,int dateType){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			int riNow = 0; 
			switch (dateType) {
			case 1:
				riNow = Calendar.DAY_OF_YEAR;
				dateType = Calendar.YEAR;
				break;
			case 2:
				riNow = Calendar.DAY_OF_MONTH;
				dateType = Calendar.MONTH;
				break;
			}
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(riNow,1);
			calendar.add(dateType, 0);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 获取指定日期(下个月,下一年)的第一天
	 * @param dateStr
	 * @param format
	 * @param dateType(1:年 2:月)
	 * @return
	 */
	public static String getFirstDayOfNext(String dateStr,String format,int dateType){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			int riNow = 0; 
			switch (dateType) {
			case 1:
				riNow = Calendar.DAY_OF_YEAR;
				dateType = Calendar.YEAR;
				break;
			case 2:
				riNow = Calendar.DAY_OF_MONTH;
				dateType = Calendar.MONTH;
				break;
			}
			Date date = sdf.parse(dateStr);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(riNow,1);
			calendar.add(dateType, 1);
			return sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *      日期比较: 仅比较年月日，不比较时分秒, 有异常不处理直接抛出 
	 * @param Date date
	 * @param Date anotherDate
	 * @return 如果date小于anotherDate 返回小于0,  如果date大于anotherDate，返回大于0，如果相等，返回0
	 */
	public static int compareToDay(Date date,Date anotherDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String cDate = sdf.format(date);
		String anDate = sdf.format(anotherDate);
		return Integer.parseInt(cDate)-Integer.parseInt(anDate);
	}

}
