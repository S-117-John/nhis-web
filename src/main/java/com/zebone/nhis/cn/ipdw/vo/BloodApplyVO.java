package com.zebone.nhis.cn.ipdw.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

public class BloodApplyVO {	
	private String pkOrg;
    private String pkCnord;
    private String euPvtype;
    private String pkPv;
    private String pkPi;
    private Date dateEffe;
    private String codeOrdtype;    
    private String euAlways;
    private BigDecimal ordsn;
    private BigDecimal ordsnParent;
    private String pkOrd;
    private String codeOrd;
    private String pkPres;    
    private String nameOrd;
    private String descOrd;
    private String codeApply;
    private String codeFreq;
    private String spec;
    private BigDecimal dosage;
    private String pkUnitDos;
    private BigDecimal quan;
    private String pkUnit;
    private String codeSupply;
    private BigDecimal quanCg;	
    private String pkUnitCg;
    private BigDecimal packSize;
    private BigDecimal priceCg;
    private String noteSupply;
    private BigDecimal days;	
    private BigDecimal dripSpeed;
    private BigDecimal ords;
    private String flagFirst;
    private BigDecimal lastNum;
    private String pkOrgExec;
    private String pkDeptExec;
    private String euStatusOrd;
    private Date dateEnter;
    private Date dateStart;
    private String flagDurg;
    private String flagSelf;
    private String flagNote;
    private String flagBase;
    private String flagBl;
    private String pkDept;
    private String pkDeptNs;
    private String pkWg;
    private String pkEmpInput;	
    private String nameEmpInput;
    private String pkEmpOrd;
    private String nameEmpOrd;
    private Date dateSign;
    private String pkEmpChk;
    private String nameEmpChk;
    private Date dateChk;
    private Date dateLastEx;
    private Date datePlanEx;
    private Date dateStop;
    private String pkEmpStop;
    private String nameEmpStop;
    private String flagStop;
    private Date dateStopChk;
    private String pkEmpStopChk;
    private String nameEmpStopChk;
    private String flagStopChk;
    private Date dateErase;
    private String pkEmpErase;
    private String nameEmpErase;
    private String flagErase;
    private Date dateEraseChk;
    private String pkEmpEraseChk;
    private String nameEraseChk;
    private String flagEraseChk;
    private String flagCp;
    private String flagDoctor;
    private BigDecimal infantNo;
    private String pkEvent;
    private String flagPrint;
    private String flagMedout;
    private String euExctype;
    private String pkOrdExc;
    private String flagEmer;
    private String flagThera;
    private String flagPrev;
    private String flagFit;
    private BigDecimal quanBed;
	private String noteOrd;
    private String modifier;
    private Date modityTime;
    private BloodPatientVO bloodPatientVO;
    private String flagSign;
    private String deptName;
    private String dtBtAboName;
    private String dtBtRhName;
    private String pkUnitBtName;
    private String note;
    private String dtBttype;
	private String btContent;
    private String flagBthis;
    private String flagLab;
    private String flagPreg;
    private String flagAl;
    private String pkCprec;
    private String pkCpexp;
    private String expNote;
    private String euIntern;
    private String pkCpphase;
    private String nameExp;
    private Date ts;
    private String codeApplyLab;
    private String descFit;

