package com.zebone.nhis.ma.tpi.rhip.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateUtils {
	
	/**
	 * 将Date类转换为XMLGregorianCalendar
	 * @param date
	 * @return 
	 */
	public static XMLGregorianCalendar dateToXmlDate(Date date){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			DatatypeFactory dtf = null;
		     try {
				dtf = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
			}
			XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
		    dateType.setYear(cal.get(Calendar.YEAR));
		    //由于Calendar.MONTH取值范围为0~11,需要加1
		    dateType.setMonth(cal.get(Calendar.MONTH)+1);
		    dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));
		    dateType.setHour(0);
		    dateType.setMinute(0);
		    dateType.setSecond(0);
		    return dateType;
		} 

	/**
	 * 将Date类转换为XMLGregorianCalendar
	 * @param date
	 * @return 
	 */
	public static XMLGregorianCalendar dateToXmlDateTime(Date date){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			DatatypeFactory dtf = null;
		     try {
				dtf = DatatypeFactory.newInstance();
			} catch (DatatypeConfigurationException e) {
			}
			XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
		    dateType.setYear(cal.get(Calendar.YEAR));
		    //由于Calendar.MONTH取值范围为0~11,需要加1
		    dateType.setMonth(cal.get(Calendar.MONTH)+1);
		    dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));
		    dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));
		    dateType.setMinute(cal.get(Calendar.MINUTE));
		    dateType.setSecond(cal.get(Calendar.SECOND));
		    return dateType;
		} 	
	/**
	 * 将XMLGregorianCalendar转换为Date
	 * @param cal
	 * @return 
	 */
	public static Date xmlDate2Date(XMLGregorianCalendar cal){
		return cal.toGregorianCalendar().getTime();
	}
	
	public static String getDateStr(Date date){
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str="";
		str=sdf.format(date);
		
		return str;
	}	
	
	public static String getDateStrD(Date date){
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String str="";
		str=sdf.format(date);
		
		return str;
	}
	
	public static String getDateStrHM(Date date){
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str="";
		str=sdf.format(date);
		
		return str;
	}
	public static String getDateStrHMS(Date date){
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str="";
		str=sdf.format(date);
		
		return str;
	}
	public static String getDateStrHMSD(Date date){
		if(date==null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str="";
		str=sdf.format(date);
		
		return str;
	}
}
