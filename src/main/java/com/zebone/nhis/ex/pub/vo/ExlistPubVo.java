package com.zebone.nhis.ex.pub.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;

import java.util.Date;

/**
 * 医嘱执行单参数表
 * @author yangxue  
 *
 */
public class ExlistPubVo {
	private String bedNo;
    private String namePi;
    private String pkExocc;
    private Date datePlan;
    private Date dateOcc;
    private Double quanOcc;
    private String pkEmpOcc;
    private String pkEmpOcc2;
    private String nameEmpOcc;
    private String nameEmpOcc2;
    private String euStatus;
    private String nameUnit;
    private String nameDeptOcc;
    private String nameOrgOcc;
    private String flagAp;
    private String flagBack;
    private String flagDe;
    private String flagSelf;
    private String flagBase;
    private String flagDurg;
    private String nameOrd;
    private String flagFit;
    private String descFit;//适应症描述
    private String flagBl;
    private String infantNo;
    private String pkCnord;
    private String pkOrd;
    private String codeSupply;
    private int ordsn;
    private int ordsnParent;
    private String flagSt;
    private String euSt;
    private String nameFreq;
    private String nameSupply;
    private String isskt;
    private String euPvtype;
    private String pkOrg;
    private String pkPres;
    private String pkPv;
    private String pkEmpOrd;
    private String nameEmpOrd;
    private String pkDept;//开立科室
	private String nameDept;//开立科室名称
    private String pkDeptExec;//执行科室
    private String pkPi;
    private String pkUnit;
    private String pkOrgOcc;
    private String flagCheck;
    private String pkDeptPv;
    private String pkDeptNs;//开立病区
    private String euAlways;//长期、临时 
    private String euAlwaysFreq;//频次维护表对应的长期、临时标志
    private String sign;//同组标志
    private Integer packSize;//包装量
    private String noteOrd;
	private String pkEmpCanc;//撤销人账号
	private String nameEmpCanc;//撤销人姓名
	private String spec;//药品规格
	private String nameUnitDos; //剂量单位
	private Double dosage; //剂量
	private Double price; //单价
	private Double priceCg; //医嘱单价
    private Date dateStart;//医嘱开始时间
	private Date datePlanEx;//计划执行时间
	private String pkEmpEx;//医嘱执行护士
	private String nameEmpEx;//医嘱执行护士
	private boolean flagLabor;//产房标志
	private int cntFreq;//频次数量
	private String flagPivas;//静配标志
	private Double firstNum;//首次
	private Double lastNum;//末次
	private String ordtype;//医嘱类型
	private Double groupno;//医嘱序号
	private String flagMedout;//出院带药标志
	private CnSignCa cnSignCa;//CA认证信息
	private String euAddcharge;//附加费标志
	private String taskAutocgFlag;  //定时任务自动执行记费标志 1标志为定时任务调用
	private  String pkEmpChk;//核对人
	private String euBltype;
	private String codeOrdtype;//医嘱类型
	private String risEuStatus;//检查申请单状态
	private String chargingStatus;//s收费状态
	private String consultFlag;//会诊申请医嘱标志, 1 会诊申请医嘱,0或其它 非会诊申请医嘱
	private String otherDeptOrderAndFeeCheckFlag;//非本科室医嘱且执行时校验重复计费标志,1 是,0或其它 否
    private String flagNote;//嘱托标志
	private String pkPdapdt;//对应领药主键
	private Date ts;	

	public String getChargingStatus() {
		return chargingStatus;
	}

	public void setChargingStatus(String chargingStatus) {
		this.chargingStatus = chargingStatus;
	}

	public String getEuBltype() {
		return euBltype;
	}

