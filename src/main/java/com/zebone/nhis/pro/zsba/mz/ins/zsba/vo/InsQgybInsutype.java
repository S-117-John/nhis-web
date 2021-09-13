package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "Ins_Qgyb_Insutype")
public class InsQgybInsutype extends BaseModule{

	@PK
	@Field(value = "pk_insutype", id = KeyId.UUID)
	private String pkInsutype;

	//医保患者主键
	@Field("pk_insupi")
	@JsonProperty("pk_insupi")
	private String pkInsupi;
	// 余额
	@Field("balc")
	private String balc;
	// 险种类型
	@Field("insutype")
	private String insutype;
	// 人员类别
	@Field("psn_type")
	@JsonProperty("psn_type")
	private String psnType;

	// 公务员标志
	@Field("cvlserv_flag")
	@JsonProperty("cvlserv_flag")
	private String cvlservFlag;
	// 参保地医保区划
	@Field("insuplc_admdvs")
	@JsonProperty("insuplc_admdvs")
	private String insuplcAdmdvs;
	// 单位名称
	@Field("emp_name")
	@JsonProperty("emp_name")
	private String empName;
	// 参保状态
	@Field("psn_insu_stas")
	@JsonProperty("psn_insu_stas")
	private String psnInsuStas;
	// 参保日期
	@Field("psn_insu_date")
	@JsonProperty("psn_insu_date")
	private String psnInsuDate;
	// 停保日期
	@Field("paus_insu_date")
	@JsonProperty("paus_insu_date")
	private String pausInsuDate;

	public String getBalc() {
		return balc;
	}

	public void setBalc(String balc) {
		this.balc = balc;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
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

	public String getInsuplcAdmdvs() {
		return insuplcAdmdvs;
	}

	public void setInsuplcAdmdvs(String insuplcAdmdvs) {
		this.insuplcAdmdvs = insuplcAdmdvs;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getPkInsutype() {
		return pkInsutype;
	}

	public void setPkInsutype(String pkInsutype) {
		this.pkInsutype = pkInsutype;
	}

	public String getPsnInsuStas() {
		return psnInsuStas;
	}

	public void setPsnInsuStas(String psnInsuStas) {
		this.psnInsuStas = psnInsuStas;
	}

	public String getPsnInsuDate() {
		return psnInsuDate;
	}

	public void setPsnInsuDate(String psnInsuDate) {
		this.psnInsuDate = psnInsuDate;
	}

	public String getPausInsuDate() {
		return pausInsuDate;
	}

	public void setPausInsuDate(String pausInsuDate) {
		this.pausInsuDate = pausInsuDate;
	}

	public String getPkInsupi() {
		return pkInsupi;
	}

	public void setPkInsupi(String pkInsupi) {
		this.pkInsupi = pkInsupi;
	}



}
