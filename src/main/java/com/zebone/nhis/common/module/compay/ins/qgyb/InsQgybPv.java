package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "ins_qgyb_pv")
public class InsQgybPv extends BaseModule {

	
	private static final long serialVersionUID = 1L;

    /**
    * pk_ins_medtype
    */
	@PK
	@Field(value = "pk_inspv", id = Field.KeyId.UUID)
    private String pkInspv;

    /**
    * pk_pv
    */
	@Field(value = "pk_pv")
    private String pkPv;

    /**
    * pk_pi
    */
	@Field(value = "pk_pi")
    private String pkPi;

    /**
    * mdtrt_id
    */
	@Field(value = "mdtrt_id")
    private String mdtrtId;

    /**
    * name_pi
    */
	@Field(value = "name_pi")
    private String namePi;
    /**
    * pk_hp
    */
	@Field(value = "pk_hp")
    private String pkHp;

    /**
    * med_type
    */
	@Field(value = "med_type")
    private String medType;

    /**
    * dt_exthp
    */
	@Field(value = "dt_exthp")
    private String dtExthp;

    /**
    * main_cond_dscr
    */
	@Field(value = "main_cond_dscr")
    private String mainCondDscr;

    /**
    * dise_codg
    */
	@Field(value = "dise_codg")
    private String diseCodg;

    /**
    * dise_name
    */
	@Field(value = "dise_name")
    private String diseName;

    /**
    * birctrl_type
    */
	@Field(value = "birctrl_type")
    private String birctrlType;

    /**
    * birctrl_matn_date
    */
	@Field(value = "birctrl_matn_date")
    private Date birctrlMatnDate;

    /**
    * note
    */
	@Field(value = "note")
    private String note;

	/**
	 * 手术操作代码
	 */
	@Field(value = "oprn_Oprt_Code")
	private String oprnOprtCode;

	/**
	 * 计划生育服务证号
	 */
	@Field(value = "fpsc_No")
	private String fpscNo;

	/**
	 * 晚育标志
	 */
	@Field(value = "latechb_Flag")
	private String latechbFlag;

	/**
	 * 胎次
	 */
	@Field(value = "fetts")
	private String fetts;

	/**
	 * 胎儿数
	 */
	@Field(value = "fetus_Cnt")
	private String fetusCnt;

	/**
	 * 早产标志
	 */
	@Field(value = "pret_Flag")
	private String pretFlag;

	/**
	 * 手术操作代码
	 */
	@Field(value = "dise_Type_Code")
	private String diseTypeCode;

	@Field(value = "matn_type")
	private String matnType;

	@Field(value = "geso_val")
	private String gesoVal;

	public String getGesoVal() {
		return gesoVal;
	}

	public void setGesoVal(String gesoVal) {
		this.gesoVal = gesoVal;
	}

	public String getMatnType() {
		return matnType;
	}

	public void setMatnType(String matnType) {
		this.matnType = matnType;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getOprnOprtCode() {
		return oprnOprtCode;
	}

	public void setOprnOprtCode(String oprnOprtCode) {
		this.oprnOprtCode = oprnOprtCode;
	}

	public String getFpscNo() {
		return fpscNo;
	}

	public void setFpscNo(String fpscNo) {
		this.fpscNo = fpscNo;
	}

	public String getLatechbFlag() {
		return latechbFlag;
	}

	public void setLatechbFlag(String latechbFlag) {
		this.latechbFlag = latechbFlag;
	}

	public String getFetts() {
		return fetts;
	}

	public void setFetts(String fetts) {
		this.fetts = fetts;
	}

	public String getFetusCnt() {
		return fetusCnt;
	}

	public void setFetusCnt(String fetusCnt) {
		this.fetusCnt = fetusCnt;
	}

	public String getPretFlag() {
		return pretFlag;
	}

	public void setPretFlag(String pretFlag) {
		this.pretFlag = pretFlag;
	}

	public String getDiseTypeCode() {
		return diseTypeCode;
	}

	public void setDiseTypeCode(String diseTypeCode) {
		this.diseTypeCode = diseTypeCode;
	}

	public String getPkInspv() {
		return pkInspv;
	}

	public void setPkInspv(String pkInspv) {
		this.pkInspv = pkInspv;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}

	public String getDtExthp() {
		return dtExthp;
	}

	public void setDtExthp(String dtExthp) {
		this.dtExthp = dtExthp;
	}

	public String getMainCondDscr() {
		return mainCondDscr;
	}

	public void setMainCondDscr(String mainCondDscr) {
		this.mainCondDscr = mainCondDscr;
	}

	public String getDiseCodg() {
		return diseCodg;
	}

	public void setDiseCodg(String diseCodg) {
		this.diseCodg = diseCodg;
	}

	public String getDiseName() {
		return diseName;
	}

	public void setDiseName(String diseName) {
		this.diseName = diseName;
	}

	public String getBirctrlType() {
		return birctrlType;
	}

	public void setBirctrlType(String birctrlType) {
		this.birctrlType = birctrlType;
	}

	public Date getBirctrlMatnDate() {
		return birctrlMatnDate;
	}

	public void setBirctrlMatnDate(Date birctrlMatnDate) {
		this.birctrlMatnDate = birctrlMatnDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	

   
}