	public void setEuBltype(String euBltype) {
		this.euBltype = euBltype;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getTaskAutocgFlag() {
		return taskAutocgFlag;
	}

	public void setTaskAutocgFlag(String taskAutocgFlag) {
		this.taskAutocgFlag = taskAutocgFlag;
	}
	
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}

	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}

	public boolean isFlagLabor() {
		return flagLabor;
	}

	public void setFlagLabor(boolean flagLabor) {
		this.flagLabor = flagLabor;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}

	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getPkEmpCanc() {
		return pkEmpCanc;
	}

	public void setPkEmpCanc(String pkEmpCanc) {
		this.pkEmpCanc = pkEmpCanc;
	}

	public String getNameEmpCanc() {
		return nameEmpCanc;
	}

	public void setNameEmpCanc(String nameEmpCanc) {
		this.nameEmpCanc = nameEmpCanc;
	}


	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getEuAlwaysFreq() {
		return euAlwaysFreq;
	}
	public void setEuAlwaysFreq(String euAlwaysFreq) {
		this.euAlwaysFreq = euAlwaysFreq;
	}
	public String getEuSt() {
		return euSt;
	}
	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}
	public Integer getPackSize() {
		return packSize;
	}
	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getPkDeptPv() {
		return pkDeptPv;
	}
	public void setPkDeptPv(String pkDeptPv) {
		this.pkDeptPv = pkDeptPv;
	}
	public String getFlagCheck() {
		return flagCheck;
	}
	public void setFlagCheck(String flagCheck) {
		this.flagCheck = flagCheck;
	}
	public String getPkOrgOcc() {
		return pkOrgOcc;
	}
	public void setPkOrgOcc(String pkOrgOcc) {
		this.pkOrgOcc = pkOrgOcc;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkEmpOrd() {
		return pkEmpOrd;
	}
	public void setPkEmpOrd(String pkEmpOrd) {
		this.pkEmpOrd = pkEmpOrd;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public Date getDateOcc() {
		return dateOcc;
	}
	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}
	public Double getQuanOcc() {
		return quanOcc;
	}
	public void setQuanOcc(Double quanOcc) {
		this.quanOcc = quanOcc;
	}
	public String getPkEmpOcc() {
		return pkEmpOcc;
	}
	public void setPkEmpOcc(String pkEmpOcc) {
		this.pkEmpOcc = pkEmpOcc;
	}
	public String getPkEmpOcc2() {
		return pkEmpOcc2;
	}
	public void setPkEmpOcc2(String pkEmpOcc2) {
		this.pkEmpOcc2 = pkEmpOcc2;
	}
	public String getNameEmpOcc() {
		return nameEmpOcc;
	}
	public void setNameEmpOcc(String nameEmpOcc) {
		this.nameEmpOcc = nameEmpOcc;
	}
	public String getNameEmpOcc2() {
		return nameEmpOcc2;
	}
	public void setNameEmpOcc2(String nameEmpOcc2) {
		this.nameEmpOcc2 = nameEmpOcc2;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public String getNameDeptOcc() {
		return nameDeptOcc;
	}
	public void setNameDeptOcc(String nameDeptOcc) {
		this.nameDeptOcc = nameDeptOcc;
	}
	public String getNameOrgOcc() {
		return nameOrgOcc;
	}
	public void setNameOrgOcc(String nameOrgOcc) {
		this.nameOrgOcc = nameOrgOcc;
	}
	public String getFlagAp() {
		return flagAp;
	}
	public void setFlagAp(String flagAp) {
		this.flagAp = flagAp;
	}
	public String getFlagBack() {
		return flagBack;
	}
	public void setFlagBack(String flagBack) {
		this.flagBack = flagBack;
	}
	public String getFlagDe() {
		return flagDe;
	}
	public void setFlagDe(String flagDe) {
		this.flagDe = flagDe;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public String getFlagDurg() {
		return flagDurg;
	}
	public void setFlagDurg(String flagDurg) {
		this.flagDurg = flagDurg;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
	public String getInfantNo() {
		return infantNo;
	}
	public void setInfantNo(String infantNo) {
		this.infantNo = infantNo;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getCodeSupply() {
		return codeSupply;
	}
	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}
	public int getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(int ordsn) {
		this.ordsn = ordsn;
	}
	public int getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(int ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getFlagSt() {
		return flagSt;
	}
	public void setFlagSt(String flagSt) {
		this.flagSt = flagSt;
	}
	public String getNameFreq() {
		return nameFreq;
	}
	public void setNameFreq(String nameFreq) {
		this.nameFreq = nameFreq;
	}
	public String getNameSupply() {
		return nameSupply;
	}
	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}
	public String getIsskt() {
		return isskt;
	}
	public void setIsskt(String isskt) {
		this.isskt = isskt;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getNameUnitDos() {
		return nameUnitDos;
	}

	public void setNameUnitDos(String nameUnitDos) {
		this.nameUnitDos = nameUnitDos;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDatePlanEx() {
		return datePlanEx;
	}

	public void setDatePlanEx(Date datePlanEx) {
		this.datePlanEx = datePlanEx;
	}

	public String getPkEmpEx() {
		return pkEmpEx;
	}

	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}

	public String getNameEmpEx() {
		return nameEmpEx;
	}

	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}

	public Double getPriceCg() {
		return priceCg;
	}

	public void setPriceCg(Double priceCg) {
		this.priceCg = priceCg;
	}

	public int getCntFreq() {
		return cntFreq;
	}

	public void setCntFreq(int cntFreq) {
		this.cntFreq = cntFreq;
	}

	public String getFlagPivas() {
		return flagPivas;
	}

	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}

	public Double getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(Double firstNum) {
		this.firstNum = firstNum;
	}

	public Double getLastNum() {
		return lastNum;
	}

	public void setLastNum(Double lastNum) {
		this.lastNum = lastNum;
	}

	public String getOrdtype() {
		return ordtype;
	}

	public void setOrdtype(String ordtype) {
		this.ordtype = ordtype;
	}

	public Double getGroupno() {
		return groupno;
	}

	public void setGroupno(Double groupno) {
		this.groupno = groupno;
	}

	public String getFlagMedout() {
		return flagMedout;
	}

	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}

	public String getEuAddcharge() {
		return euAddcharge;
	}

	public void setEuAddcharge(String euAddcharge) {
		this.euAddcharge = euAddcharge;
	}

	public String getRisEuStatus() {
		return risEuStatus;
	}

	public void setRisEuStatus(String risEuStatus) {
		this.risEuStatus = risEuStatus;
	}

	public String getCodeOrdtype() { return codeOrdtype; }
	public void setCodeOrdtype(String codeOrdtype) { this.codeOrdtype = codeOrdtype; }

	public String getNameDept() {return nameDept;}

	public void setNameDept(String nameDept) {this.nameDept = nameDept;}

	public String getFlagNote() {
		return flagNote;
	}

	public void setFlagNote(String flagNote) {
		this.flagNote = flagNote;
	}

	public String getPkPdapdt() {
		return pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public String getConsultFlag() {
		return consultFlag;
	}

	public void setConsultFlag(String consultFlag) {
		this.consultFlag = consultFlag;
	}

	public String getOtherDeptOrderAndFeeCheckFlag() {
		return otherDeptOrderAndFeeCheckFlag;
	}

	public void setOtherDeptOrderAndFeeCheckFlag(String otherDeptOrderAndFeeCheckFlag) {
		this.otherDeptOrderAndFeeCheckFlag = otherDeptOrderAndFeeCheckFlag;
	}
}
