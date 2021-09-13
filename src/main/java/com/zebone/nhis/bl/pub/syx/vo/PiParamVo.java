package com.zebone.nhis.bl.pub.syx.vo;

import java.util.Date;

public class PiParamVo {
   private String pkPi;
   
   private String pkPv;

	/** 门诊号 */
   private String codeOp;

	/** 患者分类 */
   private String pkPicate;

	/** 患者姓名 */
   private String namePi;

	/** 证件类型 */
   private String dtIdtype;

	/** 证件号码 */
   private String idNo;

	/** 健康卡号 */
   private String hicNo;

	/** 医保卡号 */
   private String insurNo;
   /**
    * 医保类型主键
    */
   private String pkInsu;

	/** 区域主索引 */
   private String mpi;

	/** 性别编码 */
   private String dtSex;

	/** 出生日期 */
   private Date birthDate;

	/** 手机号码 */
   private String mobile;


	/** 患者地址 */
   private String address;

	/** 联系人 */
   private String nameRel;

	/** 联系人电话 */
   private String telRel;
	
	/** 联系人证件类型*/
	private String	dtIdtypeRel;//默认身份证
	
	/** 联系人证件号*/
	private String	idnoRel;
	
	private String cardNo;//就诊卡号
	
	/** 现住址详细地址*/
	private String 	addrCurDt;
	/** 现住址详细地址描述*/
	private String addrCur;
	/** 优抚证号*/
	private String spcaNo;
	/** 老人证号*/
	private String senNo;
	/** 医疗证号*/
	private String mcno;
	/** 市民卡号*/
	private String citizenNo;
	/** 特约单位*/
	private String dtSpecunit;
	/** 患者来源*/
	private String dtSource;
	/** 备注信息*/
	private String note;
	
	private Date ts;
	
	private String modifier;
	
	private String pkHp;
	
	private String pkEmp;
	
	private String flagRealname;
	
	private String agePv;
	
	private String codePi;
	
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getFlagRealname() {
		return flagRealname;
	}
	public void setFlagRealname(String flagRealname) {
		this.flagRealname = flagRealname;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getAddrCur() {
		return addrCur;
	}
	public void setAddrCur(String addrCur) {
		this.addrCur = addrCur;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
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
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getPkPicate() {
		return pkPicate;
	}
	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getDtIdtype() {
		return dtIdtype;
	}
	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getHicNo() {
		return hicNo;
	}
	public void setHicNo(String hicNo) {
		this.hicNo = hicNo;
	}
	public String getInsurNo() {
		return insurNo;
	}
	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}
	public String getPkInsu() {
		return pkInsu;
	}
	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
	public String getMpi() {
		return mpi;
	}
	public void setMpi(String mpi) {
		this.mpi = mpi;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNameRel() {
		return nameRel;
	}
	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}
	public String getTelRel() {
		return telRel;
	}
	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}
	public String getDtIdtypeRel() {
		return dtIdtypeRel;
	}
	public void setDtIdtypeRel(String dtIdtypeRel) {
		this.dtIdtypeRel = dtIdtypeRel;
	}
	public String getIdnoRel() {
		return idnoRel;
	}
	public void setIdnoRel(String idnoRel) {
		this.idnoRel = idnoRel;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAddrCurDt() {
		return addrCurDt;
	}
	public void setAddrCurDt(String addrCurDt) {
		this.addrCurDt = addrCurDt;
	}
	public String getSpcaNo() {
		return spcaNo;
	}
	public void setSpcaNo(String spcaNo) {
		this.spcaNo = spcaNo;
	}
	public String getSenNo() {
		return senNo;
	}
	public void setSenNo(String senNo) {
		this.senNo = senNo;
	}
	public String getMcno() {
		return mcno;
	}
	public void setMcno(String mcno) {
		this.mcno = mcno;
	}
	public String getCitizenNo() {
		return citizenNo;
	}
	public void setCitizenNo(String citizenNo) {
		this.citizenNo = citizenNo;
	}
	public String getDtSpecunit() {
		return dtSpecunit;
	}
	public void setDtSpecunit(String dtSpecunit) {
		this.dtSpecunit = dtSpecunit;
	}
	public String getDtSource() {
		return dtSource;
	}
	public void setDtSource(String dtSource) {
		this.dtSource = dtSource;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getAgePv() {
		return agePv;
	}
	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}
	
}
