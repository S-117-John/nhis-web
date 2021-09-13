package com.zebone.nhis.scm.material.vo;

import java.util.Date;

/**
 * 三证查询vo-供应商
 * @author chengjia
 *
 */
public class MtlLicenseSupVo {
	
	//编码
	private String code;
	
	//名称
	private String name;
	
	//联系人
	private String nameRel;
	
	//联系电话
	private String tel;
	
	//注册证号
	private String regNo;
	
	//经营效期
	private Date dateValidRun;
	
	//许可证号
	private String licenseNo;
	
	//许可效期
	private Date dateValidLicense;

	private String spcode;
	
	
	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Date getDateValidRun() {
		return dateValidRun;
	}

	public void setDateValidRun(Date dateValidRun) {
		this.dateValidRun = dateValidRun;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public Date getDateValidLicense() {
		return dateValidLicense;
	}

	public void setDateValidLicense(Date dateValidLicense) {
		this.dateValidLicense = dateValidLicense;
	}
	
}
