package com.zebone.nhis.bl.pub.vo;

/***
 * 患者未打印发票的结算数据
 */
public class BlSettleInvoiceVo {

	private String pkSettle;// 结算主键
	private String dateCg;// 结算日期
	private String amountSt;// 结算金额
	private String amountPi;// 自费金额
	private String amountInsu;// 医保支付
	private String dtSttype;// 结算类型

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getDateCg() {
		return dateCg;
	}

	public void setDateCg(String dateCg) {
		this.dateCg = dateCg;
	}

	public String getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(String amountSt) {
		this.amountSt = amountSt;
	}

	public String getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(String amountPi) {
		this.amountPi = amountPi;
	}

	public String getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(String amountInsu) {
		this.amountInsu = amountInsu;
	}

}
