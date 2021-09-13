package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;

public class InsQgybTrtinfo {

	private String psn_no;
	private String trt_enjymnt_flag;
	private String fund_pay_type;
	private String trt_chk_type;
	private String trt_chk_rslt;
	private Date begndate;
	private Date enddate;
	public String getPsn_no() {
		return psn_no;
	}
	public void setPsn_no(String psn_no) {
		this.psn_no = psn_no;
	}
	public String getTrt_enjymnt_flag() {
		return trt_enjymnt_flag;
	}
	public void setTrt_enjymnt_flag(String trt_enjymnt_flag) {
		this.trt_enjymnt_flag = trt_enjymnt_flag;
	}
	public String getFund_pay_type() {
		return fund_pay_type;
	}
	public void setFund_pay_type(String fund_pay_type) {
		this.fund_pay_type = fund_pay_type;
	}
	public String getTrt_chk_type() {
		return trt_chk_type;
	}
	public void setTrt_chk_type(String trt_chk_type) {
		this.trt_chk_type = trt_chk_type;
	}
	public String getTrt_chk_rslt() {
		return trt_chk_rslt;
	}
	public void setTrt_chk_rslt(String trt_chk_rslt) {
		this.trt_chk_rslt = trt_chk_rslt;
	}
	public Date getBegndate() {
		return begndate;
	}
	public void setBegndate(Date begndate) {
		this.begndate = begndate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
}
