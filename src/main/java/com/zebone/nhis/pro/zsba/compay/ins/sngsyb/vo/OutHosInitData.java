package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;


/**
 * 省内工伤医保出院院登记界面数据
 * @author lipz
 *
 */
public class OutHosInitData {
	
	private String pkInspvwi;//	省内工伤医保登记主键
	
	private String pkPi;//		患者主键
	private String pkPv;//		就诊主键
	private String pkInsu;//	医保主计划主键 
	
	private String aaz218;//	医保就诊登记号
	private String bka032;//出院日期	格式：yyyyMMdd
	private String bka033;//登记人员工号	 
	private String bka034;//登记人姓名	 
	private String bkf002;//入院方式	 
	private String bkf003;//入院情况	 
	private String bkf004;//出院转归情况	 
	private String bka031;//出院诊断	 

	/*
	 * 状态标志
	 * 
	 *  1入院登记成功，2入院登记失败；3资料维护成功，4资料维护失败；
        5出院登记成功，6出院登记失败；7取消出院登记成功，
        8取消出院登记失败；9取消入院登记成功，
        10取消入院登记失败；11结算成功，
        12结算失败；13取消结算成功，14取消结算失败；15跨月取消结算成功，
        16跨月取消结算失败
	 */
	private String status;

	
	
	public String getPkInspvwi() {
		return pkInspvwi;
	}

	public void setPkInspvwi(String pkInspvwi) {
		this.pkInspvwi = pkInspvwi;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getAaz218() {
		return aaz218;
	}

	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}

	public String getBka032() {
		return bka032;
	}

	public void setBka032(String bka032) {
		this.bka032 = bka032;
	}

	public String getBka033() {
		return bka033;
	}

	public void setBka033(String bka033) {
		this.bka033 = bka033;
	}

	public String getBka034() {
		return bka034;
	}

	public void setBka034(String bka034) {
		this.bka034 = bka034;
	}

	public String getBkf002() {
		return bkf002;
	}

	public void setBkf002(String bkf002) {
		this.bkf002 = bkf002;
	}

	public String getBkf003() {
		return bkf003;
	}

	public void setBkf003(String bkf003) {
		this.bkf003 = bkf003;
	}

	public String getBkf004() {
		return bkf004;
	}

	public void setBkf004(String bkf004) {
		this.bkf004 = bkf004;
	}

	public String getBka031() {
		return bka031;
	}

	public void setBka031(String bka031) {
		this.bka031 = bka031;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
