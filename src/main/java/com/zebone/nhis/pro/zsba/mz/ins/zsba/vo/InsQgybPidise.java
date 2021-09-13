package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
@Table(value = "ins_qgyb_pidise")
public class InsQgybPidise extends BaseModule {
	
	@PK
	@Field(value = "pk_pidise", id = KeyId.UUID)
	private String pkPidise;
	@Field("pk_pi")
	private String pkPi;
	@Field("code_pi")
	private String codePi;
	@Field("psn_no")
	@JsonProperty("psn_no")
	private String psnNo;
	@Field("insutype")
	private String insutype;
	//门慢门特病种目录代码
	@Field("opsp_dise_code")
	@JsonProperty("opsp_dise_code")
	private String opspDiseCode;
	//门慢门特病种名称
	@Field("opsp_dise_name")
	@JsonProperty("opsp_dise_name")
	private String opspDiseName;
	
	@Field("tel")
	private String tel;
	@Field("addr")
	private String addr;
	@Field("insu_optins")
	@JsonProperty("insu_optins")
	private String insuOptins;
	@Field("ide_fixmedins_no")
	@JsonProperty("ide_fixmedins_no")
	private String ideFixmedinsNo;
	@Field("ide_fixmedins_name")
	@JsonProperty("ide_fixmedins_name")
	private String ideFixmedinsName;
	
	//医院鉴定日期
	@Field("hosp_ide_date")
	@JsonProperty("hosp_ide_date")
	private Date hospIdeDate;
	//诊断医师编码
	@Field("diag_dr_codg")
	@JsonProperty("diag_dr_codg")
	private String diagDrCodg;
	//诊断医师名称
	@Field("diag_dr_name")
	@JsonProperty("diag_dr_name")
	private String diagDrName;
	
	@Field("begndate")
	private Date begndate;
	@Field("enddate")
	private Date enddate;
	@Field("trt_dcla_detl_sn")
	@JsonProperty("trt_dcla_detl_sn")
	private String trtDclaDetlSn;

	@Field("memo")
	private String memo;
	@Field("flag_canc")
	private String flagCanc;
	@Field("cancelor")
	private String cancelor;
	@Field("cancel_time")
	private Date cancelTime;
	public String getPkPidise() {
		return pkPidise;
	}
	public void setPkPidise(String pkPidise) {
		this.pkPidise = pkPidise;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public String getOpspDiseCode() {
		return opspDiseCode;
	}
	public void setOpspDiseCode(String opspDiseCode) {
		this.opspDiseCode = opspDiseCode;
	}
	public String getOpspDiseName() {
		return opspDiseName;
	}
	public void setOpspDiseName(String opspDiseName) {
		this.opspDiseName = opspDiseName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getInsuOptins() {
		return insuOptins;
	}
	public void setInsuOptins(String insuOptins) {
		this.insuOptins = insuOptins;
	}
	public String getIdeFixmedinsNo() {
		return ideFixmedinsNo;
	}
	public void setIdeFixmedinsNo(String ideFixmedinsNo) {
		this.ideFixmedinsNo = ideFixmedinsNo;
	}
	public String getIdeFixmedinsName() {
		return ideFixmedinsName;
	}
	public void setIdeFixmedinsName(String ideFixmedinsName) {
		this.ideFixmedinsName = ideFixmedinsName;
	}

	public String getDiagDrCodg() {
		return diagDrCodg;
	}
	public void setDiagDrCodg(String diagDrCodg) {
		this.diagDrCodg = diagDrCodg;
	}
	public String getDiagDrName() {
		return diagDrName;
	}
	public void setDiagDrName(String diagDrName) {
		this.diagDrName = diagDrName;
	}

	public String getTrtDclaDetlSn() {
		return trtDclaDetlSn;
	}
	public void setTrtDclaDetlSn(String trtDclaDetlSn) {
		this.trtDclaDetlSn = trtDclaDetlSn;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getFlagCanc() {
		return flagCanc;
	}
	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}
	public String getCancelor() {
		return cancelor;
	}
	public void setCancelor(String cancelor) {
		this.cancelor = cancelor;
	}
	public Date getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	public Date getHospIdeDate() {
		return hospIdeDate;
	}
	public void setHospIdeDate(Date hospIdeDate) {
		this.hospIdeDate = hospIdeDate;
	}
	public Date getBegndate() {
		return begndate;
	}
	public void setBegndate(Date begndate) {
		this.begndate = begndate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	

}
