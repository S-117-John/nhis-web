package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.platform.modules.dao.build.au.Field;

public class OutParamHuaJia extends InsQgybSt{

    // 人员编号
	@Field(value = "psn_no")
	@JsonProperty("psn_no")
	private String psnNo ;
    // 人员姓名
	@Field(value = "psn_name")
	@JsonProperty("psn_name")
	private String psnName ;
    // 人员证件类型
	@JsonProperty("psn_cert_type")
	private String psnCertType ;
    // 证件号码
	@JsonProperty("certno")
	private String certno ;
    // 性别
	@JsonProperty("gend")
	private String gend ;
    // 民族
	@JsonProperty("naty")
	private String naty ;
    // 出生日期
	@JsonProperty("brdy")
	private String brdy ;
    // 年龄
	@JsonProperty("age")
	private String age ;

    // 人员类别
	@JsonProperty("psn_type")
	private String psnType ;
    // 公务员标志
	@JsonProperty("cvlserv_flag")
	private String cvlservFlag ;
	
	private List<InsQgybStDt> insStDts;
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}
	public String getPsnName() {
		return psnName;
	}
	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}
	public String getPsnCertType() {
		return psnCertType;
	}
	public void setPsnCertType(String psnCertType) {
		this.psnCertType = psnCertType;
	}
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
	public String getGend() {
		return gend;
	}
	public void setGend(String gend) {
		this.gend = gend;
	}
	public String getNaty() {
		return naty;
	}
	public void setNaty(String naty) {
		this.naty = naty;
	}
	public String getBrdy() {
		return brdy;
	}
	public void setBrdy(String brdy) {
		this.brdy = brdy;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPsnType() {
		return psnType;
	}
	public void setPsnType(String psnType) {
		this.psnType = psnType;
	}
	public String getCvlservFlag() {
		return cvlservFlag;
	}
	public void setCvlservFlag(String cvlservFlag) {
		this.cvlservFlag = cvlservFlag;
	}
	public List<InsQgybStDt> getInsStDts() {
		return insStDts;
	}
	public void setInsStDts(List<InsQgybStDt> insStDts) {
		this.insStDts = insStDts;
	}

	
    
}
