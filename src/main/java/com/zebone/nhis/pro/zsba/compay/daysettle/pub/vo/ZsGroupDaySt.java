package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

public class ZsGroupDaySt {
	/**
	 * 日结操作员
	 */
	private String nameEmpOpera;
	/**
	 * 日结截止时间
	 */
	private String dateEnd;
	/**
	 * 结算金额
	 */
	private String amtSettle;
	/**
	 * 预交金金额
	 */
	private String amtPrep;
	/**
	 * 实收金额
	 */
	private String amtSs;
	public String getNameEmpOpera() {
		return nameEmpOpera;
	}
	public void setNameEmpOpera(String nameEmpOpera) {
		this.nameEmpOpera = nameEmpOpera;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getAmtSettle() {
		return amtSettle;
	}
	public void setAmtSettle(String amtSettle) {
		this.amtSettle = amtSettle;
	}
	public String getAmtPrep() {
		return amtPrep;
	}
	public void setAmtPrep(String amtPrep) {
		this.amtPrep = amtPrep;
	}
	public String getAmtSs() {
		return amtSs;
	}
	public void setAmtSs(String amtSs) {
		this.amtSs = amtSs;
	}
}
