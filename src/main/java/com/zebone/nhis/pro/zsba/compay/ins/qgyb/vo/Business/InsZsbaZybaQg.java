package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_ZYBA_QG
 *
 * @since 2020-11-13 10:42:10
 */
@Table(value="INS_ZYBA_QG")
public class InsZsbaZybaQg extends BaseModule{

	@PK
	@Field(value="PK_ZYBA_QG",id=KeyId.UUID)
    private String pkZybaQg;
	
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="mdtrt_id")
	private String mdtrtId;
	
	@Field(value="TRT_DCLA_DETL_SN")
    private String trtDclaDetlSn;
	
	@Field(value="PSN_NO")
    private String psnNo;
	
	@Field(value="INSUTYPE")
    private String insutype;
	
	@Field(value="TEL")
    private String tel;
	
	@Field(value="ADDR")
    private String addr;
	
	@Field(value="INSU_OPTINS")
    private String insuOptins;
	
	@Field(value="DIAG_CODE")
    private String diagCode;
	
	@Field(value="DIAG_NAME")
    private String diagName;
	
	@Field(value="DISE_COND_DSCR")
    private String diseCondDscr;
	
	@Field(value="REFLIN_MEDINS_NO")
    private String reflinMedinsNo;
	
	@Field(value="REFLIN_MEDINS_NAME")
    private String reflinMedinsName;
	
	@Field(value="MDTRTAREA_ADMDVS")
    private String mdtrtareaAdmdvs;

	@Field(value="HOSP_AGRE_REFL_FLAG")
    private String hospAgreReflFlag;
	
	@Field(value="REFL_TYPE")
    private String reflType;
	
	@Field(value="REFL_DATE")
    private String reflDate;
	
	@Field(value="REFL_REA")
    private String reflRea;
	
	@Field(value="REFL_OPNN")
    private String reflOpnn;
	
	@Field(value="BEGNDATE")
    private String begnDate;
	
	@Field(value="ENDDATE")
    private String endDate;
	
	@Field(value="MEMO")
    private String memo;

	@Field(value="FLAG_CANCEL")
    private String flagCancel;
	
	@Field(value="CODE_IP")
    private String codeIp;
	
	@Field(value="ID_NO")
    private String idNo;
	
	private String code;
	private String msg;
	private String ip;
	private String mac;
	
	public String getPkZybaQg() {
		return pkZybaQg;
	}

	public void setPkZybaQg(String pkZybaQg) {
		this.pkZybaQg = pkZybaQg;
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

	public String getTrtDclaDetlSn() {
		return trtDclaDetlSn;
	}

	public void setTrtDclaDetlSn(String trtDclaDetlSn) {
		this.trtDclaDetlSn = trtDclaDetlSn;
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

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getDiseCondDscr() {
		return diseCondDscr;
	}

	public void setDiseCondDscr(String diseCondDscr) {
		this.diseCondDscr = diseCondDscr;
	}

	public String getReflinMedinsNo() {
		return reflinMedinsNo;
	}

	public void setReflinMedinsNo(String reflinMedinsNo) {
		this.reflinMedinsNo = reflinMedinsNo;
	}

	public String getReflinMedinsName() {
		return reflinMedinsName;
	}

	public void setReflinMedinsName(String reflinMedinsName) {
		this.reflinMedinsName = reflinMedinsName;
	}

	public String getMdtrtareaAdmdvs() {
		return mdtrtareaAdmdvs;
	}

	public void setMdtrtareaAdmdvs(String mdtrtareaAdmdvs) {
		this.mdtrtareaAdmdvs = mdtrtareaAdmdvs;
	}

	public String getHospAgreReflFlag() {
		return hospAgreReflFlag;
	}

	public void setHospAgreReflFlag(String hospAgreReflFlag) {
		this.hospAgreReflFlag = hospAgreReflFlag;
	}

	public String getReflType() {
		return reflType;
	}

	public void setReflType(String reflType) {
		this.reflType = reflType;
	}

	public String getReflDate() {
		return reflDate;
	}

	public void setReflDate(String reflDate) {
		this.reflDate = reflDate;
	}

	public String getReflRea() {
		return reflRea;
	}

	public void setReflRea(String reflRea) {
		this.reflRea = reflRea;
	}

	public String getReflOpnn() {
		return reflOpnn;
	}

	public void setReflOpnn(String reflOpnn) {
		this.reflOpnn = reflOpnn;
	}

	public String getBegnDate() {
		return begnDate;
	}

	public void setBegnDate(String begnDate) {
		this.begnDate = begnDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFlagCancel() {
		return flagCancel;
	}

	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
}
