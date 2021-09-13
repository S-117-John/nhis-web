package com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2013-3-15
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

	public static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm","yyyyMMdd", "HH:mm" };

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
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Object date) {
		return formatDate(parseDate(date), "yyyy-MM-dd HH:mm:ss");
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
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return simpleDateFormat.parse(str.toString());
			} catch (ParseException e1) {
				return null;
			}
		}
	}
	public static Date parseDatePatterns(Object str, String[] patterns) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), patterns);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date getDateStart(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getDateEnd(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	

	/**
	 * 获取当前时间的多少分钟后的时间
	 * 
	 * @param minute
	 *            后多少分钟数
	 * @param pattern
	 *            时间格式
	 * @return
	 */
	public static String getAfterMinute(int minute, String pattern) {
		Date date = new Date();
		int mm = minute * 60000;
		Date after = new Date(date.getTime() + mm); // 多少分钟后的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);// 可以方便地修改日期格式
		return dateFormat.format(after);
	}

	/**
	 * 获取当前时间的多少分钟前的时间
	 * 
	 * @param minute
	 *            前多少分钟数
	 * @param currDate
	 *            当前时间
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String getBeforeMinute(int minute, Date currDate, String pattern) {
		int mm = minute * 60000;
		Date before = new Date(currDate.getTime() - mm); // 多少分钟前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);// 可以方便地修改日期格式
		return dateFormat.format(before);
	}

	/**
	 * s1对比s2 两个日期的大小
	 * 
	 * @param s1
	 *            日期 字符串
	 * @param s2
	 *            日期 字符串
	 * @param pattern
	 *            格式
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

	/**
	 * 获取指定两个日期之间的所有日期
	 * (包含指定的开始、结束日期)
	 * @param start
	 * @param end
	 * @param pattern
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
				// System.out.println(df.format(calendar.getTime()));
				dates.add(df.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				tmpDate = calendar.getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dates;
	}
	
	public static String addDayByNum(String dateTime,String patterns,int day){
		Date date=parseDate(dateTime);
		Calendar calendar = new GregorianCalendar(); 
     	calendar.setTime(date); 
     	calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
     	date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
     	return formatDate(date, patterns);
	}
	
	
	/**
	 * 获取指定两个日期之间的所有月份
	 * 
	 * @param minDate
	 *            开始时间
	 * @param maxDate
	 *            结束时间
	 * @return
	 */
	public static List<String> getMonthBetween(String minDate, String maxDate) {
		List<String> result = new ArrayList<String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月

			Calendar min = Calendar.getInstance();
			Calendar max = Calendar.getInstance();

			min.setTime(sdf.parse(minDate));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

			max.setTime(sdf.parse(maxDate));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

			Calendar curr = min;
			while (curr.before(max)) {
				// System.out.println(sdf.format(curr.getTime()));
				result.add(sdf.format(curr.getTime()));
				curr.add(Calendar.MONTH, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取系统当前日期前一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getBeforeDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取系统当前日期后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 加上/减去相应天数，返回日期字符串
	 * 
	 * @param day
	 *            天数(加：+5，减：-5)
	 * @return
	 */
	public static String getNumDay(Integer day) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DATE, day);// 加上/减去相应天数，返回日期字符串
		str = sdf.format(lastDate.getTime());
		return str;
	}

	/**
	 * 加上/减去相应月份，返回日期字符串
	 * 
	 * @param month
	 *            月份(加：+5，减：-5)
	 * @return
	 */
	public static String getNumMonth(Integer month) {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.MONTH, month);// 加上/减去相应月份，返回日期字符串
		str = sdf.format(lastDate.getTime());
		return str;
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
	
/*	public static Date parseFormatDate(Date date, String pattern) throws ParseException{
		if(StringUtils.isNotEmpty(pattern)){
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dateStr = sdf.format(date);
		return sdf.parse(dateStr);
	}*/
	
	/**
	 * 返回时段如（上午、下午）
	 * @return
	 * @throws ParseException
	 */
	public static String getTimeSlot() throws ParseException{
		SimpleDateFormat sim1 = new SimpleDateFormat("HH:mm:ss");
		Date date1 = sim1.parse(sim1.format(new Date()));
		String timeSlot = "";
		if((date1.before(sim1.parse("12:00:00")))){
			timeSlot="上午";
		}else if((date1.after(sim1.parse("12:00:00")))){
			timeSlot="下午";
		}
		return timeSlot;
	}
	
	
	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
    /**
     * date2比date1多的天数
     * @param date1    
     * @param date2
     * @return    
     */
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
       int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //不同年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            
            return timeDistance + (day2-day1) ;
        }
        else    //同一年
        {
            return day2-day1;
        }
    }
    
    /**
     * 获取传入日期所在月的最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(final Date date) {

        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return cal.getTime();

    }
    
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(parseFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		/*List<String> dates =  getBetweenDate("20180206", "20180226", "yyyyMMdd");
		System.out.println(dates.size());*/
		String date = "2005-07-05 21:26:06.000";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(simpleDateFormat.parse(date));
	}
}
