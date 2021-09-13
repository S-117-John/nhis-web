package com.zebone.nhis.ex.pivas.vo;

import java.util.Date;

/***
 * 检验类
 * 
 * @author wangpeng
 * @date 2016年12月22日
 *
 */
public class CnOrderCheckVo {
	
	/** 计划执行时间 */
	private Date timePlan;
	
	/** 停止时间 */
	private Date dateStop;
	
	/** 作废标志 */
	private String flagErase;
	
	/** 停止标志 */
	private String flagStop;
	
	/** 状态 0生成，1打印，2入舱，3配置，4出舱，5打包，8复核，9签收 */
	private String euStatus;

	public Date getTimePlan() {
		return timePlan;
	}

	public void setTimePlan(Date timePlan) {
		this.timePlan = timePlan;
	}

	public Date getDateStop() {
		return dateStop;
	}

	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}

	public String getFlagErase() {
		return flagErase;
	}

	public void setFlagErase(String flagErase) {
		this.flagErase = flagErase;
	}

	public String getFlagStop() {
		return flagStop;
	}

	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

}
