package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;

public class AdtRegParam {
	
	private PiMaster pi_master; //患者基本信息
	
	private PvEncounter pv_encounter; //患者就诊信息
	
	/** 患者主键 */
	private String pkPi;
	
	/** 就诊编码 */
	private String codePv;
	
	/** 现住址 */
	private String address;
	
	/** 婚姻编码 */
	private String dtMarry;
	
	/** 主医保计划 */
	private String pkInsu;
	
	/** 收治医生主键 */
	private String pkEmpTre;
	
	/** 收治医生编码 */
	private String codeEmpTre;
	
	/** 收治医生姓名 */
	private String nameEmpTre;
	
	/** 登记日期（入院日期） */
	private Date dateReg;
	
	/** 患者分类 */
	private String pkPicate;
	
	/** 入院科室 */
	private String pkDeptAdmit;
	
	/** 入院病区 */
	private String pkDeptNsAdmit;
	
	/** 入院方式 */
	private String dtIntype;
	
	/** 病情等级 */
	private String dtLevelDise;
	
	/** 入院通知单主键 */
	private String pkInNotice;
	
	/** 诊断编码 */
	private String pkDiag;
	
	/** 诊断名称 */
	private String descDiag;
	
	/** 诊断名称 */
	private String codeDiag;
	
	/** 押金金额 */
	private Double amount;
	
	/** 付款方式 */
	private String dtPaymode;
	
	/** 银行 */
	private String dtBank;
	
	/** 银行卡号 */
	private String bankNo;
	
	/** 住院次数 */
	private Integer ipTimes;
	
	/** 诊断类型 */
	private String dtDiagtype;
	
	/** 辅医保计划[string数组] */
	private String[] pkHps;
	
	/** 身高 */
	private Double height;
	
	/** 联系电话 */
	private String telRel;
	
	/** 联系人 */
	private String nameRel;
	
	/** 医保类型 */
	private String euHptype;
	
	/** 医疗证号 */
	private String mcno;
	
	/** 特约单位 */
	private String dictSpecunit;

	/*患者有未结算记录时允许再次住院1允许0否*/
	private String allowReHospital;
	
	/**感染标志*/
	private String flagInfected;
	
	public String getCodeEmpTre() {
		return codeEmpTre;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public void setCodeEmpTre(String codeEmpTre) {
		this.codeEmpTre = codeEmpTre;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDtMarry() {
		return dtMarry;
	}

	public void setDtMarry(String dtMarry) {
		this.dtMarry = dtMarry;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getPkEmpTre() {
		return pkEmpTre;
	}

	public void setPkEmpTre(String pkEmpTre) {
		this.pkEmpTre = pkEmpTre;
	}

	public String getNameEmpTre() {
		return nameEmpTre;
	}

	public void setNameEmpTre(String nameEmpTre) {
		this.nameEmpTre = nameEmpTre;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	public String getPkDeptAdmit() {
		return pkDeptAdmit;
	}

	public void setPkDeptAdmit(String pkDeptAdmit) {
		this.pkDeptAdmit = pkDeptAdmit;
	}

	public String getPkDeptNsAdmit() {
		return pkDeptNsAdmit;
	}

	public void setPkDeptNsAdmit(String pkDeptNsAdmit) {
		this.pkDeptNsAdmit = pkDeptNsAdmit;
	}

	public String getDtIntype() {
		return dtIntype;
	}

	public void setDtIntype(String dtIntype) {
		this.dtIntype = dtIntype;
	}

	public String getDtLevelDise() {
		return dtLevelDise;
	}

	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}

	public String getPkInNotice() {
		return pkInNotice;
	}

	public void setPkInNotice(String pkInNotice) {
		this.pkInNotice = pkInNotice;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	public String getDtBank() {
		return dtBank;
	}

	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public Integer getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(Integer ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getDtDiagtype() {
		return dtDiagtype;
	}

	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}

	public String[] getPkHps() {
		return pkHps;
	}

	public void setPkHps(String[] pkHps) {
		this.pkHps = pkHps;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public String getTelRel() {
		return telRel;
	}

	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public PiMaster getPiMaster() {
		return pi_master;
	}

	public void setPiMaster(PiMaster piMaster) {
		this.pi_master = piMaster;
	}

	public PvEncounter getPvEncounter() {
		return pv_encounter;
	}

	public void setPvEncounter(PvEncounter pvEncounter) {
		this.pv_encounter = pvEncounter;
	}

	public String getMcno() {
		return mcno;
	}

	public void setMcno(String mcno) {
		this.mcno = mcno;
	}

	public String getDictSpecunit() {
		return dictSpecunit;
	}

	public void setDictSpecunit(String dictSpecunit) {
		this.dictSpecunit = dictSpecunit;
	}

	public String getEuHptype() {
		return euHptype;
	}

	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}

	public String getAllowReHospital() {
		return allowReHospital;
	}

	public void setAllowReHospital(String allowReHospital) {
		this.allowReHospital = allowReHospital;
	}

	public String getFlagInfected() {
		return flagInfected;
	}

	public void setFlagInfected(String flagInfected) {
		this.flagInfected = flagInfected;
	}

}
