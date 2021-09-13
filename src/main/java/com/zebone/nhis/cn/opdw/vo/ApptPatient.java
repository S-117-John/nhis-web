package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

import org.joda.time.DateTime;

import com.zebone.nhis.common.module.pi.PiMaster;

public class ApptPatient extends PiMaster {
	 
	private static final long serialVersionUID = 1L;

	//预约主键
    private String pkSchappt;
    //预约日期
    public Date  dateAppt;
    //午别
    private String nameDateslot;
    //资源
    private String schres;
     //科室
    private String nameDept;
    //票号
    private String ticketNo;
    //开始时间
    private String timeBegin;
    //结束时间
    private String timeEnd;
    
	public String getTimeBegin() {
		return timeBegin;
	}
	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getPkSchappt() {
		return pkSchappt;
	}
	public void setPkSchappt(String pkSchappt) {
		this.pkSchappt = pkSchappt;
	}
	public Date getDateAppt() {
		return dateAppt;
	}
	public void setDateAppt(Date dateAppt) {
		this.dateAppt = dateAppt;
	}
	public String getNameDateslot() {
		return nameDateslot;
	}
	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}
	public String getSchres() {
		return schres;
	}
	public void setSchres(String schres) {
		this.schres = schres;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	@Override
	public String toString() {
		return "ApptPatient [pkSchappt=" + pkSchappt + ", dateAppt=" + dateAppt + ", nameDateslot=" + nameDateslot
				+ ", schres=" + schres + ", nameDept=" + nameDept + ", ticketNo=" + ticketNo + ", timeBegin="
				+ timeBegin + ", timeEnd=" + timeEnd + "]";
	}
	
}
