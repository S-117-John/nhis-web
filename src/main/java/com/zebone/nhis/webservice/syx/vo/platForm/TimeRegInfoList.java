package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeRegInfoList {
	
	/**
	 * 出诊日期
	 */
	@XmlElement(name = "regDate")
	private String regDate;
	/**
	 * 出诊日期对应的星期
	 */
	@XmlElement(name = "regWeekDay")
	private String regWeekDay;
	/**
	 * 上午、下午、晚上的号源信息集合
	 */
	
	@XmlElement(name="timeRegInfo")
	private List<TimeRegInfo> timeRegInfo;
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getRegWeekDay() {
		return regWeekDay;
	}
	public void setRegWeekDay(String regWeekDay) {
		this.regWeekDay = regWeekDay;
	}
	public List<TimeRegInfo> getTimeRegInfo() {
		return timeRegInfo;
	}
	public void setTimeRegInfo(List<TimeRegInfo> timeRegInfo) {
		this.timeRegInfo = timeRegInfo;
	}
	
	
	

}
