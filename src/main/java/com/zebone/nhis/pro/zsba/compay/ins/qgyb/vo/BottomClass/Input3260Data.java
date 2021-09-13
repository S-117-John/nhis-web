package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 3260提取异地清分明细
 * @author Administrator
 *
 */
public class Input3260Data {
	
	private String	trt_year;//	结算年度
	private String	trt_month;//	结算月份   格式为：MM如1月为01
	private String	startrow;//	开始行数
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
	public String getStartrow() {
		return startrow;
	}
	public void setStartrow(String startrow) {
		this.startrow = startrow;
	}
}
