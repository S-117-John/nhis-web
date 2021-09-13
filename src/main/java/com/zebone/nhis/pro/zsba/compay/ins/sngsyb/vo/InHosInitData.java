package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;


/**
 * 省内工伤医保入院登记界面数据
 * @author lipz
 *
 */
public class InHosInitData {
	
	private String pkInspvwi;//	省内工伤医保登记主键
	
	private String pkPi;//		患者主键
	private String pkPv;//		就诊主键
	private String pkInsu;//	医保主计划主键 
	
	private String ipTimes;//	住院次数 
	private String dtSex;//		性别编码
	private String namePi;//	患者姓名
	private String birthDate;//	出生日期	格式：yyyyMMdd
	
	private String aka130;//	业务类型，见码表，41：工伤门诊、42：工伤住院
	private String bka006;//	医疗待遇类型	见码表
	private String bka017;//	住院时间	格式：yyyyMMdd
	private String bka019;//	就诊科室	 
	private String bka020;//	就诊科室名称	 
	private String bka021;//	病区编码	 
	private String bka022;//	病区名称
	private String bka023;//	床位号	 	
	private String bka025;//	住院号	 
	private String bka026;//	诊断,疾病ICD编码
	private String bka503;//	医师编码

	
	private String aae140;//	险种编码,工伤：410
	private String aaz218;//	就医登记号
	
	private PersonRowInfo gSPatientInfo;
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
	public String getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAka130() {
		return aka130;
	}
	public void setAka130(String aka130) {
		this.aka130 = aka130;
	}
	public String getBka006() {
		return bka006;
	}
	public void setBka006(String bka006) {
		this.bka006 = bka006;
	}
	public String getBka017() {
		return bka017;
	}
	public void setBka017(String bka017) {
		this.bka017 = bka017;
	}
	public String getBka019() {
		return bka019;
	}
	public void setBka019(String bka019) {
		this.bka019 = bka019;
	}
	public String getBka020() {
		return bka020;
	}
	public void setBka020(String bka020) {
		this.bka020 = bka020;
	}
	public String getBka021() {
		return bka021;
	}
	public void setBka021(String bka021) {
		this.bka021 = bka021;
	}
	public String getBka022() {
		return bka022;
	}
	public void setBka022(String bka022) {
		this.bka022 = bka022;
	}
	public String getBka023() {
		return bka023;
	}
	public void setBka023(String bka023) {
		this.bka023 = bka023;
	}
	public String getBka025() {
		return bka025;
	}
	public void setBka025(String bka025) {
		this.bka025 = bka025;
	}
	public String getBka026() {
		return bka026;
	}
	public void setBka026(String bka026) {
		this.bka026 = bka026;
	}
	public String getBka503() {
		return bka503;
	}
	public void setBka503(String bka503) {
		this.bka503 = bka503;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAae140() {
		return aae140;
	}
	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}
	public String getAaz218() {
		return aaz218;
	}
	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}
	public PersonRowInfo getgSPatientInfo() {
		return gSPatientInfo;
	}
	public void setgSPatientInfo(PersonRowInfo gSPatientInfo) {
		this.gSPatientInfo = gSPatientInfo;
	}



}
