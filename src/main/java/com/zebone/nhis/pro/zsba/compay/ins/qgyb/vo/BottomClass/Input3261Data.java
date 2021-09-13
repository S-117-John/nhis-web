package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.1.1.2异地清分结果确认3261 结算信息
 * @author Administrator
 *
 */
public class Input3261Data {
	
	private String	trt_year;//	结算年度
	private String	trt_month;//	结算月份   格式为：MM如1月为01
	private String	totalrow;//	总行数
	public String getTrt_year() {
		return trt_year;
	}
	public void setTrt_year(String trt_year) {
		this.trt_year = trt_year;
	}
	public String getTrt_month() {
		return trt_month;
	}
	public void setTrt_month(String trt_month) {
		this.trt_month = trt_month;
	}
	public String getTotalrow() {
		return totalrow;
	}
	public void setTotalrow(String totalrow) {
		this.totalrow = totalrow;
	}

}
