package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CP_TEMP_CPORD")
public class SyxCpTempCpord extends BaseModule{

	@PK
	@Field(value="PK_CPORD",id=KeyId.UUID)
    private String pkCpord;

	@Field(value="PK_CPPHASE")
    private String pkCpphase;

	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="EU_TASKTYPE")
    private String euTasktype;
	
	@Field(value="EU_ALWAYS")
    private String euAlways;
	
	@Field(value="NAME_FORM")
	private String nameForm;
	
	@Field(value="EU_CPORDTYPE")
	private String euCpordtype;
	
	@Field(value="CODE_ORDTYPE")
	private String codeOrdtype;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;
	
	@Field(value="NAME_ORD")
	private String nameOrd;
	
	@Field(value="DOSAGE")
    private Double dosage;
	
	@Field(value="PK_UNIT_DOSE")
    private String pkUnitDose;
	
	@Field(value="CODE_SUPPLY")
	private String codeSupply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="PK_PARENT")
    private String pkParent;
	
	@Field(value="DT_PHARM")
	private String dtPharm;

	@Field(value="FLAG_NEC")
    private String flagNec;
	
	@Field(value="CODE_OP")
	private String codeOp;
	
	@Field(value="NAME_OP")
	private String nameOp;
	
	@Field(value="DT_ANAE")
	private String dtAnae;
	
	@Field(value="DESC_OP")
	private String descOp;
	
	@Field(value="NOTE")
    private String note;
	
	@Field(value="FLAG_PRINT")
    private String flagPrint;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	private String euReptype;

	public String getPkCpord() {
		return pkCpord;
	}

	public void setPkCpord(String pkCpord) {
		this.pkCpord = pkCpord;
	}

	public String getPkCpphase() {
		return pkCpphase;
	}

	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public String getEuTasktype() {
		return euTasktype;
	}

	public void setEuTasktype(String euTasktype) {
		this.euTasktype = euTasktype;
	}

	public String getEuAlways() {
		return euAlways;
	}

	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}

	public String getNameForm() {
		return nameForm;
	}

	public void setNameForm(String nameForm) {
		this.nameForm = nameForm;
	}

	public String getEuCpordtype() {
		return euCpordtype;
	}

	public void setEuCpordtype(String euCpordtype) {
		this.euCpordtype = euCpordtype;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
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

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public Double getDosage() {
		return dosage;
	}

	public void setDosage(Double dosage) {
		this.dosage = dosage;
	}

	public String getPkUnitDose() {
		return pkUnitDose;
	}

	public void setPkUnitDose(String pkUnitDose) {
		this.pkUnitDose = pkUnitDose;
	}

	public String getCodeSupply() {
		return codeSupply;
	}

	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}

	public String getCodeFreq() {
		return codeFreq;
	}

	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}

	public String getPkParent() {
		return pkParent;
	}

	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}

	public String getDtPharm() {
		return dtPharm;
	}

	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}

	public String getFlagNec() {
		return flagNec;
	}

	public void setFlagNec(String flagNec) {
		this.flagNec = flagNec;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEuReptype() {
		return euReptype;
	}

	public void setEuReptype(String euReptype) {
		this.euReptype = euReptype;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getNameOp() {
		return nameOp;
	}

	public void setNameOp(String nameOp) {
		this.nameOp = nameOp;
	}

	public String getDtAnae() {
		return dtAnae;
	}

	public void setDtAnae(String dtAnae) {
		this.dtAnae = dtAnae;
	}

	public String getDescOp() {
		return descOp;
	}

	public void setDescOp(String descOp) {
		this.descOp = descOp;
	}

	public String getFlagPrint() {
		return flagPrint;
	}

	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}
	
	
}
