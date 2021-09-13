package com.zebone.nhis.pv.reg.vo;

import com.zebone.nhis.common.module.pv.PvQue;
import com.zebone.nhis.common.support.DateUtils;

import java.util.Date;

public class PvQueVO extends PvQue{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 资源类型
	 */
	private String euRestype;

	/**
	 * 工作日期
	 */
	private String dateWork;

	/**
	 * 候诊基数
	 */
	private Integer cntWait;

	/**
	 * 续诊人数
	 */
	private Integer cntConte;

	/**
	 * 序号
	 */
	private Integer num;

	/**
	 * 当开始时间
	 */
	private String dateBegin;

	/**
	 * 结束时间
	 */
	private String dateEnd;

	/**
	 * 过号入队位置
	 */
	private int sortnoOver;

	/**
	 * 是否预约优先
	 */
	private String flagAppt;

	private Date expectTime;

	private String today;
	
	/**
	 * 
	 */
	private int number;

	public String getToday() {
		return DateUtils.getDateTimeStr(new Date());
	}

	public String getFlagAppt() {
		return flagAppt;
	}

	public void setFlagAppt(String flagAppt) {
		this.flagAppt = flagAppt;
	}

	public int getSortnoOver() {
		return sortnoOver;
	}

	public void setSortnoOver(int sortnoOver) {
		this.sortnoOver = sortnoOver;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCntWait() {
		return cntWait;
	}

	public void setCntWait(Integer cntWait) {
		this.cntWait = cntWait;
	}

	public Integer getCntConte() {
		return cntConte;
	}

	public void setCntConte(Integer cntConte) {
		this.cntConte = cntConte;
	}

	public String getDateWork() {
		return dateWork;
	}

	public void setDateWork(String dateWork) {
		this.dateWork = dateWork;
	}

	public String getEuRestype() {
		return euRestype;
	}

	public void setEuRestype(String euRestype) {
		this.euRestype = euRestype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
}
