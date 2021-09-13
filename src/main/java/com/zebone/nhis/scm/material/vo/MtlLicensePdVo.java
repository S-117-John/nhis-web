package com.zebone.nhis.scm.material.vo;

import java.util.Date;

/**
 * 三证查询vo-物品
 * @author chengjia
 *
 */
public class MtlLicensePdVo {
	
	//物品主键
	private String pkPd;
	
	//物品编码
	private String code;
	
	//物品名称
	private String name;
	
	//规格
	private String spec;
	
	//单位名称
	private String unitName;
	
	//厂家名称
	private String factoryName;
	
	//注册证号
	private String regNo;
	
	//注册效期
	private Date dateValidReg;

	private String spcode;
	
	
	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
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

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public Date getDateValidReg() {
		return dateValidReg;
	}

	public void setDateValidReg(Date dateValidReg) {
		this.dateValidReg = dateValidReg;
	}
	
	
	
}
