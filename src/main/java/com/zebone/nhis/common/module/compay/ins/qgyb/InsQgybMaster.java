package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;


import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "ins_qgyb_master")
public class InsQgybMaster extends BaseModule {
	
	@PK
	@Field(value = "pk_insupi", id = KeyId.UUID)
	private String pkInsupi;
	@Field("pk_pi")
	private String pkPi;
    // 人员编号
	@Field("psn_no")
	@JsonProperty("psn_no")
	private String psnNo ;
    // 人员证件类型
	@Field("psn_cert_type")
	@JsonProperty("psn_cert_type")
	private String psnCertType ;
    // 证件号码
	@Field("certno")
	private String certno ;
    // 人员姓名
	@Field("psn_name")
	@JsonProperty("psn_name")
	private String psnName ;
    // 性别
	@Field("gend")
	private String gend ;
	// 民族
    @Field("naty")
    private String naty ;
    // 出生日期
    @Field("brdy")
    private String brdy ;
    // 年龄
    @Field("age")
    private String age ;
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
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
	public String getPsnName() {
		return psnName;
	}
	public void setPsnName(String psnName) {
		this.psnName = psnName;
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
	public String getPkInsupi() {
		return pkInsupi;
	}
	public void setPkInsupi(String pkInsupi) {
		this.pkInsupi = pkInsupi;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBrdy() {
		return brdy;
	}
	public void setBrdy(String brdy) {
		this.brdy = brdy;
	}

    
    
}