    public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getNameExp() {
		return nameExp;
	}
	public void setNameExp(String nameExp) {
		this.nameExp = nameExp;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getDtBttype() {
		return dtBttype;
	}
	public void setDtBttype(String dtBttype) {
		this.dtBttype = dtBttype;
	}
	public String getBtContent() {
		return btContent;
	}
	public void setBtContent(String btContent) {
		this.btContent = btContent;
	}

    public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDtBtAboName() {
		return dtBtAboName;
	}
	public void setDtBtAboName(String dtBtAboName) {
		this.dtBtAboName = dtBtAboName;
	}
	public String getDtBtRhName() {
		return dtBtRhName;
	}
	public void setDtBtRhName(String dtBtRhName) {
		this.dtBtRhName = dtBtRhName;
	}
	public String getPkUnitBtName() {
		return pkUnitBtName;
	}
	public void setPkUnitBtName(String pkUnitBtName) {
		this.pkUnitBtName = pkUnitBtName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
       
    public String getFlagSign() {
		return flagSign;
	}
	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
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
	public Date getDateEffe() {
		return dateEffe;
	}
	public void setDateEffe(Date dateEffe) {
		this.dateEffe = dateEffe;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public BigDecimal getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(BigDecimal ordsn) {
		this.ordsn = ordsn;
	}
	public BigDecimal getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(BigDecimal ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getCodeOrd() {
		return codeOrd;
	}
	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}
	public String getPkPres() {
		return pkPres;
	}
	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getDescOrd() {
		return descOrd;
	}
	public void setDescOrd(String descOrd) {
		this.descOrd = descOrd;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getCodeFreq() {
		return codeFreq;
	}
	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public BigDecimal getDosage() {
		return dosage;
	}
	public void setDosage(BigDecimal dosage) {
		this.dosage = dosage;
	}
	public String getPkUnitDos() {
		return pkUnitDos;
	}
	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}
	public BigDecimal getQuan() {
		return quan;
	}
	public void setQuan(BigDecimal quan) {
		this.quan = quan;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getCodeSupply() {
		return codeSupply;
	}
	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}
	public BigDecimal getQuanCg() {
		return quanCg;
	}
	public void setQuanCg(BigDecimal quanCg) {
		this.quanCg = quanCg;
	}
	public String getPkUnitCg() {
		return pkUnitCg;
	}
	public void setPkUnitCg(String pkUnitCg) {
		this.pkUnitCg = pkUnitCg;
	}
	public BigDecimal getPackSize() {
		return packSize;
	}
	public void setPackSize(BigDecimal packSize) {
		this.packSize = packSize;
	}
	public BigDecimal getPriceCg() {
		return priceCg;
	}
	public void setPriceCg(BigDecimal priceCg) {
		this.priceCg = priceCg;
	}
	public String getNoteSupply() {
		return noteSupply;
	}
	public void setNoteSupply(String noteSupply) {
		this.noteSupply = noteSupply;
	}
	public BigDecimal getDays() {
		return days;
	}
	public void setDays(BigDecimal days) {
		this.days = days;
	}
	public BigDecimal getDripSpeed() {
		return dripSpeed;
	}
	public void setDripSpeed(BigDecimal dripSpeed) {
		this.dripSpeed = dripSpeed;
	}
	public BigDecimal getOrds() {
		return ords;
	}
	public void setOrds(BigDecimal ords) {
		this.ords = ords;
	}
	public String getFlagFirst() {
		return flagFirst;
	}
	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}
	public BigDecimal getLastNum() {
		return lastNum;
	}
	public void setLastNum(BigDecimal lastNum) {
		this.lastNum = lastNum;
	}
	public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getEuStatusOrd() {
		return euStatusOrd;
	}
	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}
	public Date getDateEnter() {
		return dateEnter;
	}
	public void setDateEnter(Date dateEnter) {
		this.dateEnter = dateEnter;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public String getFlagDurg() {
		return flagDurg;
	}
	public void setFlagDurg(String flagDurg) {
		this.flagDurg = flagDurg;
	}
	public String getFlagSelf() {
		return flagSelf;
	}
	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}
	public String getFlagNote() {
		return flagNote;
	}
	public void setFlagNote(String flagNote) {
		this.flagNote = flagNote;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getPkWg() {
		return pkWg;
	}
	public void setPkWg(String pkWg) {
		this.pkWg = pkWg;
	}
	public String getPkEmpInput() {
		return pkEmpInput;
	}
	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}
	public String getNameEmpInput() {
		return nameEmpInput;
	}
	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
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
	public Date getDateSign() {
		return dateSign;
	}
	public void setDateSign(Date dateSign) {
		this.dateSign = dateSign;
	}
	public String getPkEmpChk() {
		return pkEmpChk;
	}
	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}
	public String getNameEmpChk() {
		return nameEmpChk;
	}
	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	public Date getDateLastEx() {
		return dateLastEx;
	}
	public void setDateLastEx(Date dateLastEx) {
		this.dateLastEx = dateLastEx;
	}
	public Date getDatePlanEx() {
		return datePlanEx;
	}
	public void setDatePlanEx(Date datePlanEx) {
		this.datePlanEx = datePlanEx;
	}
	public Date getDateStop() {
		return dateStop;
	}
	public void setDateStop(Date dateStop) {
		this.dateStop = dateStop;
	}
	public String getPkEmpStop() {
		return pkEmpStop;
	}
	public void setPkEmpStop(String pkEmpStop) {
		this.pkEmpStop = pkEmpStop;
	}
	public String getNameEmpStop() {
		return nameEmpStop;
	}
	public void setNameEmpStop(String nameEmpStop) {
		this.nameEmpStop = nameEmpStop;
	}
	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public Date getDateStopChk() {
		return dateStopChk;
	}
	public void setDateStopChk(Date dateStopChk) {
		this.dateStopChk = dateStopChk;
	}
	public String getPkEmpStopChk() {
		return pkEmpStopChk;
	}
	public void setPkEmpStopChk(String pkEmpStopChk) {
		this.pkEmpStopChk = pkEmpStopChk;
	}
	public String getNameEmpStopChk() {
		return nameEmpStopChk;
	}
	public void setNameEmpStopChk(String nameEmpStopChk) {
		this.nameEmpStopChk = nameEmpStopChk;
	}
	public String getFlagStopChk() {
		return flagStopChk;
	}
	public void setFlagStopChk(String flagStopChk) {
		this.flagStopChk = flagStopChk;
	}
	public Date getDateErase() {
		return dateErase;
	}
	public void setDateErase(Date dateErase) {
		this.dateErase = dateErase;
	}
	public String getPkEmpErase() {
		return pkEmpErase;
	}
	public void setPkEmpErase(String pkEmpErase) {
		this.pkEmpErase = pkEmpErase;
	}
	public String getNameEmpErase() {
		return nameEmpErase;
	}
	public void setNameEmpErase(String nameEmpErase) {
		this.nameEmpErase = nameEmpErase;
	}
	public String getFlagErase() {
		return flagErase;
	}
	public void setFlagErase(String flagErase) {
		this.flagErase = flagErase;
	}
	public Date getDateEraseChk() {
		return dateEraseChk;
	}
	public void setDateEraseChk(Date dateEraseChk) {
		this.dateEraseChk = dateEraseChk;
	}
	public String getPkEmpEraseChk() {
		return pkEmpEraseChk;
	}
	public void setPkEmpEraseChk(String pkEmpEraseChk) {
		this.pkEmpEraseChk = pkEmpEraseChk;
	}
	public String getNameEraseChk() {
		return nameEraseChk;
	}
	public void setNameEraseChk(String nameEraseChk) {
		this.nameEraseChk = nameEraseChk;
	}
	public String getFlagEraseChk() {
		return flagEraseChk;
	}
	public void setFlagEraseChk(String flagEraseChk) {
		this.flagEraseChk = flagEraseChk;
	}
	public String getFlagCp() {
		return flagCp;
	}
	public void setFlagCp(String flagCp) {
		this.flagCp = flagCp;
	}
	public String getFlagDoctor() {
		return flagDoctor;
	}
	public void setFlagDoctor(String flagDoctor) {
		this.flagDoctor = flagDoctor;
	}
	public BigDecimal getInfantNo() {
		return infantNo;
	}
	public void setInfantNo(BigDecimal infantNo) {
		this.infantNo = infantNo;
	}
	public String getPkEvent() {
		return pkEvent;
	}
	public void setPkEvent(String pkEvent) {
		this.pkEvent = pkEvent;
	}
	public String getFlagPrint() {
		return flagPrint;
	}
	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}
	public String getFlagMedout() {
		return flagMedout;
	}
	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}
	public String getEuExctype() {
		return euExctype;
	}
	public void setEuExctype(String euExctype) {
		this.euExctype = euExctype;
	}
	public String getPkOrdExc() {
		return pkOrdExc;
	}
	public void setPkOrdExc(String pkOrdExc) {
		this.pkOrdExc = pkOrdExc;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public String getFlagThera() {
		return flagThera;
	}
	public void setFlagThera(String flagThera) {
		this.flagThera = flagThera;
	}
	public String getFlagPrev() {
		return flagPrev;
	}
	public void setFlagPrev(String flagPrev) {
		this.flagPrev = flagPrev;
	}
	public String getFlagFit() {
		return flagFit;
	}
	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}
	public BigDecimal getQuanBed() {
		return quanBed;
	}
	public void setQuanBed(BigDecimal quanBed) {
		this.quanBed = quanBed;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public BloodPatientVO getBloodPatientVO() {
		return bloodPatientVO;
	}
	public void setBloodPatientVO(BloodPatientVO bloodPatientVO) {
		this.bloodPatientVO = bloodPatientVO;
	}
	public String getFlagBthis() {
		return flagBthis;
	}
	public void setFlagBthis(String flagBthis) {
		this.flagBthis = flagBthis;
	}
	public String getFlagLab() {
		return flagLab;
	}
	public void setFlagLab(String flagLab) {
		this.flagLab = flagLab;
	}
	public String getFlagPreg() {
		return flagPreg;
	}
	public void setFlagPreg(String flagPreg) {
		this.flagPreg = flagPreg;
	}
	public String getFlagAl() {
		return flagAl;
	}
	public void setFlagAl(String flagAl) {
		this.flagAl = flagAl;
	}
	public String getPkCprec() {
		return pkCprec;
	}
	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
	}
	public String getPkCpexp() {
		return pkCpexp;
	}
	public void setPkCpexp(String pkCpexp) {
		this.pkCpexp = pkCpexp;
	}
	public String getExpNote() {
		return expNote;
	}
	public void setExpNote(String expNote) {
		this.expNote = expNote;
	}
	public String getEuIntern() {
		return euIntern;
	}
	public void setEuIntern(String euIntern) {
		this.euIntern = euIntern;
	}

	public String getCodeApplyLab() {
		return codeApplyLab;
	}

	public void setCodeApplyLab(String codeApplyLab) {
		this.codeApplyLab = codeApplyLab;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}
}
