package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnPrescription;

public class SyxCnOpPresVo extends CnPrescription{

	private String pkCnord;
	private String codeSupply;
	private String nameDept;
	private Long ords;
	private String noteOrd;
	private String euPvtype;
	private String codeOrdtype;
	private String codeOrd;
	private String nameOrd;
	private String euStatusOrd;

	    private Date dateEffe;

	    private String euAlways;

		private Integer sortIv;
		
	    private Integer ordsn;

	    private Integer ordsnParent;
		
	    private Integer groupno;

	    private String pkOrd;

	    private String descOrd;

	    private String codeApply;

	    private String codeFreq;

	    private String spec;

	    private Double dosage;

	    private String pkUnitDos;

	    private Double quan;

	    private String pkUnit;

	    private Double quanCg;

	    private String pkUnitCg;

	    private Double packSize;

	    private Double priceCg;

	    private String noteSupply;

	    private Long days;

	    private Integer dripSpeed;
		
	    private String flagFirst;

	    private Long lastNum;

	    private Date dateEnter;

	    private Date dateStart;

	    private String flagDurg;

	    private String flagNote;

	    private String flagBase;

	    private String flagBl;

	    private String pkWg;

	    private String pkEmpInput;

	    private String nameEmpInput;

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

	    private Integer infantNo;

	    private String pkEvent;

	    private String flagPrint;

	    private String flagMedout;

	    private String euExctype;

	    private String pkOrdExc;

	    private String flagEmer;

	    private String flagThera;

	    private String flagPrev;

	    private String flagFit;

	    private Double quanBed;

	    private String flagSign;
		
		private String euIntern;
		
		private String euSt;
		
		private String dtUsagenote;
		
		private Integer firstNum;
		
		private String flagPivas;
		
		private String dtHerbusage;
		
		private String descFit;
		
	    private String flagItc;
		
	    private String pkEmpEx;//执行护士主键

	    private String nameEmpEx;//执行护士名称
	
	    private String pkCnordRl;//关联医嘱

	private String euInjury;//伤病判定

	public String getEuInjury() {
		return euInjury;
	}

	public void setEuInjury(String euInjury) {
		this.euInjury = euInjury;
	}

	private List<SyxOpHerbVo>  herbOrders;

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getCodeSupply() {
		return codeSupply;
	}

	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}

	public List<SyxOpHerbVo> getHerbOrders() {
		return herbOrders;
	}

	public void setHerbOrders(List<SyxOpHerbVo> herbOrders) {
		this.herbOrders = herbOrders;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getNoteOrd() {
		return noteOrd;
	}

	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getCodeOrd() {
		return codeOrd;
	}

	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getEuStatusOrd() {
		return euStatusOrd;
	}

	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}


	public Date getDateEffe() {
		return dateEffe;
	}

	public void setDateEffe(Date dateEffe) {
		this.dateEffe = dateEffe;
	}

	public String getEuAlways() {
		return euAlways;
	}

	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}

	public Integer getSortIv() {
		return sortIv;
	}

	public void setSortIv(Integer sortIv) {
		this.sortIv = sortIv;
	}

	public Integer getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}

	public Integer getOrdsnParent() {
		return ordsnParent;
	}

	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}

	public Integer getGroupno() {
		return groupno;
	}

	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
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

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getPkUnitDos() {
		return pkUnitDos;
	}

	public void setPkUnitDos(String pkUnitDos) {
		this.pkUnitDos = pkUnitDos;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public Double getQuanCg() {
		return quanCg;
	}

	public void setQuanCg(Double quanCg) {
		this.quanCg = quanCg;
	}

	public String getPkUnitCg() {
		return pkUnitCg;
	}

	public void setPkUnitCg(String pkUnitCg) {
		this.pkUnitCg = pkUnitCg;
	}

	public Double getPackSize() {
		return packSize;
	}

	public void setPackSize(Double packSize) {
		this.packSize = packSize;
	}

	public Double getPriceCg() {
		return priceCg;
	}

	public void setPriceCg(Double priceCg) {
		this.priceCg = priceCg;
	}

	public String getNoteSupply() {
		return noteSupply;
	}

	public void setNoteSupply(String noteSupply) {
		this.noteSupply = noteSupply;
	}

	public Long getDays() {
		return days;
	}

	public void setDays(Long days) {
		this.days = days;
	}

	public Integer getDripSpeed() {
		return dripSpeed;
	}

	public void setDripSpeed(Integer dripSpeed) {
		this.dripSpeed = dripSpeed;
	}

	public String getFlagFirst() {
		return flagFirst;
	}

	public void setFlagFirst(String flagFirst) {
		this.flagFirst = flagFirst;
	}

	public Long getLastNum() {
		return lastNum;
	}

	public void setLastNum(Long lastNum) {
		this.lastNum = lastNum;
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

	public Integer getInfantNo() {
		return infantNo;
	}

	public void setInfantNo(Integer infantNo) {
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

	public Double getQuanBed() {
		return quanBed;
	}

	public void setQuanBed(Double quanBed) {
		this.quanBed = quanBed;
	}

	public String getFlagSign() {
		return flagSign;
	}

	public void setFlagSign(String flagSign) {
		this.flagSign = flagSign;
	}

	public String getEuIntern() {
		return euIntern;
	}

	public void setEuIntern(String euIntern) {
		this.euIntern = euIntern;
	}

	public String getEuSt() {
		return euSt;
	}

	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}

	public String getDtUsagenote() {
		return dtUsagenote;
	}

	public void setDtUsagenote(String dtUsagenote) {
		this.dtUsagenote = dtUsagenote;
	}

	public Integer getFirstNum() {
		return firstNum;
	}

	public void setFirstNum(Integer firstNum) {
		this.firstNum = firstNum;
	}

	public String getFlagPivas() {
		return flagPivas;
	}

	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}

	public String getDtHerbusage() {
		return dtHerbusage;
	}

	public void setDtHerbusage(String dtHerbusage) {
		this.dtHerbusage = dtHerbusage;
	}

	public String getDescFit() {
		return descFit;
	}

	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}

	public String getFlagItc() {
		return flagItc;
	}

	public void setFlagItc(String flagItc) {
		this.flagItc = flagItc;
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

	public String getPkCnordRl() {
		return pkCnordRl;
	}

	public void setPkCnordRl(String pkCnordRl) {
		this.pkCnordRl = pkCnordRl;
	}

	public Long getOrds() {
		return ords;
	}

	public void setOrds(Long ords) {
		this.ords = ords;
	}
	
	
}
