package com.zebone.nhis.ex.pub.vo;

import java.util.Date;
/**
 * 医嘱单次执行数量
 * @author yangxue
 *
 */
public class OrderExecVo {
	private Date exceTime;	//执行时间
	private double quanCur;		//执行数量   
	
	public Date getExceTime() {
		return exceTime;
	}
	public void setExceTime(Date exceTime) {
		this.exceTime = exceTime;
	}
	
	public double getQuanCur() {
		return quanCur;
	}
	public void setQuanCur(double quanCur) {
		this.quanCur = quanCur;
	}
	
}
