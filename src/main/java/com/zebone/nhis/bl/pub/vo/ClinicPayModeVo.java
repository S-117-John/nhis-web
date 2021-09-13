package com.zebone.nhis.bl.pub.vo;

/** 描述诊间支付 支付方式
 */
public class ClinicPayModeVo {

	private double amountPay;		//	实付金额 amount_pay
	private String dtPaymode;		//	支付方式 dt_paymode
	private String note;			//	支付描述信息 note
	private String serialNo;        //  商户订单号
	
	public double getAmountPay() {
		return amountPay;
	}
	public void setAmountPay(double amountPay) {
		this.amountPay = amountPay;
	}
	public String getDtPaymode() {
		return dtPaymode;
	}
	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
}
