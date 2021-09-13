package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.1.1.3异地清分结果确认回退
 * @author Administrator
 *
 */
public class Input3262Data {
	
	private String	trt_year;//	结算年度
	private String	trt_month;//	结算月份   格式为：MM如1月为01
	private String	otransid;//	原交易流水号 需要作回退处理的原业务交易接收方报文ID(inf_refmsgid)。传入‘0’时，回退医疗机构该结算周期的所有确认业务
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
	public String getOtransid() {
		return otransid;
	}
	public void setOtransid(String otransid) {
		this.otransid = otransid;
	}
}
