package com.zebone.nhis.pro.zsba.compay.ins.pub.vo;

import java.math.BigDecimal;

/**
 * 查询异地医保出入院登记界面初始数据
 * @author zrj
 *
 */
public class YdHosInitialData extends InsZsPubPvOut{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2509476430686928786L;
	
	private String ipTimes; //住院次数 
	private String pkDept; //入院科室主键
	private String nameDept; //入院科室名称
	private String nameInsdept; //医保科室名称
	private String btnUpload; //用于判断是否可以录入费用明细的  1：可以录入 0：不可以
	private String preSettle;//用于判断是否可以进行预结算 1：可以 0：不可以 2：不需要
	private BigDecimal akc264;//医疗费总额
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameInsdept() {
		return nameInsdept;
	}
	public void setNameInsdept(String nameInsdept) {
		this.nameInsdept = nameInsdept;
	}
	public String getBtnUpload() {
		return btnUpload;
	}
	public void setBtnUpload(String btnUpload) {
		this.btnUpload = btnUpload;
	}
	public String getPreSettle() {
		return preSettle;
	}
	public void setPreSettle(String preSettle) {
		this.preSettle = preSettle;
	}
	public BigDecimal getAkc264() {
		return akc264;
	}
	public void setAkc264(BigDecimal akc264) {
		this.akc264 = akc264;
	}

}
